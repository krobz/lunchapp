import axios from 'axios';

const API_KEY = process.env.REACT_APP_API_KEY;
const API_URL = 'http://localhost:8080'; // Base URL for API

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'X-API-KEY': API_KEY,
        'Content-Type': 'application/json'
    },
    withCredentials: true
});

// ensure that
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('jwt');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default api;


