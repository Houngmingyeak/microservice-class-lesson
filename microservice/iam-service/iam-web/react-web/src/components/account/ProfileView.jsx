import React, { useState, useEffect, useRef } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { IconUser, IconCheck, IconEdit, IconShieldCheck, IconGlobe, IconCamera, IconTrash, IconUpload } from '../common/Icons';
import { Badge } from '../common/Badge';

export const ProfileView = ({ onShowToast }) => {
  const { user, updateProfileState } = useAuth();
  const { t } = useI18n();
  const fileInputRef = useRef(null);

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
        pictureUrl: user.pictureUrl || ''
      });
    }
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleFileSelect = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    if (file.size > 5 * 1024 * 1024) {
      onShowToast("Image size must be less than 5MB", 'error');
      return;
    }

    try {
      // 1. Try uploading the file directly to server backend
      const result = await api.uploadAvatar(file);
      if (result && result.pictureUrl) {
        setFormData(prev => ({ ...prev, pictureUrl: result.pictureUrl }));
        updateProfileState({ pictureUrl: result.pictureUrl });
        onShowToast("Profile picture uploaded and saved successfully!", 'success');
        return;
      }
    } catch (err) {
      // fallback to base64
    }

    // 2. Fallback to base64 encoding
    const reader = new FileReader();
    reader.onload = async () => {
      const base64Data = reader.result;
      setFormData(prev => ({ ...prev, pictureUrl: base64Data }));
      try {
        const updated = await api.updateProfile({ pictureUrl: base64Data });
        updateProfileState(updated);
        onShowToast("Profile picture uploaded successfully!", 'success');
      } catch (err) {
        onShowToast("Failed to save picture", 'error');
      }
    };
    reader.readAsDataURL(file);
  };

  const handleRemovePhoto = async () => {
    setFormData(prev => ({ ...prev, pictureUrl: '' }));
    try {
      const updated = await api.updateProfile({ pictureUrl: '' });
      updateProfileState(updated);
      onShowToast("Profile picture removed", 'info');
    } catch (err) {
      onShowToast(err.message, 'error');
    }
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

  return (
    <div className="view-container">
      <div className="view-header">
        <div>
          <h2 className="view-title">{t('profileTitle')}</h2>
          <p className="view-subtitle">{t('profileSubtitle')}</p>
        </div>
      </div>

      <div className="grid-2-cols">
        {/* Left Card: Summary & Uploadable Avatar */}
        <div className="glass-card flex-col items-center text-center p-6">
          <input
            type="file"
            ref={fileInputRef}
            onChange={handleFileSelect}
            accept="image/*"
            className="hidden"
          />

          {formData.pictureUrl ? (
            <div className="relative group mx-auto w-28 h-28">
              <img
                src={formData.pictureUrl}
                alt="Profile Avatar"
                className="w-28 h-28 rounded-full object-cover border-4 border-indigo-100 shadow"
              />
              <button
                type="button"
                onClick={handleRemovePhoto}
                className="absolute -top-1 -right-1 p-1.5 bg-rose-500 text-white rounded-full hover:bg-rose-600 transition shadow"
                title="Remove photo"
              >
                <IconTrash size={14} />
              </button>
            </div>
          ) : (
            <div
              onClick={() => fileInputRef.current?.click()}
              className="w-28 h-28 rounded-full bg-slate-100 border-2 border-dashed border-slate-300 text-slate-400 hover:border-indigo-400 hover:text-indigo-600 transition cursor-pointer flex flex-col items-center justify-center mx-auto group shadow-inner"
              title="Click to upload profile photo"
            >
              <IconUser size={36} className="text-slate-400 group-hover:text-indigo-500 transition" />
              <span className="text-[10px] text-slate-400 group-hover:text-indigo-600 font-medium mt-1">No Picture</span>
            </div>
          )}

          <div className="mt-3 flex justify-center gap-2">
            <button
              type="button"
              onClick={() => fileInputRef.current?.click()}
              className="btn btn-secondary text-xs py-1.5 px-3 flex items-center gap-1.5 text-slate-700"
            >
              <IconCamera size={14} />
              <span>{formData.pictureUrl ? "Change Photo" : "Upload Photo"}</span>
            </button>
          </div>

          <h3 className="mt-3 text-base font-bold text-slate-900">
            {formData.givenName} {formData.familyName}
          </h3>
          <p className="text-slate-500 text-xs">@{formData.username}</p>

          <div className="mt-2.5 flex flex-wrap gap-1 justify-center">
            {user?.roles?.map(r => (
              <Badge key={r} variant="primary">{r}</Badge>
            ))}
          </div>

          {/* OIDC Claims preview */}
          <div className="mt-5 w-full border-t border-slate-200/80 pt-3 text-left">
            <span className="text-[11px] uppercase tracking-wider text-slate-500 font-semibold mb-2 flex items-center gap-1.5">
              <IconShieldCheck size={14} className="text-emerald-500" />
              OIDC Token Claims
            </span>
            <div className="bg-slate-50 border border-slate-200/80 p-3 rounded-xl font-mono text-xs text-slate-700 space-y-1 overflow-x-auto">
              <div><span className="text-indigo-600 font-semibold">email_verified:</span> true</div>
              <div><span className="text-indigo-600 font-semibold">zoneinfo:</span> Asia/Phnom_Penh</div>
              <div><span className="text-indigo-600 font-semibold">auth_time:</span> {new Date().toLocaleTimeString()}</div>
            </div>
          </div>
        </div>

        {/* Right Card: Edit Form */}
        <div className="glass-card p-5 md:p-6">
          <form onSubmit={handleSubmit} className="space-y-3.5">
            <h4 className="text-sm font-bold text-slate-900 border-b border-slate-200/80 pb-2.5 mb-4">
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
                  className="form-input bg-slate-100 text-slate-500 cursor-not-allowed"
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
