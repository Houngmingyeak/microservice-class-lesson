import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconUser, IconCheck, IconEdit, IconShieldCheck, IconGlobe } from '../common/Icons';
import { Badge } from '../common/Badge';

export const ProfileView = ({ onShowToast }) => {
  const { user, updateProfileState } = useAuth();
  const { t } = useI18n();

  const [formData, setFormData] = useState({
    username: '',
    email: '',
    givenName: '',
    familyName: '',
    nickname: '',
    phoneNumber: '',
    gender: 'MALE',
    birthdate: '2000-01-01',
    addressStreetAddress: '',
    addressLocality: '',
    addressCountry: 'KH',
    locale: 'en',
    pictureUrl: ''
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      setFormData({
        username: user.username || '',
        email: user.email || '',
        givenName: user.givenName || '',
        familyName: user.familyName || '',
        nickname: user.nickname || '',
        phoneNumber: user.phoneNumber || '',
        gender: user.gender || 'MALE',
        birthdate: user.birthdate || '2000-01-01',
        addressStreetAddress: user.addressStreetAddress || '',
        addressLocality: user.addressLocality || '',
        addressCountry: user.addressCountry || 'KH',
        locale: user.locale || 'en',
        pictureUrl: user.pictureUrl || user.avatar || ''
      });
    }
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleAvatarChange = (avatarUrl) => {
    setFormData(prev => ({ ...prev, pictureUrl: avatarUrl }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const updated = await api.updateProfile(formData);
      updateProfileState(updated);
      onShowToast(t('profileUpdated'), 'success');
    } catch (err) {
      onShowToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const avatarOptions = [
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=256",
    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=256",
    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=256",
    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=256",
    "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=256"
  ];

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('profileTitle')}</h2>
          <p className="view-subtitle">{t('profileSubtitle')}</p>
        </div>
      </div>

      <div className="grid-2-cols">
        {/* Left Card: Summary & Avatar */}
        <div className="glass-card flex-col items-center text-center p-6">
          <div className="relative group">
            <img
              src={formData.pictureUrl || "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=256"}
              alt="Profile Avatar"
              className="w-32 h-32 rounded-full object-cover border-4 border-indigo-500/40 shadow-xl"
            />
          </div>

          <h3 className="mt-4 text-xl font-bold text-white">
            {formData.givenName} {formData.familyName}
          </h3>
          <p className="text-slate-400 text-sm">@{formData.username}</p>

          <div className="mt-3 flex flex-wrap gap-1.5 justify-center">
            {user?.roles?.map(r => (
              <Badge key={r} variant="primary">{r}</Badge>
            ))}
          </div>

          <div className="mt-6 w-full border-t border-slate-800 pt-5 text-left">
            <span className="text-xs uppercase tracking-wider text-slate-400 font-semibold mb-3 block">
              {t('changeAvatar')}
            </span>
            <div className="flex gap-2 justify-center">
              {avatarOptions.map((opt, i) => (
                <button
                  key={i}
                  type="button"
                  onClick={() => handleAvatarChange(opt)}
                  className={`avatar-choice-btn ${formData.pictureUrl === opt ? 'ring-2 ring-indigo-500' : ''}`}
                >
                  <img src={opt} alt="Choice" className="w-10 h-10 rounded-full object-cover" />
                </button>
              ))}
            </div>
          </div>

          {/* OIDC Claims preview */}
          <div className="mt-6 w-full border-t border-slate-800 pt-4 text-left">
            <span className="text-xs uppercase tracking-wider text-slate-400 font-semibold mb-2 block flex items-center gap-1.5">
              <IconShieldCheck size={14} className="text-emerald-400" />
              OIDC Token Claims
            </span>
            <div className="bg-slate-950/70 p-3 rounded-lg text-xs font-mono text-slate-300 space-y-1 overflow-x-auto">
              <div><span className="text-indigo-400">sub:</span> {user?.uuid}</div>
              <div><span className="text-indigo-400">email_verified:</span> true</div>
              <div><span className="text-indigo-400">zoneinfo:</span> Asia/Phnom_Penh</div>
              <div><span className="text-indigo-400">auth_time:</span> {new Date().toLocaleTimeString()}</div>
            </div>
          </div>
        </div>

        {/* Right Card: Edit Form */}
        <div className="glass-card p-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <h4 className="text-base font-semibold text-white border-b border-slate-800 pb-2 mb-4">
              {t('personalDetails')}
            </h4>

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
                  className="form-input bg-slate-950/40 text-slate-400 cursor-not-allowed"
                  disabled
                />
              </div>

              <div className="form-group">
                <label className="form-label">{t('nickname')}</label>
                <input
                  type="text"
                  name="nickname"
                  value={formData.nickname}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>
            </div>

            <div className="form-grid-2">
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

              <div className="form-group">
                <label className="form-label">{t('phoneNumber')}</label>
                <input
                  type="tel"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>
            </div>

            <div className="form-grid-2">
              <div className="form-group">
                <label className="form-label">{t('gender')}</label>
                <select
                  name="gender"
                  value={formData.gender}
                  onChange={handleChange}
                  className="form-input"
                >
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">{t('birthdate')}</label>
                <input
                  type="date"
                  name="birthdate"
                  value={formData.birthdate}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>
            </div>

            <div className="form-grid-2">
              <div className="form-group">
                <label className="form-label">{t('address')}</label>
                <input
                  type="text"
                  name="addressStreetAddress"
                  value={formData.addressStreetAddress}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>

              <div className="form-group">
                <label className="form-label">{t('locality')}</label>
                <input
                  type="text"
                  name="addressLocality"
                  value={formData.addressLocality}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">{t('preferredLocale')}</label>
              <select
                name="locale"
                value={formData.locale}
                onChange={handleChange}
                className="form-input"
              >
                <option value="en">English (en)</option>
                <option value="km">ខ្មែរ / Khmer (km)</option>
              </select>
            </div>

            <div className="pt-4 flex justify-end">
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
      </div>
    </div>
  );
};
