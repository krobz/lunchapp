import React, { useState } from 'react';
import api from '../services/api';

const RestaurantSubmission = ({ sessionId }) => {
    const [restaurant, setRestaurant] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post(`sessions/${sessionId}/restaurants`, { sessionId, restaurant });
            alert('Restaurant submitted successfully!');
        } catch (error) {
            console.error('Failed to submit restaurant', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={restaurant}
                onChange={(e) => setRestaurant(e.target.value)}
                placeholder="Restaurant Name"
            />
            <button type="submit">Submit Restaurant</button>
        </form>
    );
};

export default RestaurantSubmission;
