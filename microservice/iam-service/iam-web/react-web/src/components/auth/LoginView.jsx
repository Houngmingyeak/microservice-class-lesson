import React, { useState, useEffect } from 'react';
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
  IconCheck,
  IconEye,
  IconEyeOff
} from '../common/Icons';

export const LoginView = ({ onShowToast }) => {
  const { login, setAuthView } = useAuth();
  const { locale, setLocale, t } = useI18n();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    if (params.has('error')) {
      const errType = params.get('error');
      if (errType === 'oauth2') {
        onShowToast('Social login provider rejected the request. Use the social buttons below for demo login.', 'warning');
      } else if (errType === 'telegram') {
        onShowToast('Telegram authentication failed. Use the Telegram button for demo login.', 'warning');
      } else {
        onShowToast('Invalid username or password. Please verify your credentials.', 'error');
      }
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    const cleanUsername = username.trim();
    try {
      await login(cleanUsername, password);
      onShowToast(`Welcome back, ${cleanUsername}!`, 'success');
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
        <div className="flex justify-between items-center mb-4">
          <div className="flex items-center gap-3">
            <div className="brand-logo-icon">
              <IconShield size={24} className="text-indigo-600" />
            </div>
            <div>
              <h1 className="text-lg font-bold text-slate-900 tracking-tight">{t('appName')}</h1>
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

        <div className="mb-4">
          <h2 className="text-xl font-bold text-slate-900">{t('signIn')}</h2>
        </div>

        {/* Credentials Form */}
        <form onSubmit={handleSubmit} className="space-y-3">
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
            <div className="relative flex items-center">
              <input
                type={showPassword ? "text" : "password"}
                placeholder="••••••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="form-input pl-9 pr-12 w-full"
                required
              />
              <span className="absolute left-3.5 text-slate-400 pointer-events-none flex items-center" style={{ top: '50%', transform: 'translateY(-50%)' }}>
                <IconLock size={18} />
              </span>
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3.5 p-1.5 text-slate-400 hover:text-indigo-600 rounded-full transition flex items-center justify-center cursor-pointer bg-transparent border-0"
                style={{ top: '50%', transform: 'translateY(-50%)' }}
                title={showPassword ? "Hide password" : "Show password"}
                tabIndex="-1"
              >
                {showPassword ? <IconEyeOff size={20} /> : <IconEye size={20} />}
              </button>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary w-full py-2.5 font-semibold text-sm mt-1"
          >
            {loading ? t('loading') : t('signIn')}
          </button>
        </form>

        {/* Social Logins - Rounded Full */}
        <div className="mt-4 pt-3.5 border-t border-slate-200">
          <span className="text-xs text-slate-500 font-medium block mb-2 text-center">
            {t('orContinueWith')}
          </span>
          <div className="grid grid-cols-4 gap-2.5">
            {/* Google — demo bypass when no real credentials configured */}
            <a
              href="/login/social/google"
              className="py-2.5 bg-white border border-slate-200 hover:border-red-300 rounded-full flex items-center justify-center transition shadow-sm hover:shadow hover:bg-red-50"
              title="Sign in with Google / Gmail"
            >
              <IconGoogle size={18} />
            </a>
            {/* GitHub — demo bypass when no real credentials configured */}
            <a
              href="/login/social/github"
              className="py-2.5 bg-white border border-slate-200 hover:border-slate-400 rounded-full flex items-center justify-center transition shadow-sm hover:shadow hover:bg-slate-100 text-slate-900"
              title="Sign in with GitHub"
            >
              <IconGithub size={18} />
            </a>
            {/* Facebook — demo bypass when no real credentials configured */}
            <a
              href="/login/social/facebook"
              className="py-2.5 bg-white border border-slate-200 hover:border-blue-300 rounded-full flex items-center justify-center transition shadow-sm hover:shadow hover:bg-blue-50 text-blue-600"
              title="Sign in with Facebook"
            >
              <IconFacebook size={18} />
            </a>
            {/* Telegram — demo bypass when no real credentials configured */}
            <a
              href="/login/social/telegram"
              className="py-2.5 bg-white border border-slate-200 hover:border-sky-300 rounded-full flex items-center justify-center transition shadow-sm hover:shadow hover:bg-sky-50 text-sky-500"
              title="Sign in with Telegram"
            >
              <IconTelegram size={18} />
            </a>
          </div>

          {/* Quick Demo Fill Helper */}
          <div className="mt-3 text-center space-y-1">
            <p className="text-xs text-slate-400">Social icons use demo login when no credentials are set</p>
            <button
              type="button"
              onClick={() => {
                setUsername('houngmingyeak@gmail.com');
                setPassword('supersecret');
              }}
              className="text-xs text-indigo-600 hover:text-indigo-800 transition underline cursor-pointer bg-transparent border-0 font-medium"
            >
              Fill Demo Admin (houngmingyeak@gmail.com / supersecret)
            </button>
          </div>
        </div>

        {/* Sign up link */}
        <div className="mt-4 text-center text-xs text-slate-500">
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
