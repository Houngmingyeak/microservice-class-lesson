import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import {
  IconUser,
  IconLock,
  IconGlobe,
  IconLaptop,
  IconClock,
  IconActivity,
  IconShieldAlert,
  IconUsers,
  IconKey,
  IconShieldCheck,
  IconTerminal,
  IconUnlock,
  IconFileText,
  IconSettings
} from './Icons';

export const Sidebar = () => {
  const { currentMode, activeTab, setActiveTab } = useAuth();
  const { t } = useI18n();

  const accountNavItems = [
    { id: 'profile', label: t('tabProfile'), icon: IconUser, badge: null },
    { id: 'security', label: t('tabSecurity'), icon: IconLock, badge: null },
    { id: 'linkedAccounts', label: t('tabLinkedAccounts'), icon: IconGlobe, badge: null },
    { id: 'activeSessions', label: t('tabActiveSessions'), icon: IconLaptop, badge: null },
    { id: 'loginHistory', label: t('tabLoginHistory'), icon: IconClock, badge: null },
    { id: 'accountActivity', label: t('tabAccountActivity'), icon: IconActivity, badge: null },
    { id: 'deactivation', label: t('tabDeactivation'), icon: IconShieldAlert, badge: null, danger: true },
  ];

  const managementNavItems = [
    { id: 'overview', label: t('tabOverview'), icon: IconActivity, section: 'Telemetry' },
    { id: 'users', label: t('tabUsers'), icon: IconUsers, section: 'Directory' },
    { id: 'userSessions', label: t('tabUserSessions'), icon: IconLaptop, section: 'Directory' },
    { id: 'userLoginHistory', label: t('tabUserLoginHistory'), icon: IconClock, section: 'Directory' },
    { id: 'roles', label: t('tabRoles'), icon: IconKey, section: 'Access Control' },
    { id: 'permissions', label: t('tabPermissions'), icon: IconShieldCheck, section: 'Access Control' },
    { id: 'clients', label: t('tabClients'), icon: IconTerminal, section: 'Federation' },
    { id: 'securityMgmt', label: t('tabSecurityMgmt'), icon: IconUnlock, section: 'Security' },
    { id: 'auditLogs', label: t('tabAuditLogs'), icon: IconFileText, section: 'Security' },
    { id: 'systemConfig', label: t('tabSystemConfig'), icon: IconSettings, section: 'Governance' },
  ];

  const items = currentMode === 'management' ? managementNavItems : accountNavItems;

  return (
    <aside className="sidebar-root">
      <div className="sidebar-header">
        <span className="sidebar-mode-label">
          {currentMode === 'management' ? t('navManagement') : t('navAccount')}
        </span>
      </div>

      <nav className="sidebar-nav">
        {items.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`sidebar-nav-item ${isActive ? 'sidebar-nav-item-active' : ''} ${item.danger ? 'sidebar-nav-item-danger' : ''}`}
            >
              <div className="nav-item-content">
                <Icon size={18} className="nav-item-icon" />
                <span className="nav-item-label">{item.label}</span>
              </div>
              {item.badge && (
                <span className="nav-item-badge">{item.badge}</span>
              )}
            </button>
          );
        })}
      </nav>

      <div className="sidebar-footer">
        <div className="system-status-indicator">
          <span className="status-dot-pulse"></span>
          <span className="text-xs text-slate-400">Spring Auth Server: 39000</span>
        </div>
      </div>
    </aside>
  );
};
