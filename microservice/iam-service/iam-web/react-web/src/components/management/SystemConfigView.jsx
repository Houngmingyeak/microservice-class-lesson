import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { initialSystemConfig } from '../../services/mockData';
import { IconSettings, IconShield, IconLock, IconClock, IconRefresh, IconCheck } from '../common/Icons';

export const SystemConfigView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [config, setConfig] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadConfig();
  }, []);

  const loadConfig = async () => {
    const data = await api.getSystemConfig();
    setConfig(data || initialSystemConfig);
  };

  const handleChange = (key, value) => {
    setConfig(prev => ({ ...prev, [key]: value }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.updateSystemConfig(config);
      onShowToast(t('configSaved'), 'success');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleResetDefaults = () => {
    setConfig(initialSystemConfig);
    onShowToast("Policies reset to organizational defaults", 'success');
  };

  if (!config) return null;

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('sysConfigTitle')}</h2>
          <p className="view-subtitle">{t('sysConfigSubtitle')}</p>
        </div>
        <div className="flex gap-2">
          <button
            type="button"
            onClick={handleResetDefaults}
            className="btn btn-secondary flex items-center gap-1.5"
          >
            <IconRefresh size={14} />
            <span>{t('resetDefaults')}</span>
          </button>
        </div>
      </div>

      <form onSubmit={handleSave} className="space-y-6">
        {/* Section 1: Authentication & Lockout Policies */}
        <div className="glass-card p-6">
          <div className="flex items-center gap-2 mb-4 border-b border-slate-100 pb-3">
            <IconShield size={20} className="text-indigo-600" />
            <h3 className="text-base font-bold text-slate-900">{t('authPolicy')}</h3>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="form-group">
              <label className="form-label">{t('maxFailedAttempts')}</label>
              <input
                type="number"
                min="1"
                max="20"
                value={config.maxFailedLoginAttempts}
                onChange={(e) => handleChange('maxFailedLoginAttempts', parseInt(e.target.value) || 5)}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('lockoutDuration')}</label>
              <input
                type="number"
                min="1"
                max="1440"
                value={config.lockoutDurationMinutes}
                onChange={(e) => handleChange('lockoutDurationMinutes', parseInt(e.target.value) || 30)}
                className="form-input"
              />
            </div>

            <div className="md:col-span-2 pt-2">
              <label className="flex items-center gap-3 text-sm text-slate-700 cursor-pointer font-medium">
                <input
                  type="checkbox"
                  checked={config.require2faForAdmins}
                  onChange={(e) => handleChange('require2faForAdmins', e.target.checked)}
                  className="rounded border-slate-300 text-indigo-600 focus:ring-0"
                />
                <span>{t('require2faAdmin')}</span>
              </label>
            </div>
          </div>
        </div>

        {/* Section 2: Password Complexity Policies */}
        <div className="glass-card p-6">
          <div className="flex items-center gap-2 mb-4 border-b border-slate-100 pb-3">
            <IconLock size={20} className="text-emerald-600" />
            <h3 className="text-base font-bold text-slate-900">{t('passPolicy')}</h3>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="form-group">
              <label className="form-label">{t('minPassLength')}</label>
              <input
                type="number"
                min="6"
                max="32"
                value={config.minPasswordLength}
                onChange={(e) => handleChange('minPasswordLength', parseInt(e.target.value) || 8)}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('passExpirationDays')}</label>
              <input
                type="number"
                min="0"
                max="365"
                value={config.passwordExpirationDays}
                onChange={(e) => handleChange('passwordExpirationDays', parseInt(e.target.value) || 90)}
                className="form-input"
              />
            </div>

            <div className="md:col-span-2 space-y-2.5 pt-2">
              <label className="flex items-center gap-3 text-sm text-slate-700 cursor-pointer font-medium">
                <input
                  type="checkbox"
                  checked={config.requireUppercase}
                  onChange={(e) => handleChange('requireUppercase', e.target.checked)}
                  className="rounded border-slate-300 text-indigo-600 focus:ring-0"
                />
                <span>Must contain at least one uppercase letter (A-Z)</span>
              </label>
              <label className="flex items-center gap-3 text-sm text-slate-700 cursor-pointer font-medium">
                <input
                  type="checkbox"
                  checked={config.requireNumbers}
                  onChange={(e) => handleChange('requireNumbers', e.target.checked)}
                  className="rounded border-slate-300 text-indigo-600 focus:ring-0"
                />
                <span>Must contain at least one numerical digit (0-9)</span>
              </label>
              <label className="flex items-center gap-3 text-sm text-slate-700 cursor-pointer font-medium">
                <input
                  type="checkbox"
                  checked={config.requireSpecialChars}
                  onChange={(e) => handleChange('requireSpecialChars', e.target.checked)}
                  className="rounded border-slate-300 text-indigo-600 focus:ring-0"
                />
                <span>Must contain at least one special character (!@#$%^&*)</span>
              </label>
            </div>
          </div>
        </div>

        {/* Section 3: Session Expiration & Token Lifetimes */}
        <div className="glass-card p-6">
          <div className="flex items-center gap-2 mb-4 border-b border-slate-100 pb-3">
            <IconClock size={20} className="text-purple-600" />
            <h3 className="text-base font-bold text-slate-900">{t('sessionPolicy')}</h3>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="form-group">
              <label className="form-label">{t('sessionTimeout')}</label>
              <input
                type="number"
                min="5"
                max="1440"
                value={config.sessionTimeoutMinutes}
                onChange={(e) => handleChange('sessionTimeoutMinutes', parseInt(e.target.value) || 60)}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('accessTokenTtl')}</label>
              <input
                type="number"
                min="60"
                max="86400"
                value={config.accessTokenTtlSeconds}
                onChange={(e) => handleChange('accessTokenTtlSeconds', parseInt(e.target.value) || 3600)}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label className="form-label">{t('refreshTokenTtl')}</label>
              <input
                type="number"
                min="1"
                max="8760"
                value={config.refreshTokenTtlHours}
                onChange={(e) => handleChange('refreshTokenTtlHours', parseInt(e.target.value) || 720)}
                className="form-input"
              />
            </div>
          </div>
        </div>

        {/* Submit */}
        <div className="flex justify-end gap-3 pt-2">
          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary"
          >
            {loading ? t('loading') : t('save')}
          </button>
        </div>
      </form>
    </div>
  );
};
