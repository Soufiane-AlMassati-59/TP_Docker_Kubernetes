import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { CartProvider } from './context/CartContext'
import Header from './components/Header'
import SpectaclesList from './components/SpectaclesList'
import SpectacleDetail from './components/SpectacleDetail'
import Statistics from './components/Statistics'
import Login from './components/Login'
import Register from './components/Register'
import Cart from './components/Cart'
import Confirmation from './components/Confirmation'
import './App.css'

function App() {
  return (
    <CartProvider>
      <Router>
        <div className="app">
          <Header />
          
          <main className="main">
            <Routes>
              <Route path="/" element={<SpectaclesList />} />
              <Route path="/spectacle/:id" element={<SpectacleDetail />} />
              <Route path="/statistics" element={<Statistics />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/cart" element={<Cart />} />
              <Route path="/confirmation" element={<Confirmation />} />
            </Routes>
          </main>
          
          <footer className="footer">
            <div className="container">
              <p>© 2025 Plateforme de Billetterie - M1 DevOps</p>
            </div>
          </footer>
        </div>
      </Router>
    </CartProvider>
  )
}

export default App
