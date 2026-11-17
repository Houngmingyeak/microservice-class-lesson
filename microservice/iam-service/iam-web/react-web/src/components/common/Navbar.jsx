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
          <div className="flex items-center gap-3.5 bg-white border border-slate-200/90 rounded-full pl-2 pr-3.5 py-1.5 shadow-xs hover:shadow-sm hover:border-slate-300 transition">
            {/* Avatar */}
            {user.pictureUrl ? (
              <img
                src={user.pictureUrl}
                alt={user.username}
                className="w-11 h-11 rounded-full object-cover ring-2 ring-indigo-500/20 shadow-xs"
              />
            ) : (
              <div className="w-11 h-11 rounded-full bg-gradient-to-tr from-indigo-600 via-indigo-500 to-indigo-400 text-white font-bold text-xl flex items-center justify-center shadow-xs select-none">
                {user.givenName ? user.givenName.charAt(0).toUpperCase() : (user.username ? user.username.charAt(0).toUpperCase() : 'U')}
              </div>
            )}

            {/* Name and Role */}
            <div className="flex flex-col justify-center">
              <span className="font-bold text-slate-900 text-lg leading-tight">
                {user.givenName ? `${user.givenName} ${user.familyName}` : user.username}
              </span>
              <div className="flex items-center gap-1.5 mt-1">
                {user.roles?.map(r => (
                  <span
                    key={r}
                    className={`inline-flex items-center px-3 py-0.5 rounded-full text-xs font-bold uppercase tracking-wider ${
                      r === 'ADMIN'
                        ? 'bg-rose-50 text-rose-700 border border-rose-200'
                        : 'bg-indigo-50 text-indigo-700 border border-indigo-200'
                    }`}
                  >
                    {r}
                  </span>
                ))}
              </div>
            </div>

            {/* Logout button */}
            <button
              onClick={logout}
              className="w-9 h-9 rounded-full flex items-center justify-center text-slate-400 hover:text-rose-600 hover:bg-rose-50 transition border border-transparent hover:border-rose-100 ml-1"
              title={t('common.logout') || "Sign out"}
            >
              <IconLogOut size={18} />
            </button>
          </div>
        )}
      </div>
    </header>
  );
};
