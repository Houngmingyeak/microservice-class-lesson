import React, { useState, useEffect } from 'react';
import { useI18n } from '../../context/I18nContext';
import { api } from '../../services/api';
import { Badge } from '../common/Badge';
import { Modal } from '../common/Modal';
import { IconFileText, IconSearch, IconCheck, IconX, IconClock, IconSliders } from '../common/Icons';

export const AuditLogsView = ({ onShowToast }) => {
  const { t } = useI18n();
  const [logs, setLogs] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [eventFilter, setEventFilter] = useState('ALL');
  const [selectedLog, setSelectedLog] = useState(null);

  useEffect(() => {
    loadLogs();
  }, []);

  const loadLogs = async () => {
    const list = await api.getAuditLogs();
    setLogs(list);
  };

  const handleExportCSV = () => {
    const header = "Timestamp,Actor,Event,Target,IPAddress,Status,Details\n";
    const rows = logs.map(l =>
      `"${l.timestamp}","${l.actor}","${l.event}","${l.target}","${l.ipAddress}","${l.status}","${l.details?.replace(/"/g, '""')}"`
    ).join("\n");

    const blob = new Blob([header + rows], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", `iam_audit_logs_${new Date().toISOString().slice(0, 10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    onShowToast("Audit logs exported to CSV!", 'success');
  };

  const eventTypes = Array.from(new Set(logs.map(l => l.event)));

  const filteredLogs = logs.filter(l => {
    const term = searchTerm.toLowerCase();
    const matchesSearch =
      l.actor.toLowerCase().includes(term) ||
      l.event.toLowerCase().includes(term) ||
      l.target.toLowerCase().includes(term) ||
      l.details?.toLowerCase().includes(term) ||
      l.ipAddress?.includes(term);

    const matchesEvent = eventFilter === 'ALL' || l.event === eventFilter;
    return matchesSearch && matchesEvent;
  });

  return (
    <div className="view-container">
      <div className="view-header flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="view-title">{t('auditTitle')}</h2>
          <p className="view-subtitle">{t('auditSubtitle')}</p>
        </div>
        <button
          onClick={handleExportCSV}
          className="btn btn-secondary flex items-center gap-2 self-start sm:self-auto"
        >
          <IconFileText size={15} />
          <span>{t('exportLogs')}</span>
        </button>
      </div>

      <div className="glass-card p-6">
        <div className="flex flex-col sm:flex-row gap-3 mb-6 justify-between">
          <div className="relative w-full sm:w-80">
            <input
              type="text"
              placeholder={t('search')}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="form-input pl-9"
            />
            <span className="absolute left-3 top-2.5 text-slate-400">
              <IconSearch size={16} />
            </span>
          </div>

          <div className="flex gap-2">
            <select
              value={eventFilter}
              onChange={(e) => setEventFilter(e.target.value)}
              className="form-input text-xs w-auto py-1.5"
            >
              <option value="ALL">{t('all')} Events</option>
              {eventTypes.map(ev => (
                <option key={ev} value={ev}>{ev}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>{t('time')}</th>
                <th>{t('actor')}</th>
                <th>{t('event')}</th>
                <th>{t('target')}</th>
                <th>IP Address</th>
                <th>{t('status')}</th>
                <th className="text-right">Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredLogs.map((log) => (
                <tr key={log.id} className="cursor-pointer hover:bg-indigo-50/40 transition" onClick={() => setSelectedLog(log)}>
                  <td className="font-mono text-xs text-slate-600">
                    {new Date(log.timestamp).toLocaleString()}
                  </td>
                  <td>
                    <span className="font-bold text-indigo-700 text-xs">@{log.actor}</span>
                  </td>
                  <td>
                    <span className="font-mono text-xs font-semibold text-slate-800 bg-slate-100 px-2.5 py-1 rounded border border-slate-200">
                      {log.event}
                    </span>
                  </td>
                  <td className="text-xs text-slate-700 font-medium">{log.target}</td>
                  <td className="font-mono text-xs text-slate-500">{log.ipAddress}</td>
                  <td>
                    <Badge variant={log.status === 'SUCCESS' ? 'success' : 'danger'} size="sm">
                      {log.status}
                    </Badge>
                  </td>
                  <td className="text-right">
                    <button
                      onClick={(e) => { e.stopPropagation(); setSelectedLog(log); }}
                      className="text-xs font-semibold text-indigo-600 hover:text-indigo-800 hover:underline"
                    >
                      {t('viewDetails')}
                    </button>
                  </td>
                </tr>
              ))}
              {filteredLogs.length === 0 && (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-slate-500">
                    {t('noData')}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Audit Detail Modal */}
      <Modal
        isOpen={!!selectedLog}
        onClose={() => setSelectedLog(null)}
        title="Audit Event Details"
      >
        {selectedLog && (
          <div className="space-y-4 text-sm">
            <div className="grid grid-cols-2 gap-2.5 p-4 bg-slate-50 rounded-xl border border-slate-200 text-xs font-mono text-slate-800">
              <div><span className="text-slate-500 font-semibold">ID:</span> {selectedLog.id}</div>
              <div><span className="text-slate-500 font-semibold">Status:</span> {selectedLog.status}</div>
              <div><span className="text-slate-500 font-semibold">Event:</span> {selectedLog.event}</div>
              <div><span className="text-slate-500 font-semibold">Actor:</span> {selectedLog.actor}</div>
              <div><span className="text-slate-500 font-semibold">Target:</span> {selectedLog.target}</div>
              <div><span className="text-slate-500 font-semibold">IP:</span> {selectedLog.ipAddress}</div>
              <div className="col-span-2"><span className="text-slate-500 font-semibold">Timestamp:</span> {selectedLog.timestamp}</div>
            </div>

            <div>
              <span className="text-xs uppercase tracking-wider text-slate-500 font-bold block mb-1.5">
                Payload / Change Description
              </span>
              <p className="text-slate-700 bg-slate-50 p-3.5 rounded-xl border border-slate-200 leading-relaxed font-mono text-xs">
                {selectedLog.details}
              </p>
            </div>

            <div className="flex justify-end pt-2">
              <button onClick={() => setSelectedLog(null)} className="btn btn-secondary">
                {t('close')}
              </button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};
