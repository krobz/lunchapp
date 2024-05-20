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
    const [restaurantName, setRestaurantName] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!restaurantName) {
            alert('Please enter a restaurant name.');
            return;
        }
        try {
            const addRestaurantRequest = { restaurantName };
            await api.post(`sessions/${sessionId}/restaurants`, addRestaurantRequest);
            alert('Restaurant submitted successfully!');
            setRestaurantName('');  // Clear the input after successful submission
        } catch (error) {
            console.error('Failed to submit restaurant:', error.response ? error.response.data : 'Unknown error');
            alert('Failed to submit restaurant. Please try again.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={restaurantName}
                onChange={(e) => setRestaurantName(e.target.value)}
                placeholder="Restaurant Name"
            />
            <button type="submit">Submit Restaurant</button>
        </form>
    );
};

export default RestaurantSubmission;
