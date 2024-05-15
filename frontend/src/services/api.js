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

export default api;

