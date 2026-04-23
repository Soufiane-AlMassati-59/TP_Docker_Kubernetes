import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import './Confirmation.css';

function Confirmation() {
  const location = useLocation();
  const navigate = useNavigate();
  const { success, orderId, total, items } = location.state || {};

  useEffect(() => {
    if (!success) {
      navigate('/');
    }
  }, [success, navigate]);

  if (!success) return null;

  return (
    <div className="confirmation-container">
      <div className="confirmation-card">
        <div className="success-animation">
          <div className="checkmark">✓</div>
        </div>

        <h1>Réservation confirmée !</h1>
        <p className="confirmation-message">
          Félicitations ! Votre commande a été traitée avec succès.
        </p>

        <div className="order-info">
          <div className="info-row">
            <span className="label">Numéro de commande:</span>
            <span className="value order-id">{orderId}</span>
          </div>
          <div className="info-row">
            <span className="label">Montant total:</span>
            <span className="value amount">{total.toFixed(2)} €</span>
          </div>
          <div className="info-row">
            <span className="label">Date:</span>
            <span className="value">{new Date().toLocaleDateString('fr-FR')}</span>
          </div>
        </div>

        <div className="tickets-section">
          <h2>📧 Vos billets</h2>
          <p className="tickets-info">
            Vos e-billets ont été envoyés à votre adresse email.
            Vous pouvez également les télécharger ci-dessous.
          </p>

          <div className="tickets-list">
            {items.map(item => (
              <div key={item.spectacle.id} className="ticket-item">
                <div className="ticket-icon">🎫</div>
                <div className="ticket-details">
                  <h3>{item.spectacle.titre}</h3>
                  <p>{item.quantity} billet{item.quantity > 1 ? 's' : ''}</p>
                  <p className="ticket-date">
                    {new Date(item.spectacle.dateSpectacle).toLocaleDateString('fr-FR', {
                      weekday: 'long',
                      day: 'numeric',
                      month: 'long',
                      year: 'numeric'
                    })} à {item.spectacle.heureDebut}
                  </p>
                </div>
                <button className="btn-download">
                  Télécharger
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="next-steps">
          <h3>Prochaines étapes</h3>
          <ul>
            <li>✅ Vérifiez votre boîte email pour recevoir vos billets</li>
            <li>📱 Téléchargez vos billets ou imprimez-les</li>
            <li>🎭 Présentez-vous 15 minutes avant le spectacle</li>
            <li>🎫 Présentez vos billets à l'entrée (papier ou mobile)</li>
          </ul>
        </div>

        <div className="action-buttons">
          <button className="btn btn-primary" onClick={() => navigate('/')}>
            Retour à l'accueil
          </button>
          <button className="btn btn-outline" onClick={() => window.print()}>
            Imprimer la confirmation
          </button>
        </div>

        <div className="help-section">
          <p>
            <strong>Besoin d'aide ?</strong>
          </p>
          <p>
            Contactez notre service client au <a href="tel:0123456789">01 23 45 67 89</a>
            <br />
            ou par email : <a href="mailto:contact@theatre.fr">contact@theatre.fr</a>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Confirmation;
