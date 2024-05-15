import React, { useState } from 'react';
import api from '../services/api';

/**
 * Represents a function component that creates a session.
 *
 * @returns {JSX.Element} - The rendered form for creating a session.
 */
const SessionCreator = () => {
    const [creatorId, setCreatorId] = useState('');

    const handleSubmit = async event => {
        event.preventDefault();
        try {
            // Construct the creation request
            const creationRequest = { creatorId };

            // Call the create API endpoint
            const response = await api.post(`sessions/create`, null, { params : creationRequest });

            alert('Session created successfully!');
        } catch (error) {
            console.error('Failed to create a session', error);
            if (error.response && error.response.data) {
                alert('Failed to create a session: ' + error.response.data);
            } else {
                alert('Failed to create a session. Please try again.');
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Enter the Creator ID"
                value={creatorId}
                onChange={event => setCreatorId(event.target.value)}
            />
            <button type="submit">Create Session</button>
        </form>
    );
};

export default SessionCreator;