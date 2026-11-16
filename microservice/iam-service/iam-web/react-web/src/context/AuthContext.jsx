import React, { createContext, useContext, useState, useEffect } from 'react';
import { initialCurrentUser, store } from '../services/mockData';
import { api } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const cached = store.get('current_user');
    return cached || null;
  });

  const [activeTab, setActiveTab] = useState('profile');
  const [currentMode, setCurrentMode] = useState('account'); // 'account' | 'management'
  const [authView, setAuthView] = useState(() => {
    if (typeof window !== 'undefined' && window.location.pathname.includes('/register')) {
      return 'register';
    }
    return !store.get('current_user') ? 'login' : null;
  }); // null = authenticated, 'login' | 'register'

  // Sync session with Spring Boot backend if running
  useEffect(() => {
    const checkSpringSession = async () => {
      try {
        const res = await fetch('/web-api/me');
        if (res.ok) {
          const data = await res.json();
          if (data && data.authenticated) {
            const isAdminUser = data.roles && data.roles.includes('ADMIN');
            const springUser = {
              ...initialCurrentUser,
              username: data.username,
              email: data.email,
              givenName: data.givenName || data.username,
              familyName: data.familyName || '',
              nickname: data.nickname || '',
              phoneNumber: data.phoneNumber || '',
              roles: data.roles || ['USER'],
              effectivePermissions: data.permissions && data.permissions.length > 0
                ? data.permissions
                : (isAdminUser ? initialCurrentUser.effectivePermissions : ["user:read", "profile:read", "profile:update", "session:read"]),
              claims: data.claims || {}
            };
            setUser(springUser);
            setAuthView(null);
            setCurrentMode(isAdminUser ? 'management' : 'account');
            setActiveTab(isAdminUser ? 'overview' : 'profile');
            return;
          }
        }
      } catch (e) {
        // Fallback silently if offline or running standalone
      }

      // Check URL query parameters for logout
      const params = new URLSearchParams(window.location.search);
      if (params.has('logout')) {
        setUser(null);
        setAuthView('login');
      }
    };

    checkSpringSession();
  }, []);

  useEffect(() => {
    if (user) {
      store.set('current_user', user);
    }
  }, [user]);

  const login = async (username, password) => {
    // 1. Try real Spring Boot form login
    try {
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);

      const response = await fetch('/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData,
        redirect: 'follow'
      });

      if (response.ok) {
        const meRes = await fetch('/web-api/me');
        if (meRes.ok) {
          const data = await meRes.json();
          if (data && data.authenticated) {
            const isAdminUser = data.roles && data.roles.includes('ADMIN');
            const springUser = {
              ...initialCurrentUser,
              username: data.username,
              email: data.email,
              givenName: data.givenName || data.username,
              familyName: data.familyName || '',
              nickname: data.nickname || '',
              phoneNumber: data.phoneNumber || '',
              roles: data.roles || ['USER'],
              effectivePermissions: data.permissions && data.permissions.length > 0
                ? data.permissions
                : (isAdminUser ? initialCurrentUser.effectivePermissions : ["user:read", "profile:read", "profile:update"]),
              claims: data.claims || {}
            };
            setUser(springUser);
            setAuthView(null);
            setCurrentMode(isAdminUser ? 'management' : 'account');
            setActiveTab(isAdminUser ? 'overview' : 'profile');
            api.logActivity("LOGIN_SUCCESS", `Authenticated via Spring Security session (${springUser.username})`);
            return springUser;
          }
        }
      }
    } catch (e) {
      // Backend not running or blocked, fallback to client-side state
    }

    // 2. Client-side fallback authentication for database users
    const users = store.get('users') || [];
    const matched = users.find(u => u.username?.toLowerCase() === username.toLowerCase() || u.email?.toLowerCase() === username.toLowerCase());

    if (matched) {
      if (!matched.enabled) {
        throw new Error("This account is currently disabled. Please contact an administrator.");
      }
      if (!matched.accountNonLocked) {
        throw new Error("This account is currently locked due to security policy violations.");
      }

      const fullUser = {
        ...matched,
        effectivePermissions: matched.roles?.includes('ADMIN')
          ? initialCurrentUser.effectivePermissions
          : ["user:read", "profile:read", "profile:update", "session:read"]
      };

      setUser(fullUser);
      setAuthView(null);
      setCurrentMode(fullUser.roles?.includes('ADMIN') ? 'management' : 'account');
      setActiveTab(fullUser.roles?.includes('ADMIN') ? 'overview' : 'profile');

      api.logActivity("LOGIN_SUCCESS", `Signed in via credentials (${fullUser.username})`);
      return fullUser;
    }

    throw new Error("Invalid username or password. Please verify your credentials.");
  };

  const register = async (userData) => {
    // 1. Register directly in PostgreSQL database via backend API
    await api.registerUser(userData);

    // 2. Automatically sign in with the new credentials
    try {
      const loggedIn = await login(userData.username, userData.password);
      return loggedIn;
    } catch (e) {
      // Fallback: set user session state
      const newUser = {
        ...initialCurrentUser,
        username: userData.username,
        email: userData.email,
        givenName: userData.givenName || userData.username,
        familyName: userData.familyName || "",
        roles: ["USER"],
        effectivePermissions: ["user:read", "profile:read", "profile:update", "session:read"]
      };
      setUser(newUser);
      setAuthView(null);
      setCurrentMode('account');
      setActiveTab('profile');
      return newUser;
    }
  };

  const logout = async () => {
    try {
      await fetch('/logout', { method: 'POST' });
    } catch (e) {
      // Ignore network errors
    }
    setUser(null);
    store.set('current_user', null);
    setAuthView('login');
  };

  const updateProfileState = (updatedFields) => {
    setUser(prev => {
      const next = { ...prev, ...updatedFields };
      store.set('current_user', next);
      return next;
    });
  };

  const isAdmin = user?.roles?.includes('ADMIN');

  return (
    <AuthContext.Provider value={{
      user,
      isAdmin,
      currentMode,
      setCurrentMode,
      activeTab,
      setActiveTab,
      authView,
      setAuthView,
      login,
      register,
      logout,
      updateProfileState
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
