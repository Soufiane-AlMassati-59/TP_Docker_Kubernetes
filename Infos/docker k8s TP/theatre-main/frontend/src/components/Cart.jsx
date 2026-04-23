import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import './Cart.css';

function Cart() {
  const navigate = useNavigate();
  const { cart, removeFromCart, updateQuantity, getTotal, clearCart } = useCart();
  const [showCheckout, setShowCheckout] = useState(false);

  if (cart.length === 0) {
    return (
      <div className="cart-empty">
        <div className="empty-icon">🛒</div>
        <h2>Votre panier est vide</h2>
        <p>Découvrez nos spectacles et ajoutez vos favoris au panier !</p>
        <button className="btn btn-primary" onClick={() => navigate('/')}>
          Voir les spectacles
        </button>
      </div>
    );
  }

  return (
    <div className="cart-container">
      <div className="cart-content">
        <div className="cart-items-section">
          <div className="cart-header">
            <h1>🛒 Mon Panier</h1>
            <button className="btn-text" onClick={clearCart}>
              Vider le panier
            </button>
          </div>

          <div className="cart-items">
            {cart.map(item => (
              <div key={item.spectacle.id} className="cart-item">
                <div className="cart-item-image">
                  <img 
                    src={item.spectacle.imageUrl || 'https://via.placeholder.com/150'} 
                    alt={item.spectacle.titre}
                  />
                </div>
                
                <div className="cart-item-details">
                  <h3>{item.spectacle.titre}</h3>
                  <p className="cart-item-genre">{item.spectacle.genre}</p>
                  <p className="cart-item-date">
                    📅 {new Date(item.spectacle.dateSpectacle).toLocaleDateString('fr-FR', {
                      weekday: 'long',
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric'
                    })}
                  </p>
                  <p className="cart-item-time">
                    🕐 {item.spectacle.heureDebut}
                  </p>
                </div>

                <div className="cart-item-actions">
                  <div className="quantity-control">
                    <button 
                      onClick={() => updateQuantity(item.spectacle.id, item.quantity - 1)}
                      className="quantity-btn"
                    >
                      −
                    </button>
                    <span className="quantity">{item.quantity}</span>
                    <button 
                      onClick={() => updateQuantity(item.spectacle.id, item.quantity + 1)}
                      className="quantity-btn"
                      disabled={item.quantity >= item.spectacle.placesDisponibles}
                    >
                      +
                    </button>
                  </div>
                  
                  <div className="cart-item-price">
                    <span className="price-label">Prix unitaire:</span>
                    <span className="price">{Number(item.spectacle.prixUnitaire).toFixed(2)} €</span>
                  </div>
                  
                  <div className="cart-item-total">
                    <span className="total-label">Sous-total:</span>
                    <span className="total">{(Number(item.spectacle.prixUnitaire) * item.quantity).toFixed(2)} €</span>
                  </div>

                  <button 
                    className="btn-remove"
                    onClick={() => removeFromCart(item.spectacle.id)}
                    title="Retirer du panier"
                  >
                    🗑️
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="cart-summary">
          <div className="summary-card">
            <h2>Récapitulatif</h2>
            
            <div className="summary-details">
              <div className="summary-line">
                <span>Nombre de billets:</span>
                <span className="summary-value">
                  {cart.reduce((sum, item) => sum + item.quantity, 0)}
                </span>
              </div>
              
              <div className="summary-line">
                <span>Sous-total:</span>
                <span className="summary-value">{getTotal().toFixed(2)} €</span>
              </div>
              
              <div className="summary-line">
                <span>Frais de service:</span>
                <span className="summary-value">2.50 €</span>
              </div>
              
              <div className="summary-divider"></div>
              
              <div className="summary-line summary-total">
                <span>Total:</span>
                <span className="total-amount">{(getTotal() + 2.50).toFixed(2)} €</span>
              </div>
            </div>

            <button 
              className="btn btn-primary btn-full"
              onClick={() => setShowCheckout(true)}
            >
              Procéder au paiement
            </button>

            <button 
              className="btn btn-outline btn-full"
              onClick={() => navigate('/')}
            >
              Continuer mes achats
            </button>
          </div>

          <div className="cart-info">
            <div className="info-item">
              <span className="info-icon">✅</span>
              <span>Paiement sécurisé</span>
            </div>
            <div className="info-item">
              <span className="info-icon">📧</span>
              <span>Billets par email</span>
            </div>
            <div className="info-item">
              <span className="info-icon">🎫</span>
              <span>E-billets instantanés</span>
            </div>
          </div>
        </div>
      </div>

      {showCheckout && (
        <CheckoutModal 
          cart={cart}
          total={getTotal() + 2.50}
          onClose={() => setShowCheckout(false)}
        />
      )}
    </div>
  );
}

function CheckoutModal({ cart, total, onClose }) {
  const navigate = useNavigate();
  const { clearCart } = useCart();
  const [loading, setLoading] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('card');

  const handlePayment = async () => {
    const user = localStorage.getItem('user');
    
    if (!user) {
      alert('Vous devez être connecté pour réserver');
      navigate('/login');
      return;
    }

    setLoading(true);

    // Simuler le paiement (2 secondes)
    setTimeout(() => {
      setLoading(false);
      clearCart();
      navigate('/confirmation', { 
        state: { 
          success: true,
          orderId: Math.random().toString(36).substr(2, 9).toUpperCase(),
          total: total,
          items: cart
        } 
      });
    }, 2000);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={e => e.stopPropagation()}>
        <button className="modal-close" onClick={onClose}>×</button>
        
        <h2>💳 Paiement</h2>
        
        <div className="payment-summary">
          <p>Montant total: <strong>{total.toFixed(2)} €</strong></p>
          <p>Nombre de billets: <strong>{cart.reduce((sum, item) => sum + item.quantity, 0)}</strong></p>
        </div>

        <div className="payment-methods">
          <h3>Mode de paiement</h3>
          
          <label className={`payment-option ${paymentMethod === 'card' ? 'selected' : ''}`}>
            <input 
              type="radio" 
              name="payment" 
              value="card"
              checked={paymentMethod === 'card'}
              onChange={(e) => setPaymentMethod(e.target.value)}
            />
            <span className="payment-icon">💳</span>
            <span>Carte bancaire</span>
          </label>

          <label className={`payment-option ${paymentMethod === 'paypal' ? 'selected' : ''}`}>
            <input 
              type="radio" 
              name="payment" 
              value="paypal"
              checked={paymentMethod === 'paypal'}
              onChange={(e) => setPaymentMethod(e.target.value)}
            />
            <span className="payment-icon">🅿️</span>
            <span>PayPal</span>
          </label>
        </div>

        {paymentMethod === 'card' && (
          <div className="payment-form">
            <div className="form-group">
              <label>Numéro de carte</label>
              <input type="text" placeholder="1234 5678 9012 3456" maxLength="19" />
            </div>
            
            <div className="form-row">
              <div className="form-group">
                <label>Date d'expiration</label>
                <input type="text" placeholder="MM/AA" maxLength="5" />
              </div>
              
              <div className="form-group">
                <label>CVV</label>
                <input type="text" placeholder="123" maxLength="3" />
              </div>
            </div>
          </div>
        )}

        <button 
          className="btn btn-primary btn-full"
          onClick={handlePayment}
          disabled={loading}
        >
          {loading ? '⏳ Traitement en cours...' : `Payer ${total.toFixed(2)} €`}
        </button>
      </div>
    </div>
  );
}

export default Cart;
