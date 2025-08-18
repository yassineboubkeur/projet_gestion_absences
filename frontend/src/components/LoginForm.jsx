import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function LoginPage() {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const loginStore = useAuthStore((state) => state.login);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ login, password }),
      });

      const data = await response.json();

      if (!response.ok) {
        toast.error(data.message || "Erreur lors de la connexion");
        return;
      }

      loginStore(
        {
          id: data.userId,
          nomComplet: data.nomComplet,
          email: data.email,
          role: data.role,
        },
        data.token
      );
     

      toast.success("Connexion réussie !");
      navigate("/dashboard");
    } catch (err) {
      toast.error(err.message || "Erreur réseau");
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "400px" }}>
      <h2 className="mb-4 text-center">Connexion</h2>

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="login" className="form-label">
            Login
          </label>
          <input
            id="login"
            name="login"
            type="text"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
            className="form-control"
          />
        </div>

        <div className="mb-3">
          <label htmlFor="password" className="form-label">
            Mot de passe
          </label>
          <input
            id="password"
            name="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="form-control"
          />
        </div>

        <button type="submit" className="btn btn-primary w-100">
          Se connecter
        </button>
      </form>

      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}

export default LoginPage;
