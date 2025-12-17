import React, { useState, useEffect } from 'react';
import Login from './Login';

// ============================================
// MINI-LMS - Système de Gestion Éducatif
// Frontend React avec Sidebar Toggle
// ============================================

// Configuration API
const API_BASE = 'http://localhost:8080/api';

// Fonction fetch générique
const fetchAPI = async (endpoint, options = {}) => {
  try {
    const response = await fetch(API_BASE + endpoint, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });
    if (!response.ok) {
      // Créer une erreur avec la réponse pour pouvoir accéder aux détails
      const error = new Error('HTTP error! status: ' + response.status);
      error.response = response;
      throw error;
    }
    if (response.status === 204) return null;
    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};

// ============================================
// ICÔNES SVG
// ============================================
const Icons = {
  Menu: () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="3" y1="6" x2="21" y2="6"></line>
      <line x1="3" y1="12" x2="21" y2="12"></line>
      <line x1="3" y1="18" x2="21" y2="18"></line>
    </svg>
  ),
  GraduationCap: () => (
    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
      <path d="M6 12v5c3 3 9 3 12 0v-5"/>
    </svg>
  ),
  Users: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
      <circle cx="9" cy="7" r="4"/>
      <path d="M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>
    </svg>
  ),
  BookOpen: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/>
      <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>
    </svg>
  ),
  ClipboardList: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="8" y="2" width="8" height="4" rx="1" ry="1"/>
      <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/>
      <path d="M12 11h4M12 16h4M8 11h.01M8 16h.01"/>
    </svg>
  ),
  Award: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="8" r="6"/>
      <path d="M15.477 12.89 17 22l-5-3-5 3 1.523-9.11"/>
    </svg>
  ),
  TrendingUp: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
      <polyline points="17 6 23 6 23 12"/>
    </svg>
  ),
  Plus: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="12" y1="5" x2="12" y2="19"/>
      <line x1="5" y1="12" x2="19" y2="12"/>
    </svg>
  ),
  Edit: () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
      <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
    </svg>
  ),
  Trash2: () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="3 6 5 6 21 6"/>
      <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
      <line x1="10" y1="11" x2="10" y2="17"/>
      <line x1="14" y1="11" x2="14" y2="17"/>
    </svg>
  ),
  X: () => (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="18" y1="6" x2="6" y2="18"/>
      <line x1="6" y1="6" x2="18" y2="18"/>
    </svg>
  ),
  RefreshCw: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="23 4 23 10 17 10"/>
      <polyline points="1 20 1 14 7 14"/>
      <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"/>
    </svg>
  ),
};

// ============================================
// COMPOSANT MODAL
// ============================================
const Modal = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null;

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(0, 0, 0, 0.6)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 2000,
      backdropFilter: 'blur(4px)',
    }}>
      <div style={{
        backgroundColor: 'white',
        borderRadius: '16px',
        padding: '0',
        width: '90%',
        maxWidth: '500px',
        maxHeight: '90vh',
        overflow: 'hidden',
        boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
      }}>
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          padding: '20px 24px',
          borderBottom: '1px solid #e2e8f0',
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          color: 'white',
        }}>
          <h2 style={{ margin: 0, fontSize: '1.25rem', fontWeight: '600' }}>{title}</h2>
          <button
            onClick={onClose}
            style={{
              background: 'rgba(255,255,255,0.2)',
              border: 'none',
              cursor: 'pointer',
              padding: '8px',
              borderRadius: '8px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'white',
            }}
          >
            <Icons.X />
          </button>
        </div>
        <div style={{ padding: '24px', overflowY: 'auto', maxHeight: 'calc(90vh - 140px)' }}>
          {children}
        </div>
      </div>
    </div>
  );
};

// ============================================
// PAGE DASHBOARD
// ============================================
const Dashboard = ({ students, courses, enrollments, grades }) => {
  const totalStudents = students.length;
  const totalCourses = courses.length;
  const totalEnrollments = enrollments.length;
  const averageGrade = grades.length > 0
    ? (grades.reduce((sum, g) => sum + (g.value || 0), 0) / grades.length).toFixed(2)
    : '0.00';

  const statCards = [
    { title: 'Étudiants', value: totalStudents, subtitle: 'Inscrits au système', color: '#3b82f6', icon: Icons.Users },
    { title: 'Cours', value: totalCourses, subtitle: 'Disponibles', color: '#10b981', icon: Icons.BookOpen },
    { title: 'Inscriptions', value: totalEnrollments, subtitle: 'Actives', color: '#f59e0b', icon: Icons.ClipboardList },
    { title: 'Moyenne Générale', value: averageGrade + '/20', subtitle: grades.length + ' notes attribuées', color: '#8b5cf6', icon: Icons.Award },
  ];

  return (
    <div>
      <div style={{ marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2rem', fontWeight: '700', color: '#1e293b', marginBottom: '8px' }}>
          Tableau de bord
        </h1>
        <p style={{ color: '#64748b', fontSize: '1rem' }}>Vue d'ensemble du système éducatif</p>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
        gap: '24px',
        marginBottom: '40px',
      }}>
        {statCards.map((card, index) => (
          <div key={index} style={{
            background: 'linear-gradient(135deg, ' + card.color + ' 0%, ' + card.color + 'dd 100%)',
            borderRadius: '16px',
            padding: '24px',
            color: 'white',
            boxShadow: '0 10px 40px ' + card.color + '40',
          }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <p style={{ fontSize: '0.875rem', opacity: 0.9, marginBottom: '8px', fontWeight: '500' }}>{card.title}</p>
                <p style={{ fontSize: '2.5rem', fontWeight: '700', marginBottom: '4px' }}>{card.value}</p>
                <p style={{ fontSize: '0.8rem', opacity: 0.8 }}>{card.subtitle}</p>
              </div>
              <div style={{
                backgroundColor: 'rgba(255,255,255,0.2)',
                borderRadius: '12px',
                padding: '12px',
              }}>
                <card.icon />
              </div>
            </div>
          </div>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(350px, 1fr))', gap: '24px' }}>
        <div style={{
          backgroundColor: 'white',
          borderRadius: '16px',
          padding: '24px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        }}>
          <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '20px', color: '#1e293b' }}>
            Derniers étudiants
          </h3>
          {students.slice(0, 5).map((student, index) => (
            <div key={student.id || index} style={{
              display: 'flex',
              alignItems: 'center',
              padding: '12px 0',
              borderBottom: index < 4 ? '1px solid #f1f5f9' : 'none',
            }}>
              <div style={{
                width: '40px',
                height: '40px',
                borderRadius: '10px',
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: 'white',
                fontWeight: '600',
                marginRight: '12px',
                fontSize: '0.875rem',
              }}>
                {(student.firstName || '?')[0]}{(student.lastName || '?')[0]}
              </div>
              <div>
                <p style={{ fontWeight: '500', color: '#1e293b', marginBottom: '2px' }}>
                  {student.firstName} {student.lastName}
                </p>
                <p style={{ fontSize: '0.8rem', color: '#64748b' }}>{student.email}</p>
              </div>
            </div>
          ))}
          {students.length === 0 && (
            <p style={{ color: '#94a3b8', textAlign: 'center', padding: '20px' }}>Aucun étudiant</p>
          )}
        </div>

        <div style={{
          backgroundColor: 'white',
          borderRadius: '16px',
          padding: '24px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
        }}>
          <h3 style={{ fontSize: '1.125rem', fontWeight: '600', marginBottom: '20px', color: '#1e293b' }}>
            Cours disponibles
          </h3>
          {courses.slice(0, 5).map((course, index) => (
            <div key={course.id || index} style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '12px 0',
              borderBottom: index < 4 ? '1px solid #f1f5f9' : 'none',
            }}>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <div style={{
                  width: '40px',
                  height: '40px',
                  borderRadius: '10px',
                  background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: 'white',
                  marginRight: '12px',
                }}>
                  <Icons.BookOpen />
                </div>
                <div>
                  <p style={{ fontWeight: '500', color: '#1e293b', marginBottom: '2px' }}>{course.title}</p>
                  <p style={{ fontSize: '0.8rem', color: '#64748b' }}>{course.professorName || course.instructor}</p>
                </div>
              </div>
              <span style={{
                backgroundColor: '#ecfdf5',
                color: '#059669',
                padding: '4px 12px',
                borderRadius: '20px',
                fontSize: '0.75rem',
                fontWeight: '600',
              }}>
                {course.credits} crédits
              </span>
            </div>
          ))}
          {courses.length === 0 && (
            <p style={{ color: '#94a3b8', textAlign: 'center', padding: '20px' }}>Aucun cours</p>
          )}
        </div>
      </div>
    </div>
  );
};

// ============================================
// PAGE ÉTUDIANTS
// ============================================
const StudentsPage = ({ students, onRefresh }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', dateOfBirth: '', phone: '', address: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Préparer les données pour l'API
      const apiData = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phoneNumber: formData.phone || null, // Mapper 'phone' vers 'phoneNumber'
        address: formData.address || null,
        // Le champ type="date" retourne déjà le format YYYY-MM-DD
        // Mais on gère aussi le cas où l'utilisateur entre manuellement DD/MM/YYYY
        dateOfBirth: formData.dateOfBirth ? (() => {
          // Si la date est au format DD/MM/YYYY, la convertir en YYYY-MM-DD
          if (formData.dateOfBirth.includes('/')) {
            const [day, month, year] = formData.dateOfBirth.split('/');
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
          }
          return formData.dateOfBirth; // Déjà au format ISO (YYYY-MM-DD)
        })() : null
      };

      if (editingStudent) {
        await fetchAPI('/students/' + editingStudent.id, {
          method: 'PUT',
          body: JSON.stringify(apiData)
        });
      } else {
        await fetchAPI('/students', {
          method: 'POST',
          body: JSON.stringify(apiData)
        });
      }
      setIsModalOpen(false);
      setEditingStudent(null);
      setFormData({ firstName: '', lastName: '', email: '', dateOfBirth: '', phone: '', address: '' });
      onRefresh();
    } catch (error) {
      // Afficher un message d'erreur plus détaillé
      let errorMessage = 'Erreur lors de la sauvegarde';
      try {
        const errorData = await error.response?.json();
        if (errorData?.errors) {
          const errorDetails = Object.entries(errorData.errors)
            .map(([field, msg]) => `${field}: ${msg}`)
            .join('\n');
          errorMessage = `Erreurs de validation:\n${errorDetails}`;
        } else if (errorData?.message) {
          errorMessage = errorData.message;
        }
      } catch (e) {
        // Si on ne peut pas parser l'erreur, utiliser le message par défaut
      }
      alert(errorMessage);
    }
  };

  const handleEdit = (student) => {
    setEditingStudent(student);
    // Convertir la date ISO en format DD/MM/YYYY pour l'affichage
    let dateOfBirth = '';
    if (student.dateOfBirth) {
      try {
        const date = new Date(student.dateOfBirth);
        if (!isNaN(date.getTime())) {
          const day = String(date.getDate()).padStart(2, '0');
          const month = String(date.getMonth() + 1).padStart(2, '0');
          const year = date.getFullYear();
          dateOfBirth = `${day}/${month}/${year}`;
        }
      } catch (e) {
        dateOfBirth = student.dateOfBirth;
      }
    }
    setFormData({
      firstName: student.firstName || '',
      lastName: student.lastName || '',
      email: student.email || '',
      dateOfBirth: dateOfBirth,
      phone: student.phoneNumber || student.phone || '',
      address: student.address || ''
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?')) {
      try {
        await fetchAPI('/students/' + id, { method: 'DELETE' });
        onRefresh();
      } catch (error) {
        alert('Erreur lors de la suppression');
      }
    }
  };

  const inputStyle = {
    width: '100%',
    padding: '12px 16px',
    borderRadius: '10px',
    border: '2px solid #e2e8f0',
    fontSize: '0.95rem',
    outline: 'none',
    boxSizing: 'border-box',
  };

  const labelStyle = {
    display: 'block',
    marginBottom: '6px',
    fontWeight: '500',
    color: '#374151',
    fontSize: '0.9rem',
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1 style={{ fontSize: '2rem', fontWeight: '700', color: '#1e293b', marginBottom: '8px' }}>Étudiants</h1>
          <p style={{ color: '#64748b' }}>{students.length} étudiant(s) enregistré(s)</p>
        </div>
        <button
          onClick={() => { setEditingStudent(null); setFormData({ firstName: '', lastName: '', email: '', dateOfBirth: '', phone: '', address: '' }); setIsModalOpen(true); }}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            padding: '12px 24px',
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            color: 'white',
            border: 'none',
            borderRadius: '12px',
            cursor: 'pointer',
            fontWeight: '600',
            fontSize: '0.95rem',
            boxShadow: '0 4px 15px rgba(102, 126, 234, 0.4)',
          }}
        >
          <Icons.Plus />
          Ajouter un étudiant
        </button>
      </div>

      <div style={{
        backgroundColor: 'white',
        borderRadius: '16px',
        overflow: 'hidden',
        boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
      }}>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ backgroundColor: '#f8fafc' }}>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Étudiant</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Email</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Téléphone</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Statut</th>
                <th style={{ padding: '16px 24px', textAlign: 'right', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {students.map((student, index) => (
                <tr key={student.id || index} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: '16px 24px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <div style={{
                        width: '40px',
                        height: '40px',
                        borderRadius: '10px',
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        fontWeight: '600',
                        fontSize: '0.875rem',
                      }}>
                        {(student.firstName || '?')[0]}{(student.lastName || '?')[0]}
                      </div>
                      <div>
                        <p style={{ fontWeight: '500', color: '#1e293b' }}>{student.firstName} {student.lastName}</p>
                        <p style={{ fontSize: '0.8rem', color: '#64748b' }}>#{student.studentNumber || student.id}</p>
                      </div>
                    </div>
                  </td>
                  <td style={{ padding: '16px 24px', color: '#475569' }}>{student.email}</td>
                  <td style={{ padding: '16px 24px', color: '#475569' }}>{student.phone || '-'}</td>
                  <td style={{ padding: '16px 24px' }}>
                    <span style={{
                      padding: '6px 12px',
                      borderRadius: '20px',
                      fontSize: '0.75rem',
                      fontWeight: '600',
                      backgroundColor: student.status === 'ACTIVE' ? '#dcfce7' : '#fef3c7',
                      color: student.status === 'ACTIVE' ? '#166534' : '#92400e',
                    }}>
                      {student.status || 'ACTIVE'}
                    </span>
                  </td>
                  <td style={{ padding: '16px 24px' }}>
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                      <button onClick={() => handleEdit(student)} style={{ padding: '8px', backgroundColor: '#f1f5f9', border: 'none', borderRadius: '8px', cursor: 'pointer', color: '#475569' }} title="Modifier">
                        <Icons.Edit />
                      </button>
                      <button onClick={() => handleDelete(student.id)} style={{ padding: '8px', backgroundColor: '#fef2f2', border: 'none', borderRadius: '8px', cursor: 'pointer', color: '#ef4444' }} title="Supprimer">
                        <Icons.Trash2 />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {students.length === 0 && (
          <div style={{ padding: '48px', textAlign: 'center', color: '#94a3b8' }}>
            <p>Aucun étudiant enregistré</p>
          </div>
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={editingStudent ? "Modifier l'étudiant" : 'Nouvel étudiant'}>
        <form onSubmit={handleSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={labelStyle}>Prénom</label>
              <input type="text" value={formData.firstName} onChange={(e) => setFormData({ ...formData, firstName: e.target.value })} style={inputStyle} required />
            </div>
            <div>
              <label style={labelStyle}>Nom</label>
              <input type="text" value={formData.lastName} onChange={(e) => setFormData({ ...formData, lastName: e.target.value })} style={inputStyle} required />
            </div>
          </div>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Email</label>
            <input type="email" value={formData.email} onChange={(e) => setFormData({ ...formData, email: e.target.value })} style={inputStyle} required />
          </div>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Date de naissance</label>
            <input type="date" value={formData.dateOfBirth} onChange={(e) => setFormData({ ...formData, dateOfBirth: e.target.value })} style={inputStyle} />
          </div>
          <div style={{ marginBottom: '24px' }}>
            <label style={labelStyle}>Téléphone</label>
            <input type="tel" value={formData.phone} onChange={(e) => setFormData({ ...formData, phone: e.target.value })} style={inputStyle} />
          </div>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button type="button" onClick={() => setIsModalOpen(false)} style={{ flex: 1, padding: '14px', border: '2px solid #e2e8f0', borderRadius: '10px', backgroundColor: 'white', color: '#475569', fontWeight: '600', cursor: 'pointer' }}>
              Annuler
            </button>
            <button type="submit" style={{ flex: 1, padding: '14px', border: 'none', borderRadius: '10px', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white', fontWeight: '600', cursor: 'pointer' }}>
              {editingStudent ? 'Mettre à jour' : 'Créer'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

// ============================================
// PAGE COURS
// ============================================
const CoursesPage = ({ courses, onRefresh }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCourse, setEditingCourse] = useState(null);
  const [formData, setFormData] = useState({
    code: '', title: '', description: '', instructor: '', credits: 3, maxCapacity: 30
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Préparer les données pour l'API - mapper les champs frontend vers backend
      const apiData = {
        courseCode: formData.code, // Mapper 'code' vers 'courseCode'
        title: formData.title,
        description: formData.description || null,
        professorName: formData.instructor, // Mapper 'instructor' vers 'professorName'
        credits: formData.credits,
        maxCapacity: formData.maxCapacity
      };

      if (editingCourse) {
        await fetchAPI('/courses/' + editingCourse.id, { method: 'PUT', body: JSON.stringify(apiData) });
      } else {
        await fetchAPI('/courses', { method: 'POST', body: JSON.stringify(apiData) });
      }
      setIsModalOpen(false);
      setEditingCourse(null);
      setFormData({ code: '', title: '', description: '', instructor: '', credits: 3, maxCapacity: 30 });
      onRefresh();
    } catch (error) {
      // Afficher un message d'erreur plus détaillé
      let errorMessage = 'Erreur lors de la sauvegarde';
      try {
        const errorData = await error.response?.json();
        if (errorData?.errors) {
          const errorDetails = Object.entries(errorData.errors)
            .map(([field, msg]) => `${field}: ${msg}`)
            .join('\n');
          errorMessage = `Erreurs de validation:\n${errorDetails}`;
        } else if (errorData?.message) {
          errorMessage = errorData.message;
        }
      } catch (e) {
        // Si on ne peut pas parser l'erreur, utiliser le message par défaut
      }
      alert(errorMessage);
    }
  };

  const handleEdit = (course) => {
    setEditingCourse(course);
    setFormData({
      code: course.courseCode || course.code || '', // Mapper 'courseCode' vers 'code'
      title: course.title || '',
      description: course.description || '',
      instructor: course.professorName || course.instructor || '', // Mapper 'professorName' vers 'instructor'
      credits: course.credits || 3,
      maxCapacity: course.maxCapacity || 30
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce cours ?')) {
      try {
        await fetchAPI('/courses/' + id, { method: 'DELETE' });
        onRefresh();
      } catch (error) {
        alert('Erreur lors de la suppression');
      }
    }
  };

  const inputStyle = { width: '100%', padding: '12px 16px', borderRadius: '10px', border: '2px solid #e2e8f0', fontSize: '0.95rem', outline: 'none', boxSizing: 'border-box' };
  const labelStyle = { display: 'block', marginBottom: '6px', fontWeight: '500', color: '#374151', fontSize: '0.9rem' };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1 style={{ fontSize: '2rem', fontWeight: '700', color: '#1e293b', marginBottom: '8px' }}>Cours</h1>
          <p style={{ color: '#64748b' }}>{courses.length} cours disponible(s)</p>
        </div>
        <button onClick={() => { setEditingCourse(null); setFormData({ code: '', title: '', description: '', instructor: '', credits: 3, maxCapacity: 30 }); setIsModalOpen(true); }} style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '12px 24px', background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)', color: 'white', border: 'none', borderRadius: '12px', cursor: 'pointer', fontWeight: '600', fontSize: '0.95rem', boxShadow: '0 4px 15px rgba(16, 185, 129, 0.4)' }}>
          <Icons.Plus />
          Ajouter un cours
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '24px' }}>
        {courses.map((course, index) => (
          <div key={course.id || index} style={{ backgroundColor: 'white', borderRadius: '16px', overflow: 'hidden', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }}>
            <div style={{ background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)', padding: '20px 24px', color: 'white' }}>
              <span style={{ backgroundColor: 'rgba(255,255,255,0.2)', padding: '4px 10px', borderRadius: '6px', fontSize: '0.75rem', fontWeight: '600' }}>{course.courseCode || course.code}</span>
              <h3 style={{ marginTop: '12px', fontSize: '1.25rem', fontWeight: '600' }}>{course.title}</h3>
            </div>
            <div style={{ padding: '20px 24px' }}>
              <p style={{ color: '#64748b', fontSize: '0.9rem', marginBottom: '16px', lineHeight: '1.5' }}>{course.description || 'Aucune description'}</p>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                <div>
                  <p style={{ fontSize: '0.8rem', color: '#94a3b8' }}>Instructeur</p>
                  <p style={{ fontWeight: '500', color: '#1e293b' }}>{course.professorName || course.instructor}</p>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <p style={{ fontSize: '0.8rem', color: '#94a3b8' }}>Crédits</p>
                  <p style={{ fontWeight: '600', color: '#10b981', fontSize: '1.25rem' }}>{course.credits}</p>
                </div>
              </div>
              <div style={{ display: 'flex', gap: '8px' }}>
                <button onClick={() => handleEdit(course)} style={{ flex: 1, padding: '10px', backgroundColor: '#f1f5f9', border: 'none', borderRadius: '8px', cursor: 'pointer', color: '#475569', fontWeight: '500', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '6px' }}>
                  <Icons.Edit /> Modifier
                </button>
                <button onClick={() => handleDelete(course.id)} style={{ padding: '10px', backgroundColor: '#fef2f2', border: 'none', borderRadius: '8px', cursor: 'pointer', color: '#ef4444' }}>
                  <Icons.Trash2 />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {courses.length === 0 && (
        <div style={{ backgroundColor: 'white', borderRadius: '16px', padding: '48px', textAlign: 'center', color: '#94a3b8' }}>
          <p>Aucun cours disponible</p>
        </div>
      )}

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={editingCourse ? 'Modifier le cours' : 'Nouveau cours'}>
        <form onSubmit={handleSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={labelStyle}>Code</label>
              <input type="text" value={formData.code} onChange={(e) => setFormData({ ...formData, code: e.target.value })} style={inputStyle} placeholder="CS101" required />
            </div>
            <div>
              <label style={labelStyle}>Titre</label>
              <input type="text" value={formData.title} onChange={(e) => setFormData({ ...formData, title: e.target.value })} style={inputStyle} required />
            </div>
          </div>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Description</label>
            <textarea value={formData.description} onChange={(e) => setFormData({ ...formData, description: e.target.value })} style={{ ...inputStyle, minHeight: '80px', resize: 'vertical' }} />
          </div>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Instructeur</label>
            <input type="text" value={formData.instructor} onChange={(e) => setFormData({ ...formData, instructor: e.target.value })} style={inputStyle} required />
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '24px' }}>
            <div>
              <label style={labelStyle}>Crédits</label>
              <input type="number" value={formData.credits} onChange={(e) => setFormData({ ...formData, credits: parseInt(e.target.value) })} style={inputStyle} min="1" max="10" />
            </div>
            <div>
              <label style={labelStyle}>Capacité max</label>
              <input type="number" value={formData.maxCapacity} onChange={(e) => setFormData({ ...formData, maxCapacity: parseInt(e.target.value) })} style={inputStyle} min="1" />
            </div>
          </div>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button type="button" onClick={() => setIsModalOpen(false)} style={{ flex: 1, padding: '14px', border: '2px solid #e2e8f0', borderRadius: '10px', backgroundColor: 'white', color: '#475569', fontWeight: '600', cursor: 'pointer' }}>Annuler</button>
            <button type="submit" style={{ flex: 1, padding: '14px', border: 'none', borderRadius: '10px', background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)', color: 'white', fontWeight: '600', cursor: 'pointer' }}>{editingCourse ? 'Mettre à jour' : 'Créer'}</button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

// ============================================
// PAGE INSCRIPTIONS
// ============================================
const EnrollmentsPage = ({ enrollments, students, courses, onRefresh }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({ studentId: '', courseId: '' });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await fetchAPI('/enrollments', { method: 'POST', body: JSON.stringify({ studentId: parseInt(formData.studentId), courseId: parseInt(formData.courseId) }) });
      setIsModalOpen(false);
      setFormData({ studentId: '', courseId: '' });
      onRefresh();
    } catch (error) {
      alert("Erreur lors de l'inscription");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette inscription ?')) {
      try {
        await fetchAPI('/enrollments/' + id, { method: 'DELETE' });
        onRefresh();
      } catch (error) {
        alert('Erreur lors de la suppression');
      }
    }
  };

  const getStudentName = (studentId) => {
    const student = students.find(s => s.id === studentId);
    return student ? student.firstName + ' ' + student.lastName : 'Étudiant #' + studentId;
  };

  const getCourseName = (courseId) => {
    const course = courses.find(c => c.id === courseId);
    return course ? course.title : 'Cours #' + courseId;
  };

  const selectStyle = { width: '100%', padding: '12px 16px', borderRadius: '10px', border: '2px solid #e2e8f0', fontSize: '0.95rem', outline: 'none', boxSizing: 'border-box', backgroundColor: 'white' };
  const labelStyle = { display: 'block', marginBottom: '6px', fontWeight: '500', color: '#374151', fontSize: '0.9rem' };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1 style={{ fontSize: '2rem', fontWeight: '700', color: '#1e293b', marginBottom: '8px' }}>Inscriptions</h1>
          <p style={{ color: '#64748b' }}>{enrollments.length} inscription(s) active(s)</p>
        </div>
        <button onClick={() => { setFormData({ studentId: '', courseId: '' }); setIsModalOpen(true); }} style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '12px 24px', background: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)', color: 'white', border: 'none', borderRadius: '12px', cursor: 'pointer', fontWeight: '600', fontSize: '0.95rem', boxShadow: '0 4px 15px rgba(245, 158, 11, 0.4)' }}>
          <Icons.Plus />
          Nouvelle inscription
        </button>
      </div>

      <div style={{ backgroundColor: 'white', borderRadius: '16px', overflow: 'hidden', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }}>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ backgroundColor: '#f8fafc' }}>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Étudiant</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Cours</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Statut</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Date</th>
                <th style={{ padding: '16px 24px', textAlign: 'right', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {enrollments.map((enrollment, index) => (
                <tr key={enrollment.id || index} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: '16px 24px', fontWeight: '500', color: '#1e293b' }}>{getStudentName(enrollment.studentId)}</td>
                  <td style={{ padding: '16px 24px', color: '#475569' }}>{getCourseName(enrollment.courseId)}</td>
                  <td style={{ padding: '16px 24px' }}>
                    <span style={{ padding: '6px 12px', borderRadius: '20px', fontSize: '0.75rem', fontWeight: '600', backgroundColor: enrollment.status === 'ENROLLED' ? '#dbeafe' : '#fef3c7', color: enrollment.status === 'ENROLLED' ? '#1d4ed8' : '#92400e' }}>{enrollment.status || 'ENROLLED'}</span>
                  </td>
                  <td style={{ padding: '16px 24px', color: '#64748b', fontSize: '0.9rem' }}>{enrollment.enrollmentDate ? new Date(enrollment.enrollmentDate).toLocaleDateString() : '-'}</td>
                  <td style={{ padding: '16px 24px' }}>
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                      <button onClick={() => handleDelete(enrollment.id)} style={{ padding: '8px', backgroundColor: '#fef2f2', border: 'none', borderRadius: '8px', cursor: 'pointer', color: '#ef4444' }} title="Supprimer">
                        <Icons.Trash2 />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {enrollments.length === 0 && (
          <div style={{ padding: '48px', textAlign: 'center', color: '#94a3b8' }}>
            <p>Aucune inscription</p>
          </div>
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Nouvelle inscription">
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Étudiant</label>
            <select value={formData.studentId} onChange={(e) => setFormData({ ...formData, studentId: e.target.value })} style={selectStyle} required>
              <option value="">Sélectionner un étudiant</option>
              {students.map(student => (
                <option key={student.id} value={student.id}>{student.firstName} {student.lastName}</option>
              ))}
            </select>
          </div>
          <div style={{ marginBottom: '24px' }}>
            <label style={labelStyle}>Cours</label>
            <select value={formData.courseId} onChange={(e) => setFormData({ ...formData, courseId: e.target.value })} style={selectStyle} required>
              <option value="">Sélectionner un cours</option>
              {courses.map(course => (
                <option key={course.id} value={course.id}>{course.title} ({course.courseCode || course.code})</option>
              ))}
            </select>
          </div>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button type="button" onClick={() => setIsModalOpen(false)} style={{ flex: 1, padding: '14px', border: '2px solid #e2e8f0', borderRadius: '10px', backgroundColor: 'white', color: '#475569', fontWeight: '600', cursor: 'pointer' }}>Annuler</button>
            <button type="submit" style={{ flex: 1, padding: '14px', border: 'none', borderRadius: '10px', background: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)', color: 'white', fontWeight: '600', cursor: 'pointer' }}>Inscrire</button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

// ============================================
// PAGE NOTES
// ============================================
const GradesPage = ({ grades, enrollments, students, courses, onRefresh }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({ enrollmentId: '', value: '', gradeType: 'EXAM', coefficient: 1, comment: '', gradedBy: '' });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const value = parseFloat(formData.value);
    if (value < 0 || value > 20) {
      alert('La note doit être entre 0 et 20');
      return;
    }
    const enrollment = enrollments.find(en => en.id === parseInt(formData.enrollmentId));
    if (!enrollment) {
      alert('Inscription non trouvée');
      return;
    }
    try {
      await fetchAPI('/grades', { method: 'POST', body: JSON.stringify({ enrollmentId: parseInt(formData.enrollmentId), studentId: enrollment.studentId, courseId: enrollment.courseId, value, gradeType: formData.gradeType, coefficient: parseFloat(formData.coefficient), comment: formData.comment || '', gradedBy: formData.gradedBy || '' }) });
      setIsModalOpen(false);
      setFormData({ enrollmentId: '', value: '', gradeType: 'EXAM', coefficient: 1, comment: '', gradedBy: '' });
      onRefresh();
    } catch (error) {
      alert('Erreur');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette note ?')) {
      try {
        await fetchAPI('/grades/' + id, { method: 'DELETE' });
        onRefresh();
      } catch (error) {
        alert('Erreur lors de la suppression');
      }
    }
  };

  const getStudentName = (studentId) => {
    const student = students.find(s => s.id === studentId);
    return student ? student.firstName + ' ' + student.lastName : 'Étudiant #' + studentId;
  };

  const getCourseName = (courseId) => {
    const course = courses.find(c => c.id === courseId);
    return course ? course.title : 'Cours #' + courseId;
  };

  const getLetterGrade = (value) => {
    if (value >= 16) return 'A';
    if (value >= 14) return 'B';
    if (value >= 12) return 'C';
    if (value >= 10) return 'D';
    return 'F';
  };

  const inputStyle = { width: '100%', padding: '12px 16px', borderRadius: '10px', border: '2px solid #e2e8f0', fontSize: '0.95rem', outline: 'none', boxSizing: 'border-box' };
  const selectStyle = { ...inputStyle, backgroundColor: 'white' };
  const labelStyle = { display: 'block', marginBottom: '6px', fontWeight: '500', color: '#374151', fontSize: '0.9rem' };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1 style={{ fontSize: '2rem', fontWeight: '700', color: '#1e293b', marginBottom: '8px' }}>Notes</h1>
          <p style={{ color: '#64748b' }}>{grades.length} note(s) attribuée(s)</p>
        </div>
        <button onClick={() => { setFormData({ enrollmentId: '', value: '', gradeType: 'EXAM', coefficient: 1, comment: '', gradedBy: '' }); setIsModalOpen(true); }} style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '12px 24px', background: 'linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%)', color: 'white', border: 'none', borderRadius: '12px', cursor: 'pointer', fontWeight: '600', fontSize: '0.95rem', boxShadow: '0 4px 15px rgba(139, 92, 246, 0.4)' }}>
          <Icons.Plus />
          Attribuer une note
        </button>
      </div>

      <div style={{ backgroundColor: 'white', borderRadius: '16px', overflow: 'hidden', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }}>
        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ backgroundColor: '#f8fafc' }}>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Étudiant</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Cours</th>
                <th style={{ padding: '16px 24px', textAlign: 'left', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Type</th>
                <th style={{ padding: '16px 24px', textAlign: 'center', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Note</th>
                <th style={{ padding: '16px 24px', textAlign: 'center', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Lettre</th>
                <th style={{ padding: '16px 24px', textAlign: 'center', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Coef.</th>
                <th style={{ padding: '16px 24px', textAlign: 'right', fontWeight: '600', color: '#475569', fontSize: '0.875rem' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {grades.map((grade, index) => (
                <tr key={grade.id || index} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: '16px 24px', fontWeight: '500', color: '#1e293b' }}>{getStudentName(grade.studentId)}</td>
                  <td style={{ padding: '16px 24px', color: '#475569' }}>{getCourseName(grade.courseId)}</td>
                  <td style={{ padding: '16px 24px' }}>
                    <span style={{ padding: '4px 10px', borderRadius: '6px', fontSize: '0.75rem', fontWeight: '500', backgroundColor: '#f1f5f9', color: '#475569' }}>{grade.gradeType || 'EXAM'}</span>
                  </td>
                  <td style={{ padding: '16px 24px', textAlign: 'center' }}>
                    <span style={{ fontSize: '1.25rem', fontWeight: '700', color: grade.value >= 10 ? '#10b981' : '#ef4444' }}>{grade.value}/20</span>
                  </td>
                  <td style={{ padding: '16px 24px', textAlign: 'center' }}>
                    <span style={{ display: 'inline-block', width: '32px', height: '32px', lineHeight: '32px', borderRadius: '8px', fontWeight: '700', backgroundColor: grade.value >= 10 ? '#dcfce7' : '#fef2f2', color: grade.value >= 10 ? '#166534' : '#dc2626' }}>{getLetterGrade(grade.value)}</span>
                  </td>
                  <td style={{ padding: '16px 24px', textAlign: 'center', color: '#64748b' }}>{grade.coefficient || 1}</td>
                  <td style={{ padding: '16px 24px' }}>
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                      <button onClick={() => handleDelete(grade.id)} style={{ padding: '8px', backgroundColor: '#fef2f2', border: 'none', borderRadius: '8px', cursor: 'pointer', color: '#ef4444' }} title="Supprimer">
                        <Icons.Trash2 />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {grades.length === 0 && (
          <div style={{ padding: '48px', textAlign: 'center', color: '#94a3b8' }}>
            <p>Aucune note attribuée</p>
          </div>
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Attribuer une note">
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Inscription</label>
            <select value={formData.enrollmentId} onChange={(e) => setFormData({ ...formData, enrollmentId: e.target.value })} style={selectStyle} required>
              <option value="">Sélectionner une inscription</option>
              {enrollments.map(enrollment => {
                const student = students.find(s => s.id === enrollment.studentId);
                const course = courses.find(c => c.id === enrollment.courseId);
                return (
                  <option key={enrollment.id} value={enrollment.id}>
                    {student ? student.firstName + ' ' + student.lastName : 'Étudiant #' + enrollment.studentId} → {course ? course.title : 'Cours #' + enrollment.courseId}
                  </option>
                );
              })}
            </select>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
            <div>
              <label style={labelStyle}>Note (/20)</label>
              <input type="number" value={formData.value} onChange={(e) => setFormData({ ...formData, value: e.target.value })} style={inputStyle} min="0" max="20" step="0.5" required />
            </div>
            <div>
              <label style={labelStyle}>Coefficient</label>
              <input type="number" value={formData.coefficient} onChange={(e) => setFormData({ ...formData, coefficient: e.target.value })} style={inputStyle} min="0.5" max="10" step="0.5" />
            </div>
          </div>
          <div style={{ marginBottom: '16px' }}>
            <label style={labelStyle}>Type</label>
            <select value={formData.gradeType} onChange={(e) => setFormData({ ...formData, gradeType: e.target.value })} style={selectStyle}>
              <option value="EXAM">Examen</option>
              <option value="MIDTERM">Partiel</option>
              <option value="QUIZ">Contrôle</option>
              <option value="HOMEWORK">Devoir</option>
              <option value="PROJECT">Projet</option>
              <option value="PARTICIPATION">Participation</option>
              <option value="LAB">TP</option>
            </select>
          </div>
          <div style={{ marginBottom: '24px' }}>
            <label style={labelStyle}>Évaluateur</label>
            <input type="text" value={formData.gradedBy} onChange={(e) => setFormData({ ...formData, gradedBy: e.target.value })} style={inputStyle} placeholder="Nom de l'évaluateur" />
          </div>
          <div style={{ display: 'flex', gap: '12px' }}>
            <button type="button" onClick={() => setIsModalOpen(false)} style={{ flex: 1, padding: '14px', border: '2px solid #e2e8f0', borderRadius: '10px', backgroundColor: 'white', color: '#475569', fontWeight: '600', cursor: 'pointer' }}>Annuler</button>
            <button type="submit" style={{ flex: 1, padding: '14px', border: 'none', borderRadius: '10px', background: 'linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%)', color: 'white', fontWeight: '600', cursor: 'pointer' }}>Attribuer</button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

// ============================================
// APPLICATION PRINCIPALE
// ============================================
function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(() => {
    return localStorage.getItem('isAuthenticated') === 'true';
  });
  const [currentPage, setCurrentPage] = useState('dashboard');
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [students, setStudents] = useState([]);
  const [courses, setCourses] = useState([]);
  const [enrollments, setEnrollments] = useState([]);
  const [grades, setGrades] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchAllData = async () => {
    setLoading(true);
    try {
      const [studentsData, coursesData, enrollmentsData, gradesData] = await Promise.all([
        fetchAPI('/students').catch(() => []),
        fetchAPI('/courses').catch(() => []),
        fetchAPI('/enrollments').catch(() => []),
        fetchAPI('/grades').catch(() => []),
      ]);
      setStudents(studentsData || []);
      setCourses(coursesData || []);
      setEnrollments(enrollmentsData || []);
      setGrades(gradesData || []);
    } catch (error) {
      console.error('Erreur lors du chargement des données:', error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchAllData();
  }, []);

  const menuItems = [
    { id: 'dashboard', label: 'Tableau de bord', icon: Icons.TrendingUp },
    { id: 'students', label: 'Étudiants', icon: Icons.Users },
    { id: 'courses', label: 'Cours', icon: Icons.BookOpen },
    { id: 'enrollments', label: 'Inscriptions', icon: Icons.ClipboardList },
    { id: 'grades', label: 'Notes', icon: Icons.Award },
  ];

  const renderPage = () => {
    if (loading) {
      return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '60vh' }}>
          <div style={{ textAlign: 'center' }}>
            <div style={{ width: '50px', height: '50px', border: '4px solid #e2e8f0', borderTopColor: '#667eea', borderRadius: '50%', animation: 'spin 1s linear infinite', margin: '0 auto 16px' }} />
            <p style={{ color: '#64748b' }}>Chargement...</p>
          </div>
        </div>
      );
    }

    switch (currentPage) {
      case 'students': return <StudentsPage students={students} onRefresh={fetchAllData} />;
      case 'courses': return <CoursesPage courses={courses} onRefresh={fetchAllData} />;
      case 'enrollments': return <EnrollmentsPage enrollments={enrollments} students={students} courses={courses} onRefresh={fetchAllData} />;
      case 'grades': return <GradesPage grades={grades} enrollments={enrollments} students={students} courses={courses} onRefresh={fetchAllData} />;
      default: return <Dashboard students={students} courses={courses} enrollments={enrollments} grades={grades} />;
    }
  };

  const cssAnimations = '@keyframes spin { to { transform: rotate(360deg); } }';

  // Si l'utilisateur n'est pas authentifié, afficher la page de login
  if (!isAuthenticated) {
    return <Login onLogin={setIsAuthenticated} />;
  }

  const handleLogout = () => {
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('username');
    setIsAuthenticated(false);
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: '#f1f5f9' }}>
      <style>{cssAnimations}</style>

      {sidebarOpen && (
        <div onClick={() => setSidebarOpen(false)} style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0, 0, 0, 0.5)', zIndex: 999 }} />
      )}

      <aside style={{
        width: '280px',
        position: 'fixed',
        left: sidebarOpen ? '0' : '-280px',
        top: 0,
        height: '100vh',
        background: 'linear-gradient(180deg, #1e1b4b 0%, #3730a3 50%, #6366f1 100%)',
        color: 'white',
        padding: '24px 16px',
        display: 'flex',
        flexDirection: 'column',
        zIndex: 1000,
        overflowY: 'auto',
        transition: 'left 0.3s ease',
        boxShadow: sidebarOpen ? '4px 0 20px rgba(0, 0, 0, 0.2)' : 'none',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '32px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <div style={{ backgroundColor: 'rgba(255,255,255,0.15)', borderRadius: '12px', padding: '10px' }}>
              <Icons.GraduationCap />
            </div>
            <div>
              <h1 style={{ fontSize: '1.5rem', fontWeight: '700' }}>Mini-LMS</h1>
              <p style={{ fontSize: '0.75rem', opacity: 0.7 }}>Système Éducatif</p>
            </div>
          </div>
          <button onClick={() => setSidebarOpen(false)} style={{ background: 'rgba(255,255,255,0.1)', border: 'none', borderRadius: '8px', padding: '8px', cursor: 'pointer', color: 'white', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Icons.X />
          </button>
        </div>

        <nav style={{ flex: 1 }}>
          {menuItems.map((item) => (
            <button
              key={item.id}
              onClick={() => { setCurrentPage(item.id); setSidebarOpen(false); }}
              style={{
                width: '100%',
                display: 'flex',
                alignItems: 'center',
                gap: '12px',
                padding: '14px 16px',
                marginBottom: '8px',
                border: 'none',
                borderRadius: '12px',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                backgroundColor: currentPage === item.id ? 'rgba(255,255,255,0.2)' : 'transparent',
                color: 'white',
                fontSize: '0.95rem',
                fontWeight: currentPage === item.id ? '600' : '400',
                textAlign: 'left',
              }}
            >
              <item.icon />
              {item.label}
            </button>
          ))}
        </nav>

        <div style={{ borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '20px', marginTop: '20px' }}>
          <div style={{ backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: '12px', padding: '16px' }}>
            <p style={{ fontSize: '0.75rem', opacity: 0.7, marginBottom: '4px' }}>Services Status</p>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <div style={{ width: '8px', height: '8px', borderRadius: '50%', backgroundColor: '#4ade80' }} />
              <span style={{ fontSize: '0.8rem' }}>Tous les services actifs</span>
            </div>
          </div>
        </div>
      </aside>

      <main style={{ flex: 1, padding: '24px', minHeight: '100vh', backgroundColor: '#f1f5f9', width: '100%', transition: 'all 0.3s ease' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '24px', backgroundColor: 'white', padding: '16px 24px', borderRadius: '16px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <button onClick={() => setSidebarOpen(true)} style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', border: 'none', cursor: 'pointer', padding: '12px', borderRadius: '12px', display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 4px 15px rgba(102, 126, 234, 0.3)', color: 'white' }} title="Ouvrir le menu">
              <Icons.Menu />
            </button>
            <div>
              <h2 style={{ fontSize: '1.25rem', fontWeight: '600', color: '#1e293b' }}>
                {(menuItems.find(m => m.id === currentPage) || {}).label || 'Tableau de bord'}
              </h2>
              <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Mini-LMS - Système Éducatif</p>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
            <span style={{ fontSize: '0.9rem', color: '#64748b' }}>
              {localStorage.getItem('username') || 'Utilisateur'}
            </span>
            <button onClick={fetchAllData} style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '10px 20px', backgroundColor: '#f1f5f9', border: 'none', borderRadius: '10px', cursor: 'pointer', color: '#64748b', fontWeight: '500' }}>
              <Icons.RefreshCw />
              Actualiser
            </button>
            <button onClick={handleLogout} style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '10px 20px', backgroundColor: '#fef2f2', border: 'none', borderRadius: '10px', cursor: 'pointer', color: '#dc2626', fontWeight: '500' }} title="Déconnexion">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                <polyline points="16 17 21 12 16 7"/>
                <line x1="21" y1="12" x2="9" y2="12"/>
              </svg>
              Déconnexion
            </button>
          </div>
        </div>

        {renderPage()}
      </main>
    </div>
  );
}

export default App;