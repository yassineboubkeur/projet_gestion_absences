import React, { useState } from 'react';

function LoginPage() {
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ login, password }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Erreur lors de la connexion');
      }

      setSuccess('Connexion réussie !');
      console.log('Token JWT:', data.token);
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="container">
      <h2 className="title">Connexion</h2>

      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="login" className="label">Login</label>
          <input
            id="login"
            name="login"
            type="text"
            value={login}
            onChange={e => setLogin(e.target.value)}
            required
            className="input"
          />
        </div>

        <div className="form-group">
          <label htmlFor="password" className="label">Mot de passe</label>
          <input
            id="password"
            name="password"
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
            className="input"
          />
        </div>

        <button type="submit" className="button">
          Se connecter
        </button>
      </form>
    </div>
  );
}

export default LoginPage;
