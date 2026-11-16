import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { I18nProvider, useI18n } from './context/I18nContext';
import { Navbar } from './components/common/Navbar';
import { Sidebar } from './components/common/Sidebar';
import { Toast } from './components/common/Toast';

// Auth Views
import { LoginView } from './components/auth/LoginView';
import { RegisterView } from './components/auth/RegisterView';

// Account Views
import { ProfileView } from './components/account/ProfileView';
import { SecurityView } from './components/account/SecurityView';
import { LinkedAccountsView } from './components/account/LinkedAccountsView';
import { ActiveSessionsView } from './components/account/ActiveSessionsView';
import { LoginHistoryView } from './components/account/LoginHistoryView';
import { AccountActivityView } from './components/account/AccountActivityView';
import { DeactivationView } from './components/account/DeactivationView';

// Management Views
import { AdminOverviewView } from './components/management/AdminOverviewView';
import { UserManagementView } from './components/management/UserManagementView';
import { UserSessionsView } from './components/management/UserSessionsView';
import { UserLoginHistoryView } from './components/management/UserLoginHistoryView';
import { RoleManagementView } from './components/management/RoleManagementView';
import { PermissionManagementView } from './components/management/PermissionManagementView';
import { ClientManagementView } from './components/management/ClientManagementView';
import { SecurityManagementView } from './components/management/SecurityManagementView';
import { AuditLogsView } from './components/management/AuditLogsView';
import { SystemConfigView } from './components/management/SystemConfigView';

function MainApp() {
  const { user, activeTab, authView } = useAuth();
  const [toast, setToast] = useState({ message: '', type: 'success' });

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => {
      setToast({ message: '', type: 'success' });
    }, 4000);
  };

  // If user navigated to login or register
  if (!user || authView === 'login') {
    return (
      <>
        <LoginView onShowToast={showToast} />
        {toast.message && (
          <Toast
            message={toast.message}
            type={toast.type}
            onClose={() => setToast({ message: '', type: 'success' })}
          />
        )}
      </>
    );
  }

  if (authView === 'register') {
    return (
      <>
        <RegisterView onShowToast={showToast} />
        {toast.message && (
          <Toast
            message={toast.message}
            type={toast.type}
            onClose={() => setToast({ message: '', type: 'success' })}
          />
        )}
      </>
    );
  }

  // Render view by active tab
  const renderActiveView = () => {
    switch (activeTab) {
      // Account tabs
      case 'profile':
        return <ProfileView onShowToast={showToast} />;
      case 'security':
        return <SecurityView onShowToast={showToast} />;
      case 'linkedAccounts':
        return <LinkedAccountsView onShowToast={showToast} />;
      case 'activeSessions':
        return <ActiveSessionsView onShowToast={showToast} />;
      case 'loginHistory':
        return <LoginHistoryView onShowToast={showToast} />;
      case 'accountActivity':
        return <AccountActivityView onShowToast={showToast} />;
      case 'deactivation':
        return <DeactivationView onShowToast={showToast} />;

      // Management tabs
      case 'overview':
        return <AdminOverviewView onShowToast={showToast} />;
      case 'users':
        return <UserManagementView onShowToast={showToast} />;
      case 'userSessions':
        return <UserSessionsView onShowToast={showToast} />;
      case 'userLoginHistory':
        return <UserLoginHistoryView onShowToast={showToast} />;
      case 'roles':
        return <RoleManagementView onShowToast={showToast} />;
      case 'permissions':
        return <PermissionManagementView onShowToast={showToast} />;
      case 'clients':
        return <ClientManagementView onShowToast={showToast} />;
      case 'securityMgmt':
        return <SecurityManagementView onShowToast={showToast} />;
      case 'auditLogs':
        return <AuditLogsView onShowToast={showToast} />;
      case 'systemConfig':
        return <SystemConfigView onShowToast={showToast} />;

      default:
        return <ProfileView onShowToast={showToast} />;
    }
  };

  return (
    <div className="app-container">
      <Navbar />
      <div className="main-layout">
        <Sidebar />
        <main className="main-content">
          {renderActiveView()}
        </main>
      </div>

      {toast.message && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast({ message: '', type: 'success' })}
        />
      )}
    </div>
  );
}

export default function App() {
  return (
    <I18nProvider>
      <AuthProvider>
        <MainApp />
      </AuthProvider>
    </I18nProvider>
  );
}
