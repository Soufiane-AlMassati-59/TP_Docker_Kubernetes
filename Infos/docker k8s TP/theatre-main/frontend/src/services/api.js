const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
  // Récupérer le token d'authentification
  getAuthToken() {
    return localStorage.getItem('token');
  }

  // Créer les headers avec authentification
  getHeaders() {
    const headers = {
      'Content-Type': 'application/json',
    };
    
    const token = this.getAuthToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    return headers;
  }

  async get(endpoint) {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: this.getHeaders(),
      });
      if (!response.ok) {
        if (response.status === 401) {
          // Token expiré, déconnexion
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  }

  async post(endpoint, data) {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: this.getHeaders(),
        body: JSON.stringify(data),
      });
      if (!response.ok) {
        if (response.status === 401) {
          // Token expiré, déconnexion
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  }

  // Spectacles
  async getAllSpectacles() {
    return this.get('/spectacles');
  }

  async getSpectacleById(id) {
    return this.get(`/spectacles/${id}`);
  }

  async getUpcomingSpectacles() {
    return this.get('/spectacles/upcoming');
  }

  async searchSpectacles(query) {
    return this.get(`/spectacles/search?query=${encodeURIComponent(query)}`);
  }

  // Statistics
  async getGlobalStatistics() {
    return this.get('/statistics/global');
  }

  async getPopularSpectacles(limit = 5) {
    return this.get(`/statistics/popular?limit=${limit}`);
  }

  // Reservations
  async createReservation(data) {
    return this.post('/reservations', data);
  }

  async getMyReservations() {
    return this.get('/reservations/mes-reservations');
  }
}

export default new ApiService();
