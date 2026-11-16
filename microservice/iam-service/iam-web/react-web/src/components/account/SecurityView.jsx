import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconLock, IconShieldCheck, IconEye, IconEyeOff, IconCheck, IconX, IconAlertTriangle } from '../common/Icons';
import { Badge } from '../common/Badge';

export const SecurityView = ({ onShowToast }) => {
  const { user, updateProfileState } = useAuth();
  const { t } = useI18n();

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showCurrent, setShowCurrent] = useState(false);
  const [showNew, setShowNew] = useState(false);
  const [loading, setLoading] = useState(false);
  const [mfaLoading, setMfaLoading] = useState(false);

  // Password rules validation
  const hasLength = newPassword.length >= 8;
  const hasUpper = /[A-Z]/.test(newPassword);
  const hasNumber = /[0-9]/.test(newPassword);
  const hasSpecial = /[^A-Za-z0-9]/.test(newPassword);

  const passedRules = [hasLength, hasUpper, hasNumber, hasSpecial].filter(Boolean).length;
  let strengthLabel = t('weak');
  let strengthColor = 'bg-rose-500';
  let strengthPercent = '25%';

  if (passedRules >= 4 && newPassword.length >= 10) {
    strengthLabel = t('strong');
    strengthColor = 'bg-emerald-500';
    strengthPercent = '100%';
  } else if (passedRules >= 3) {
    strengthLabel = t('medium');
    strengthColor = 'bg-amber-500';
    strengthPercent = '65%';
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      onShowToast("New passwords do not match!", 'error');
      return;
    }
    if (!hasLength) {
      onShowToast(t('passReqLength'), 'error');
      return;
    }

    setLoading(true);
    try {
      await api.changePassword(currentPassword, newPassword);
      updateProfileState({ passwordChangedAt: new Date().toISOString() });
      onShowToast(t('passwordChangedSuccess'), 'success');
      setCurrentPassword('');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const toggleMfa = async () => {
    setMfaLoading(true);
    try {
      const nextMfa = !user?.mfaEnabled;
      await api.updateProfile({ mfaEnabled: nextMfa });
      updateProfileState({ mfaEnabled: nextMfa });
      onShowToast(`Two-factor authentication ${nextMfa ? 'enabled' : 'disabled'}!`, 'success');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setMfaLoading(false);
    }
  };

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('securityTitle')}</h2>
          <p className="view-subtitle">{t('securitySubtitle')}</p>
        </div>
      </div>

      <div className="grid-2-cols">
        {/* Left: Password Form */}
        <div className="glass-card p-6">
          <div className="flex items-center gap-2 mb-4 border-b border-slate-800 pb-3">
            <IconLock size={20} className="text-indigo-400" />
            <h3 className="text-lg font-bold text-white">{t('changePassword')}</h3>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="form-group">
              <label className="form-label">{t('currentPassword')}</label>
              <div className="relative">
                <input
                  type={showCurrent ? "text" : "password"}
                  value={currentPassword}
                  onChange={(e) => setCurrentPassword(e.target.value)}
                  className="form-input pr-10"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowCurrent(!showCurrent)}
                  className="absolute right-3 top-3 text-slate-400 hover:text-white"
                >
                  {showCurrent ? <IconEyeOff size={16} /> : <IconEye size={16} />}
                </button>
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">{t('newPassword')}</label>
              <div className="relative">
                <input
                  type={showNew ? "text" : "password"}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="form-input pr-10"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowNew(!showNew)}
                  className="absolute right-3 top-3 text-slate-400 hover:text-white"
                >
                  {showNew ? <IconEyeOff size={16} /> : <IconEye size={16} />}
                </button>
              </div>

              {/* Strength Meter */}
              {newPassword && (
                <div className="mt-2 space-y-1">
                  <div className="flex justify-between text-xs text-slate-400">
                    <span>{t('passwordStrength')}</span>
                    <span className="font-semibold text-slate-200">{strengthLabel}</span>
                  </div>
                  <div className="w-full bg-slate-800 h-1.5 rounded-full overflow-hidden">
                    <div
                      className={`h-full transition-all duration-300 ${strengthColor}`}
                      style={{ width: strengthPercent }}
                    />
                  </div>
                </div>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">{t('confirmNewPassword')}</label>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="form-input"
                required
              />
            </div>

            {/* Rules Checklist */}
            <div className="p-3 bg-slate-950/50 rounded-lg space-y-1.5 text-xs">
              <div className={`flex items-center gap-2 ${hasLength ? 'text-emerald-400' : 'text-slate-400'}`}>
                {hasLength ? <IconCheck size={14} /> : <IconX size={14} />}
                <span>{t('passReqLength')}</span>
              </div>
              <div className={`flex items-center gap-2 ${hasUpper ? 'text-emerald-400' : 'text-slate-400'}`}>
                {hasUpper ? <IconCheck size={14} /> : <IconX size={14} />}
                <span>{t('passReqUpper')}</span>
              </div>
              <div className={`flex items-center gap-2 ${hasNumber ? 'text-emerald-400' : 'text-slate-400'}`}>
                {hasNumber ? <IconCheck size={14} /> : <IconX size={14} />}
                <span>{t('passReqNumber')}</span>
              </div>
              <div className={`flex items-center gap-2 ${hasSpecial ? 'text-emerald-400' : 'text-slate-400'}`}>
                {hasSpecial ? <IconCheck size={14} /> : <IconX size={14} />}
                <span>{t('passReqSpecial')}</span>
              </div>
            </div>

            <div className="pt-2 flex justify-end">
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

        {/* Right: 2FA & Security Posture */}
        <div className="space-y-6">
          {/* 2FA Card */}
          <div className="glass-card p-6">
            <div className="flex items-center justify-between mb-3 border-b border-slate-800 pb-3">
              <div className="flex items-center gap-2">
                <IconShieldCheck size={20} className="text-emerald-400" />
                <h3 className="text-lg font-bold text-white">{t('mfaHeading')}</h3>
              </div>
              <Badge variant={user?.mfaEnabled ? 'success' : 'warning'}>
                {user?.mfaEnabled ? t('enabled') : t('disabled')}
              </Badge>
            </div>

            <p className="text-sm text-slate-300 mb-4">
              {user?.mfaEnabled ? t('mfaEnabled') : t('mfaDisabled')}
            </p>

            <button
              onClick={toggleMfa}
              disabled={mfaLoading}
              className={`btn ${user?.mfaEnabled ? 'btn-secondary text-rose-400' : 'btn-success'}`}
            >
              {user?.mfaEnabled ? t('disableMfa') : t('enableMfa')}
            </button>
          </div>

          {/* Security Advisory */}
          <div className="glass-card p-6">
            <h4 className="text-sm uppercase tracking-wider text-slate-400 font-semibold mb-3">
              Account Security Status
            </h4>
            <div className="space-y-3 text-sm text-slate-300">
              <div className="flex justify-between items-center py-2 border-b border-slate-800/60">
                <span>Last password change:</span>
                <span className="font-mono text-xs text-indigo-300">
                  {user?.passwordChangedAt ? new Date(user.passwordChangedAt).toLocaleDateString() : 'Never'}
                </span>
              </div>
              <div className="flex justify-between items-center py-2 border-b border-slate-800/60">
                <span>Account Lockout Policy:</span>
                <span className="text-emerald-400 text-xs font-semibold">Protected (5 Max Tries)</span>
              </div>
              <div className="flex justify-between items-center py-2">
                <span>Active Credentials:</span>
                <span className="text-xs text-slate-400">OAuth2.1 / Argon2id / OpenID Connect</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
