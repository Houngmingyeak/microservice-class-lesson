import React, { createContext, useContext, useState, useEffect } from 'react';
import { initialCurrentUser, store } from '../services/mockData';
import { api } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const cached = store.get('current_user');
    return cached || initialCurrentUser;
  });

  const [activeTab, setActiveTab] = useState('profile');
  const [currentMode, setCurrentMode] = useState('account'); // 'account' | 'management'
  const [authView, setAuthView] = useState(null); // null = authenticated, 'login' | 'register'

  useEffect(() => {
    store.set('current_user', user);
  }, [user]);

  const login = async (username, password) => {
    // Check against seeded users
    const users = store.get('users') || [];
    const matched = users.find(u => u.username.toLowerCase() === username.toLowerCase() || u.email.toLowerCase() === username.toLowerCase());

    if (matched) {
      if (!matched.enabled) {
        throw new Error("This account is currently disabled. Please contact an administrator.");
      }
      if (!matched.accountNonLocked) {
        throw new Error("This account is currently locked due to security policy violations.");
      }

      const fullUser = {
        ...matched,
        effectivePermissions: matched.roles.includes('ADMIN')
          ? initialCurrentUser.effectivePermissions
          : ["user:read", "profile:read", "profile:update", "session:read"]
      };

      setUser(fullUser);
      setAuthView(null);
      setCurrentMode(fullUser.roles.includes('ADMIN') ? 'management' : 'account');
      setActiveTab(fullUser.roles.includes('ADMIN') ? 'overview' : 'profile');

      api.logActivity("LOGIN_SUCCESS", `Signed in via credentials from web portal (${fullUser.username})`);
      return fullUser;
    }

    // Default admin fallback
    if (username === 'houngmingyeak' || username === 'admin') {
      setUser(initialCurrentUser);
      setAuthView(null);
      setCurrentMode('management');
      setActiveTab('overview');
      return initialCurrentUser;
    }

    throw new Error("Invalid username or password. Please verify your organizational credentials.");
  };

  const quickLogin = (role) => {
    if (role === 'admin') {
      setUser(initialCurrentUser);
      setAuthView(null);
      setCurrentMode('management');
      setActiveTab('overview');
    } else {
      const users = store.get('users') || [];
      const standard = users.find(u => u.username === 'john_doe') || {
        ...initialCurrentUser,
        username: "john_doe",
        givenName: "John",
        familyName: "Doe",
        email: "john.doe@company.com",
        roles: ["USER"],
        effectivePermissions: ["user:read", "profile:read", "profile:update"]
      };
      setUser(standard);
      setAuthView(null);
      setCurrentMode('account');
      setActiveTab('profile');
    }
  };

  const register = async (userData) => {
    const newUser = await api.createUser({
      ...userData,
      roles: ["USER"]
    });
    setUser(newUser);
    setAuthView(null);
    setCurrentMode('account');
    setActiveTab('profile');
    return newUser;
  };

  const logout = () => {
    setUser(null);
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
      quickLogin,
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
