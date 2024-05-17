import React, { useState } from 'react';
import api from '../services/api';

/**
 * Represents a component that allows users to submit a restaurant.
 *
 * @param {Object} props - The component properties.
 * @param {string} props.sessionId - The session ID.
 * @returns {JSX.Element} - The component JSX.
 */
const RestaurantSubmission = ({ sessionId }) => {
    const [restaurantName, setRestaurant] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userId = localStorage.getItem('userId'); // userId is stored in local storage
            if (userId) {
                const addRestaurantRequest = {
                    userId,
                    restaurantName,
                };
                await api.post(`sessions/${sessionId}/restaurants`, addRestaurantRequest);
                alert('Restaurant submitted successfully!');
            } else {
                alert('User ID not found, please log in');
            }
        } catch (error) {
            console.error('Failed to submit restaurant', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={restaurantName}
                onChange={(e) => setRestaurant(e.target.value)}
                placeholder="Restaurant Name"
            />
            <button type="submit">Submit Restaurant</button>
        </form>
    );
};

export default RestaurantSubmission;
