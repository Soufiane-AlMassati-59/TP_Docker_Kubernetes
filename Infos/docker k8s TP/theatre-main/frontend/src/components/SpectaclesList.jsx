import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { useCart } from '../context/CartContext'
import apiService from '../services/api'
import './SpectaclesList.css'

function SpectaclesList() {
  const [spectacles, setSpectacles] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [searchQuery, setSearchQuery] = useState('')
  const [addedToCart, setAddedToCart] = useState({})
  const { addToCart } = useCart()

  useEffect(() => {
    loadSpectacles()
  }, [])

  const loadSpectacles = async () => {
    try {
      setLoading(true)
      setError(null)
      const data = await apiService.getAllSpectacles()
      setSpectacles(data)
    } catch (err) {
      setError('Erreur lors du chargement des spectacles')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!searchQuery.trim()) {
      loadSpectacles()
      return
    }
    
    try {
      setLoading(true)
      setError(null)
      const data = await apiService.searchSpectacles(searchQuery)
      setSpectacles(data)
    } catch (err) {
      setError('Erreur lors de la recherche')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleAddToCart = (e, spectacle) => {
    e.preventDefault() // Empêcher la navigation
    e.stopPropagation()
    addToCart(spectacle, 1)
    
    // Animation de confirmation
    setAddedToCart(prev => ({ ...prev, [spectacle.id_spectacle]: true }))
    setTimeout(() => {
      setAddedToCart(prev => ({ ...prev, [spectacle.id_spectacle]: false }))
    }, 2000)
  }

  if (loading) {
    return <div className="loading">Chargement des spectacles...</div>
  }

  if (error) {
    return (
      <div className="error">
        <p>{error}</p>
        <button className="btn btn-primary" onClick={loadSpectacles}>
          Réessayer
        </button>
      </div>
    )
  }

  return (
    <div className="spectacles-container">
      <div className="spectacles-header">
        <h1>🎭 Découvrez nos spectacles</h1>
        <p className="subtitle">Réservez vos places pour les meilleurs spectacles</p>
        
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Rechercher un spectacle, un genre..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="search-input"
          />
          <button type="submit" className="btn btn-primary">
            🔍 Rechercher
          </button>
          {searchQuery && (
            <button 
              type="button" 
              className="btn btn-outline"
              onClick={() => {
                setSearchQuery('')
                loadSpectacles()
              }}
            >
              ✕ Réinitialiser
            </button>
          )}
        </form>
      </div>

      {spectacles.length === 0 ? (
        <div className="no-results">
          <div className="no-results-icon">🎭</div>
          <h3>Aucun spectacle trouvé</h3>
          <p>Essayez une autre recherche ou parcourez tous nos spectacles</p>
        </div>
      ) : (
        <div className="spectacles-grid">
          {spectacles.map((spectacle) => (
            <div key={spectacle.id} className="spectacle-card">
              <Link 
                to={`/spectacle/${spectacle.id}`}
                className="spectacle-link"
              >
                <div className="spectacle-image">
                  <img 
                    src={spectacle.imageUrl || 'https://images.unsplash.com/photo-1503095396549-807759245b35?w=400'} 
                    alt={spectacle.titre}
                  />
                  <div className="spectacle-genre">{spectacle.genre || 'Spectacle'}</div>
                </div>
                
                <div className="spectacle-content">
                  <h3 className="spectacle-title">{spectacle.titre}</h3>
                  <p className="spectacle-description">
                    {spectacle.description?.substring(0, 100)}...
                  </p>
                  
                  <div className="spectacle-info">
                    <div className="info-item">
                      <span className="info-icon">📅</span>
                      <span>{new Date(spectacle.dateSpectacle).toLocaleDateString('fr-FR', {
                        day: 'numeric',
                        month: 'long'
                      })}</span>
                    </div>
                    <div className="info-item">
                      <span className="info-icon">🕐</span>
                      <span>{spectacle.heureDebut}</span>
                    </div>
                    <div className="info-item">
                      <span className="info-icon">⏱️</span>
                      <span>{spectacle.dureeMinutes || 120} min</span>
                    </div>
                  </div>

                  <div className="spectacle-availability">
                    <div className="availability-bar">
                      <div 
                        className="availability-fill"
                        style={{
                          width: `${((spectacle.placesTotales - spectacle.placesDisponibles) / spectacle.placesTotales) * 100}%`
                        }}
                      />
                    </div>
                    <span className="availability-text">
                      {spectacle.placesDisponibles} places disponibles
                    </span>
                  </div>
                </div>
              </Link>

              <div className="spectacle-footer">
                <div className="spectacle-price">
                  <span className="price-label">À partir de</span>
                  <span className="price-amount">{Number(spectacle.prixUnitaire).toFixed(2)} €</span>
                </div>
                
                <button 
                  className={`btn-add-cart ${addedToCart[spectacle.id] ? 'added' : ''}`}
                  onClick={(e) => handleAddToCart(e, spectacle)}
                  disabled={spectacle.placesDisponibles === 0}
                >
                  {addedToCart[spectacle.id] ? '✓ Ajouté !' : '🛒 Ajouter'}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default SpectaclesList
