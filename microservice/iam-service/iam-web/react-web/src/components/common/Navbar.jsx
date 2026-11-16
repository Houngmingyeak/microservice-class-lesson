import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import { IconShield, IconLogOut, IconGlobe, IconUser, IconSliders, IconLock } from './Icons';
import { Badge } from './Badge';

export const Navbar = () => {
  const { user, isAdmin, currentMode, setCurrentMode, setActiveTab, logout } = useAuth();
  const { locale, setLocale, t } = useI18n();

  const toggleLanguage = () => {
    setLocale(locale === 'en' ? 'km' : 'en');
  };

  const handleSwitchMode = (mode) => {
    setCurrentMode(mode);
    if (mode === 'management') {
      setActiveTab('overview');
    } else {
      setActiveTab('profile');
    }
  };

  return (
    <header className="navbar-root">
      <div className="navbar-left">
        <div className="navbar-brand">
          <div className="brand-logo-icon">
            <IconShield size={24} className="text-indigo-400" />
          </div>
          <div className="brand-texts">
            <span className="brand-title">{t('appName')}</span>
            <span className="brand-subtitle">{t('tagline')}</span>
          </div>
        </div>

        {isAdmin && (
          <div className="mode-toggle-group">
            <button
              onClick={() => handleSwitchMode('account')}
              className={`mode-btn ${currentMode === 'account' ? 'mode-btn-active' : ''}`}
            >
              <IconUser size={15} />
              <span>{t('navAccount')}</span>
            </button>
            <button
              onClick={() => handleSwitchMode('management')}
              className={`mode-btn ${currentMode === 'management' ? 'mode-btn-active' : ''}`}
            >
              <IconSliders size={15} />
              <span>{t('navManagement')}</span>
              <span className="admin-chip">Admin</span>
            </button>
          </div>
        )}
      </div>

      <div className="navbar-right">
        {/* Language Switcher */}
        <button
          onClick={toggleLanguage}
          className="lang-toggle-btn"
          title={locale === 'en' ? 'Switch to Khmer (ប្តូរជាភាសាខ្មែរ)' : 'Switch to English'}
        >
          <IconGlobe size={16} />
          <span className="font-semibold">{locale === 'en' ? 'ខ្មែរ' : 'EN'}</span>
        </button>

        {/* User Card */}
        {user && (
          <div className="nav-user-profile">
            <img
              src={user.pictureUrl || user.avatar || "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=256"}
              alt={user.username}
              className="nav-avatar"
            />
            <div className="nav-user-info">
              <span className="nav-username">{user.givenName ? `${user.givenName} ${user.familyName}` : user.username}</span>
              <div className="nav-role-badges">
                {user.roles?.map(r => (
                  <Badge key={r} variant={r === 'ADMIN' ? 'danger' : 'primary'} size="sm">
                    {r}
                  </Badge>
                ))}
              </div>
            </div>
            <button onClick={logout} className="logout-btn" title={t('common.logout') || "Sign out"}>
              <IconLogOut size={16} />
            </button>
          </div>
        )}
      </div>
    </header>
  );
};
