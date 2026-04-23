import React, { useState, useEffect } from 'react'
import apiService from '../services/api'

function Statistics() {
  const [stats, setStats] = useState(null)
  const [popularSpectacles, setPopularSpectacles] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    loadStatistics()
  }, [])

  const loadStatistics = async () => {
    try {
      setLoading(true)
      setError(null)
      const [globalStats, popular] = await Promise.all([
        apiService.getGlobalStatistics(),
        apiService.getPopularSpectacles(10)
      ])
      setStats(globalStats)
      setPopularSpectacles(popular)
    } catch (err) {
      setError('Erreur lors du chargement des statistiques')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <div className="loading">Chargement des statistiques...</div>
  }

  if (error) {
    return (
      <div className="error">
        <p>{error}</p>
        <button className="btn btn-primary" onClick={loadStatistics}>
          Réessayer
        </button>
      </div>
    )
  }

  return (
    <div>
      <h2 style={{ marginBottom: '2rem' }}>📊 Statistiques globales</h2>
      
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <h3>{stats.totalSpectacles || 0}</h3>
            <p>Spectacles</p>
          </div>
          
          <div className="stat-card">
            <h3>{stats.totalReservations || 0}</h3>
            <p>Réservations</p>
          </div>
          
          <div className="stat-card">
            <h3>{stats.totalBillets || 0}</h3>
            <p>Billets vendus</p>
          </div>
          
          <div className="stat-card">
            <h3>{stats.chiffreAffairesTotal ? `${stats.chiffreAffairesTotal.toFixed(2)}€` : '0€'}</h3>
            <p>Chiffre d'affaires</p>
          </div>
          
          <div className="stat-card">
            <h3>{stats.tauxRemplissageMoyen ? `${stats.tauxRemplissageMoyen.toFixed(1)}%` : '0%'}</h3>
            <p>Taux de remplissage moyen</p>
          </div>
        </div>
      )}
      
      <h2 style={{ marginTop: '3rem', marginBottom: '2rem' }}>🔥 Spectacles populaires</h2>
      
      {popularSpectacles.length === 0 ? (
        <p style={{ textAlign: 'center', color: '#666', padding: '2rem' }}>
          Aucune donnée disponible
        </p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {popularSpectacles.map((spectacle, index) => (
            <div key={index} className="card">
              <div style={{ 
                display: 'flex', 
                justifyContent: 'space-between',
                alignItems: 'center'
              }}>
                <div>
                  <h3 style={{ color: '#667eea', marginBottom: '0.5rem' }}>
                    #{index + 1} - {spectacle.titre}
                  </h3>
                  <div style={{ display: 'flex', gap: '2rem', color: '#666' }}>
                    <span>💰 {spectacle.chiffreAffaires ? `${spectacle.chiffreAffaires.toFixed(2)}€` : '0€'}</span>
                    <span>🎫 {spectacle.nombreBillets || 0} billets</span>
                    <span>📊 {spectacle.tauxRemplissage ? `${spectacle.tauxRemplissage.toFixed(1)}%` : '0%'} rempli</span>
                  </div>
                </div>
                <div style={{
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                  width: '50px',
                  height: '50px',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '1.5rem',
                  fontWeight: 'bold'
                }}>
                  {index + 1}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default Statistics
