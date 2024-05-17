import React from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const SessionCreator = () => {
    const navigate = useNavigate();

    const handleSubmit = async event => {
        event.preventDefault();
        const creatorId = localStorage.getItem('userId');
        try {
            // Construct the creation request
            const creationRequest = { creatorId };

            const response = await api.post(`sessions/create`, null, { params: creationRequest });

            // Store the created session ID in localStorage
            const sessionId = response.data.id;
            localStorage.setItem('createdSessionId', sessionId);

            // Navigate to the session's page
            navigate(`/session/${sessionId}`);

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
            <button type="submit">Create Session</button>
        </form>
    );
};

export default SessionCreator;
