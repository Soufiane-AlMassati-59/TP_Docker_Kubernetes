import React, { useState, useEffect } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'
import apiService from '../services/api'
import { useCart } from '../context/CartContext'

function SpectacleDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { addToCart } = useCart()
  const [spectacle, setSpectacle] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [quantity, setQuantity] = useState(1)
  const [addedToCart, setAddedToCart] = useState(false)

  useEffect(() => {
    loadSpectacle()
  }, [id])

  const loadSpectacle = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await apiService.getSpectacleById(id)
      setSpectacle(data)
    } catch (err) {
      setError('Erreur lors du chargement du spectacle')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <div className="loading">Chargement...</div>
  }

  if (error) {
    return (
      <div className="error">
        <p>{error}</p>
        <Link to="/" className="btn btn-primary">
          Retour à la liste
        </Link>
      </div>
    )
  }

  if (!spectacle) {
    return <div>Spectacle non trouvé</div>
  }

  const handleAddToCart = () => {
    addToCart(spectacle, quantity)
    setAddedToCart(true)
    setTimeout(() => setAddedToCart(false), 2000)
  }

  const handleReserveAndGoToCart = () => {
    addToCart(spectacle, quantity)
    navigate('/cart')
  }

  return (
    <div>
      <Link to="/" style={{ 
        display: 'inline-block', 
        marginBottom: '2rem',
        color: '#667eea',
        textDecoration: 'none'
      }}>
        ← Retour à la liste
      </Link>
      
      <div className="card" style={{ maxWidth: '800px', margin: '0 auto' }}>
        <h1 style={{ color: '#667eea', marginBottom: '1rem' }}>
          {spectacle.titre}
        </h1>
        
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: '1fr 1fr',
          gap: '2rem',
          marginTop: '2rem'
        }}>
          <div>
            <h3 style={{ marginBottom: '1rem' }}>📋 Description</h3>
            <p style={{ color: '#666', lineHeight: '1.6' }}>
              {spectacle.description}
            </p>
          </div>
          
          <div>
            <h3 style={{ marginBottom: '1rem' }}>ℹ️ Informations</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
              <div>
                <strong>Date et heure:</strong>
                <br />
                {spectacle.dateSpectacle && new Date(spectacle.dateSpectacle).toLocaleDateString('fr-FR', {
                  weekday: 'long',
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric'
                })}
                <br />
                {spectacle.heureDebut}
              </div>
              
              <div>
                <strong>Durée:</strong> {spectacle.dureeMinutes || spectacle.duree || 'N/A'} minutes
              </div>
              
              <div>
                <strong>Prix:</strong> {Number(spectacle.prixUnitaire).toFixed(2)}€
              </div>
              
              <div>
                <strong>Places disponibles:</strong> {spectacle.placesDisponibles}
              </div>
              
              {spectacle.tauxRemplissage !== undefined && (
                <div>
                  <strong>Taux de remplissage:</strong> {spectacle.tauxRemplissage.toFixed(1)}%
                  <div style={{
                    background: '#e2e8f0',
                    borderRadius: '999px',
                    height: '12px',
                    overflow: 'hidden',
                    marginTop: '0.5rem'
                  }}>
                    <div style={{
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      height: '100%',
                      width: `${spectacle.tauxRemplissage}%`,
                      transition: 'width 0.3s'
                    }} />
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
        
        <div style={{ 
          marginTop: '3rem', 
          paddingTop: '2rem', 
          borderTop: '1px solid #eee',
          textAlign: 'center'
        }}>
          {spectacle.placesDisponibles > 0 && (
            <div style={{ marginBottom: '1.5rem' }}>
              <label style={{ marginRight: '1rem', fontSize: '1.1rem' }}>
                <strong>Nombre de billets:</strong>
              </label>
              <div style={{ display: 'inline-flex', alignItems: 'center', gap: '1rem' }}>
                <button 
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  className="btn"
                  style={{ padding: '0.5rem 1rem' }}
                >
                  −
                </button>
                <span style={{ fontSize: '1.5rem', fontWeight: 'bold', minWidth: '3rem', textAlign: 'center' }}>
                  {quantity}
                </span>
                <button 
                  onClick={() => setQuantity(Math.min(spectacle.placesDisponibles, quantity + 1))}
                  className="btn"
                  style={{ padding: '0.5rem 1rem' }}
                  disabled={quantity >= spectacle.placesDisponibles}
                >
                  +
                </button>
              </div>
            </div>
          )}
          
          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
            <button 
              className="btn btn-primary"
              style={{ fontSize: '1.1rem', padding: '1rem 2rem' }}
              disabled={spectacle.placesDisponibles === 0}
              onClick={handleAddToCart}
            >
              {addedToCart ? '✓ Ajouté au panier !' : spectacle.placesDisponibles === 0 ? 'Complet' : '🛒 Ajouter au panier'}
            </button>
            
            <button 
              className="btn"
              style={{ 
                fontSize: '1.1rem', 
                padding: '1rem 2rem',
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                color: 'white',
                border: 'none'
              }}
              disabled={spectacle.placesDisponibles === 0}
              onClick={handleReserveAndGoToCart}
            >
              {spectacle.placesDisponibles === 0 ? 'Complet' : 'Réserver maintenant'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default SpectacleDetail
