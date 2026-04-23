import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { useCart } from '../context/CartContext';
import './Header.css';

function Header() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const { getItemCount } = useCart();
  const cartCount = getItemCount();

  useEffect(() => {
    // Charger l'utilisateur depuis localStorage
    const userData = localStorage.getItem('user');
    if (userData) {
      try {
        setUser(JSON.parse(userData));
      } catch (e) {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
      }
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
    navigate('/');
    window.location.reload();
  };

  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="logo">
          🎭 Théâtre
        </Link>

        <nav className="nav-menu">
          <Link to="/" className="nav-link">Spectacles</Link>
          <Link to="/statistics" className="nav-link">Statistiques</Link>
          <Link to="/cart" className="nav-link cart-link">
            🛒 Panier
            {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
          </Link>
        </nav>

        <div className="auth-section">
          {user ? (
            <div className="user-menu">
              <span className="user-name">
                👤 {user.prenom} {user.nom}
              </span>
              <button onClick={handleLogout} className="btn btn-outline">
                Déconnexion
              </button>
            </div>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="btn btn-outline">
                Connexion
              </Link>
              <Link to="/register" className="btn btn-primary">
                Inscription
              </Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}

export default Header;
