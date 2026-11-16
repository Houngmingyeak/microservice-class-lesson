import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import {
  IconShield,
  IconLock,
  IconUser,
  IconGoogle,
  IconGithub,
  IconFacebook,
  IconTelegram,
  IconGlobe,
  IconCheck
} from '../common/Icons';

export const LoginView = ({ onShowToast }) => {
  const { login, setAuthView } = useAuth();
  const { locale, setLocale, t } = useI18n();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await login(username, password);
      onShowToast(`Welcome back, ${username}!`, 'success');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const toggleLanguage = () => {
    setLocale(locale === 'en' ? 'km' : 'en');
  };

  return (
    <div className="auth-page-root">
      <div className="auth-card-container">
        {/* Top bar with language */}
        <div className="flex justify-between items-center mb-6">
          <div className="flex items-center gap-3">
            <div className="brand-logo-icon">
              <IconShield size={24} className="text-indigo-600" />
            </div>
            <div>
              <h1 className="text-lg font-bold text-slate-900 tracking-tight">{t('appName')}</h1>
              <p className="text-xs text-slate-500">{t('tagline')}</p>
            </div>
          </div>

          <button
            onClick={toggleLanguage}
            className="lang-toggle-btn text-xs py-1 px-2.5"
            title="Toggle Language"
          >
            <IconGlobe size={14} />
            <span>{locale === 'en' ? 'ខ្មែរ' : 'EN'}</span>
          </button>
        </div>

        <div className="mb-6">
          <h2 className="text-xl font-bold text-slate-900 mb-1">{t('signIn')}</h2>
          <p className="text-sm text-slate-500">{t('signInPrompt')}</p>
        </div>

        {/* Credentials Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="form-group">
            <label className="form-label">{t('username')}</label>
            <div className="relative">
              <input
                type="text"
                placeholder="Username or organizational email"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="form-input pl-9"
                required
              />
              <span className="absolute left-3 top-2.5 text-slate-400">
                <IconUser size={16} />
              </span>
            </div>
          </div>

          <div className="form-group">
            <div className="flex justify-between items-center mb-1">
              <label className="form-label mb-0">{t('password')}</label>
            </div>
            <div className="relative">
              <input
                type="password"
                placeholder="••••••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="form-input pl-9"
                required
              />
              <span className="absolute left-3 top-2.5 text-slate-400">
                <IconLock size={16} />
              </span>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary w-full py-2.5 font-semibold text-sm mt-2"
          >
            {loading ? t('loading') : t('signIn')}
          </button>
        </form>

        {/* Social Logins */}
        <div className="mt-6 pt-5 border-t border-slate-200">
          <span className="text-xs text-slate-500 font-medium block mb-3 text-center">
            {t('orContinueWith')}
          </span>
          <div className="grid grid-cols-4 gap-2">
            <a
              href="/oauth2/authorization/google"
              className="p-2.5 bg-white border border-slate-200 hover:border-slate-300 rounded-xl flex items-center justify-center transition shadow-sm hover:shadow"
              title="Sign in with Google"
            >
              <IconGoogle size={18} />
            </a>
            <a
              href="/oauth2/authorization/github"
              className="p-2.5 bg-white border border-slate-200 hover:border-slate-300 rounded-xl flex items-center justify-center transition shadow-sm hover:shadow text-slate-900"
              title="Sign in with GitHub"
            >
              <IconGithub size={18} />
            </a>
            <a
              href="/oauth2/authorization/facebook"
              className="p-2.5 bg-white border border-slate-200 hover:border-slate-300 rounded-xl flex items-center justify-center transition shadow-sm hover:shadow text-blue-600"
              title="Sign in with Facebook"
            >
              <IconFacebook size={18} />
            </a>
            <a
              href="/oauth2/authorization/telegram"
              className="p-2.5 bg-white border border-slate-200 hover:border-slate-300 rounded-xl flex items-center justify-center transition shadow-sm hover:shadow text-sky-500"
              title="Sign in with Telegram"
            >
              <IconTelegram size={18} />
            </a>
          </div>
        </div>

        {/* Sign up link */}
        <div className="mt-6 text-center text-xs text-slate-500">
          {t('dontHaveAccount')}{' '}
          <button
            type="button"
            onClick={() => setAuthView('register')}
            className="text-indigo-600 hover:underline font-semibold"
          >
            {t('signUp')}
          </button>
        </div>
      </div>
    </div>
  );
};
