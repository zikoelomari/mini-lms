import React, { useState } from 'react';

// ============================================
// COMPOSANT LOGIN
// ============================================
const Login = ({ onLogin }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Simulation d'authentification simple
    // En production, cela devrait appeler un service d'authentification backend
    setTimeout(() => {
      // Pour la démo, accepte n'importe quel identifiant/mot de passe
      // ou utilise des identifiants par défaut
      if (formData.username && formData.password) {
        // Stocker l'état de connexion dans localStorage
        localStorage.setItem('isAuthenticated', 'true');
        localStorage.setItem('username', formData.username);
        onLogin(true);
      } else {
        setError('Veuillez remplir tous les champs');
      }
      setLoading(false);
    }, 500);
  };

  const inputStyle = {
    width: '100%',
    padding: '14px 18px',
    borderRadius: '12px',
    border: '2px solid #e2e8f0',
    fontSize: '1rem',
    outline: 'none',
    boxSizing: 'border-box',
    transition: 'all 0.2s ease',
  };

  const labelStyle = {
    display: 'block',
    marginBottom: '8px',
    fontWeight: '600',
    color: '#374151',
    fontSize: '0.95rem',
  };

  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      padding: '20px',
    }}>
      <div style={{
        backgroundColor: 'white',
        borderRadius: '24px',
        padding: '48px',
        width: '100%',
        maxWidth: '440px',
        boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
      }}>
        {/* Logo et titre */}
        <div style={{
          textAlign: 'center',
          marginBottom: '40px',
        }}>
          <div style={{
            width: '80px',
            height: '80px',
            margin: '0 auto 24px',
            backgroundColor: 'rgba(102, 126, 234, 0.1)',
            borderRadius: '20px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}>
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#667eea" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
              <path d="M6 12v5c3 3 9 3 12 0v-5"/>
            </svg>
          </div>
          <h1 style={{
            fontSize: '2rem',
            fontWeight: '700',
            color: '#1e293b',
            marginBottom: '8px',
          }}>
            Mini-LMS
          </h1>
          <p style={{
            color: '#64748b',
            fontSize: '1rem',
          }}>
            Connectez-vous à votre compte
          </p>
        </div>

        {/* Formulaire */}
        <form onSubmit={handleSubmit}>
          {error && (
            <div style={{
              backgroundColor: '#fef2f2',
              border: '1px solid #fecaca',
              color: '#dc2626',
              padding: '12px 16px',
              borderRadius: '10px',
              marginBottom: '24px',
              fontSize: '0.9rem',
            }}>
              {error}
            </div>
          )}

          <div style={{ marginBottom: '24px' }}>
            <label style={labelStyle}>Nom d'utilisateur</label>
            <input
              type="text"
              value={formData.username}
              onChange={(e) => setFormData({ ...formData, username: e.target.value })}
              style={{
                ...inputStyle,
                borderColor: error && !formData.username ? '#ef4444' : '#e2e8f0',
              }}
              placeholder="Entrez votre nom d'utilisateur"
              required
              autoFocus
            />
          </div>

          <div style={{ marginBottom: '32px' }}>
            <label style={labelStyle}>Mot de passe</label>
            <input
              type="password"
              value={formData.password}
              onChange={(e) => setFormData({ ...formData, password: e.target.value })}
              style={{
                ...inputStyle,
                borderColor: error && !formData.password ? '#ef4444' : '#e2e8f0',
              }}
              placeholder="Entrez votre mot de passe"
              required
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            style={{
              width: '100%',
              padding: '16px',
              background: loading 
                ? '#94a3b8' 
                : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white',
              border: 'none',
              borderRadius: '12px',
              fontSize: '1rem',
              fontWeight: '600',
              cursor: loading ? 'not-allowed' : 'pointer',
              boxShadow: loading 
                ? 'none' 
                : '0 4px 15px rgba(102, 126, 234, 0.4)',
              transition: 'all 0.2s ease',
            }}
          >
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>

        {/* Note d'information */}
        <div style={{
          marginTop: '32px',
          padding: '16px',
          backgroundColor: '#f8fafc',
          borderRadius: '12px',
          fontSize: '0.85rem',
          color: '#64748b',
          textAlign: 'center',
        }}>
          <p style={{ margin: 0 }}>
            <strong>Note:</strong> Pour la démonstration, vous pouvez utiliser n'importe quel identifiant et mot de passe.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;

