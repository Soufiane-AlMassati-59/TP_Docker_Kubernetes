import { createContext, useContext, useState, useEffect } from 'react';

const CartContext = createContext();

export function CartProvider({ children }) {
  const [cart, setCart] = useState(() => {
    // Charger le panier depuis localStorage au démarrage
    const savedCart = localStorage.getItem('cart');
    return savedCart ? JSON.parse(savedCart) : [];
  });

  // Sauvegarder le panier dans localStorage à chaque modification
  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(cart));
  }, [cart]);

  // Ajouter un billet au panier
  const addToCart = (spectacle, quantity = 1) => {
    setCart(prevCart => {
      const existingItem = prevCart.find(item => item.spectacle.id === spectacle.id);
      
      if (existingItem) {
        // Si le spectacle existe déjà, augmenter la quantité
        return prevCart.map(item =>
          item.spectacle.id === spectacle.id
            ? { ...item, quantity: item.quantity + quantity }
            : item
        );
      } else {
        // Sinon, ajouter un nouvel item
        return [...prevCart, { spectacle, quantity }];
      }
    });
  };

  // Retirer un billet du panier
  const removeFromCart = (spectacleId) => {
    setCart(prevCart => prevCart.filter(item => item.spectacle.id !== spectacleId));
  };

  // Mettre à jour la quantité
  const updateQuantity = (spectacleId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(spectacleId);
      return;
    }
    
    setCart(prevCart =>
      prevCart.map(item =>
        item.spectacle.id === spectacleId
          ? { ...item, quantity }
          : item
      )
    );
  };

  // Vider le panier
  const clearCart = () => {
    setCart([]);
  };

  // Calculer le total
  const getTotal = () => {
    return cart.reduce((total, item) => total + (Number(item.spectacle.prixUnitaire) * item.quantity), 0);
  };

  // Nombre total d'articles
  const getItemCount = () => {
    return cart.reduce((count, item) => count + item.quantity, 0);
  };

  const value = {
    cart,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getTotal,
    getItemCount
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
}
