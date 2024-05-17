import React, { useState, useEffect } from 'react';
import api from '../services/api';

/**
 * Represents the component for displaying submitted restaurants.
 *
 * @param {Object} props - The component properties.
 * @param {string} props.sessionId - The session ID.
 * @returns {JSX.Element} The RestaurantsDisplay component.
 */
const RestaurantsDisplay = ({ sessionId }) => {
    const [restaurants, setRestaurants] = useState([]);

    useEffect(() => {
        const fetchRestaurants = async () => {
            try {
                const response = await api.get(`/sessions/${sessionId}`);
                setRestaurants(response.data.restaurants);
            } catch (error) {
                console.error('Failed to fetch restaurants', error);
            }
        };

        fetchRestaurants();
    }, [sessionId]);

    return (
        <div>
            <h3>Submitted Restaurants</h3>
            <ul>
                {restaurants.map(restaurant => (
                    <li key={restaurant.id}>{restaurant.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default RestaurantsDisplay;
