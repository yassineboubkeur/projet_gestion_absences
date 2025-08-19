import React, { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";

export default function ParametresStatic() {
  const [settings, setSettings] = useState({
    nomEtablissement: "",
    emailContact: "",
    telephone: "",
    adresse: "",
  });

  const [success, setSuccess] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSettings((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSuccess("Paramètres mis à jour avec succès !");
    setSettings({
      nomEtablissement: "",
      emailContact: "",
      telephone: "",
      adresse: "",
    });
  };

  return (
    <div className="container mt-4">
      <h2>Paramètres</h2>

      {success && <Alert variant="success">{success}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Nom de l'établissement</Form.Label>
          <Form.Control
            type="text"
            name="nomEtablissement"
            value={settings.nomEtablissement}
            onChange={handleChange}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Email de contact</Form.Label>
          <Form.Control
            type="email"
            name="emailContact"
            value={settings.emailContact}
            onChange={handleChange}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Téléphone</Form.Label>
          <Form.Control
            type="text"
            name="telephone"
            value={settings.telephone}
            onChange={handleChange}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Adresse</Form.Label>
          <Form.Control
            type="text"
            name="adresse"
            value={settings.adresse}
            onChange={handleChange}
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Enregistrer
        </Button>
      </Form>
    </div>
  );
}
