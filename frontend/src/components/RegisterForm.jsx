import React, { useState } from 'react';

function RegisterPage() {
  const [form, setForm] = useState({
    nom: '',
    prenom: '',
    email: '',
    login: '',
    password: '',
    matricule: '',
    role: 'ROLE_ETUDIANT',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Erreur lors de l'inscription");
      }

      setSuccess('Inscription réussie ! Vous pouvez maintenant vous connecter.');
      setForm({
        nom: '',
        prenom: '',
        email: '',
        login: '',
        password: '',
        matricule: '',
        role: 'ROLE_ETUDIANT',
      });
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="container mt-5 mb-5" style={{ maxWidth: '600px' }}>
      <h2 className="mb-4">Inscription</h2>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit}>
        {[
          { label: 'Nom', name: 'nom', type: 'text', required: true },
          { label: 'Prénom', name: 'prenom', type: 'text', required: true },
          { label: 'Email', name: 'email', type: 'email', required: true },
          { label: 'Login', name: 'login', type: 'text', required: true },
          { label: 'Mot de passe', name: 'password', type: 'password', required: true },
          { label: 'Matricule', name: 'matricule', type: 'text', required: false },
        ].map(({ label, name, type, required }) => (
          <div key={name} className="mb-3">
            <label htmlFor={name} className="form-label">{label}</label>
            <input
              id={name}
              name={name}
              type={type}
              value={form[name]}
              onChange={handleChange}
              required={required}
              className="form-control"
            />
          </div>
        ))}

        <div className="mb-3">
          <label htmlFor="role" className="form-label">Rôle</label>
          <select
            id="role"
            name="role"
            value={form.role}
            onChange={handleChange}
            required
            className="form-select"
          >
            <option value="ROLE_ETUDIANT">Étudiant</option>
            <option value="ROLE_PROFESSEUR">Professeur</option>
            <option value="ROLE_ADMIN">Admin</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary w-100">
          S'inscrire
        </button>
      </form>
    </div>
  );
}

export default RegisterPage;
