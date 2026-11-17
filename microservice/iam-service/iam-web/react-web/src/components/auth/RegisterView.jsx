import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import { IconShield, IconLock, IconUser, IconGlobe, IconCheck, IconX, IconEye, IconEyeOff } from '../common/Icons';

export const RegisterView = ({ onShowToast }) => {
  const { register, setAuthView } = useAuth();
  const { locale, setLocale, t } = useI18n();

  const [formData, setFormData] = useState({
    username: '',
    email: '',
    givenName: '',
    familyName: '',
    password: '',
    confirmPassword: ''
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const passwordsMatch = formData.password && formData.password === formData.confirmPassword;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!passwordsMatch) {
      onShowToast("Passwords do not match", 'error');
      return;
    }
    if (formData.password.length < 8) {
      onShowToast("Password must be at least 8 characters", 'error');
      return;
    }

    setLoading(true);
    try {
      await register(formData);
      onShowToast("Account created successfully! Welcome to ISTAD IAM.", 'success');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page-root">
      <div className="auth-card-container max-w-lg">
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
            onClick={() => setLocale(locale === 'en' ? 'km' : 'en')}
            className="lang-toggle-btn text-xs py-1 px-2.5"
            title="Toggle Language"
          >
            <IconGlobe size={14} />
            <span>{locale === 'en' ? 'ខ្មែរ' : 'EN'}</span>
          </button>
        </div>

        <div className="mb-6">
          <h2 className="text-xl font-bold text-slate-900 mb-1">{t('register.title') || "Create an account"}</h2>
          <p className="text-sm text-slate-500">Join the enterprise identity network</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('givenName')}</label>
              <input
                type="text"
                name="givenName"
                value={formData.givenName}
                onChange={handleChange}
                className="form-input"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('familyName')}</label>
              <input
                type="text"
                name="familyName"
                value={formData.familyName}
                onChange={handleChange}
                className="form-input"
                required
              />
            </div>
          </div>

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('username')}</label>
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                className="form-input"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">{t('email')}</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="form-input"
                required
              />
            </div>
          </div>

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">{t('password')}</label>
              <div className="relative flex items-center">
                <input
                  type={showPassword ? "text" : "password"}
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  className="form-input pr-11 w-full"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 p-1 text-slate-400 hover:text-indigo-600 rounded-full transition flex items-center justify-center cursor-pointer bg-transparent border-0"
                  style={{ top: '50%', transform: 'translateY(-50%)' }}
                  title={showPassword ? "Hide password" : "Show password"}
                  tabIndex="-1"
                >
                  {showPassword ? <IconEyeOff size={18} /> : <IconEye size={18} />}
                </button>
              </div>
            </div>
            <div className="form-group">
              <label className="form-label">{t('confirmNewPassword')}</label>
              <div className="relative flex items-center">
                <input
                  type={showConfirmPassword ? "text" : "password"}
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  className="form-input pr-11 w-full"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute right-3 p-1 text-slate-400 hover:text-indigo-600 rounded-full transition flex items-center justify-center cursor-pointer bg-transparent border-0"
                  style={{ top: '50%', transform: 'translateY(-50%)' }}
                  title={showConfirmPassword ? "Hide password" : "Show password"}
                  tabIndex="-1"
                >
                  {showConfirmPassword ? <IconEyeOff size={18} /> : <IconEye size={18} />}
                </button>
              </div>
            </div>
          </div>

          {formData.confirmPassword && (
            <div className={`text-xs flex items-center gap-1.5 ${passwordsMatch ? 'text-emerald-600' : 'text-rose-600'}`}>
              {passwordsMatch ? <IconCheck size={14} /> : <IconX size={14} />}
              <span>{passwordsMatch ? "Passwords match" : "Passwords do not match"}</span>
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary w-full py-2.5 font-semibold text-sm mt-3"
          >
            {loading ? t('loading') : t('signUp')}
          </button>
        </form>

        <div className="mt-6 text-center text-xs text-slate-500">
          {t('alreadyHaveAccount')}{' '}
          <button
            type="button"
            onClick={() => setAuthView('login')}
            className="text-indigo-600 hover:underline font-semibold"
          >
            {t('signIn')}
          </button>
        </div>
      </div>
    </div>
  );
};
