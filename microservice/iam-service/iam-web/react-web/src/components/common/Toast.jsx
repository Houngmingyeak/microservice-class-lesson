import React from 'react';
import { IconCheck, IconAlertTriangle, IconX } from './Icons';

export const Toast = ({ message, type = 'success', onClose }) => {
  if (!message) return null;

  const isSuccess = type === 'success';

  return (
    <div className={`toast-container ${isSuccess ? 'toast-success' : 'toast-error'}`}>
      <div className="toast-icon">
        {isSuccess ? <IconCheck size={18} /> : <IconAlertTriangle size={18} />}
      </div>
      <div className="toast-message">{message}</div>
      {onClose && (
        <button onClick={onClose} className="toast-close">
          <IconX size={14} />
        </button>
      )}
    </div>
  );
};
