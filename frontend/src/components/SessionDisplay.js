import React, { useState, useEffect } from 'react';
import api from '../services/api';

/**
 * Represents a component for displaying session information and handling session actions.
 *
 * @returns {JSX.Element} The rendered session display component
 */
const SessionDisplay = () => {
    const [sessions, setSessions] = useState([]);

    useEffect(() => {
        const fetchSessions = async () => {
            try {
                const response = await api.get('/sessions');
                setSessions(response.data);
            } catch (error) {
                console.error('Failed to fetch sessions', error);
            }
        };

        fetchSessions();
    }, []);

    // Event handler for ending the session
    const handleEndSession = async (sessionId) => {
        try {
            const userId = localStorage.getItem('userId'); // Fetch the ID of the user from local storage
            const endSessionRequest = { userId: userId };

            const response = await api.post(`sessions/${sessionId}/end`, endSessionRequest);

            if (response.data && response.data.name) {
                alert('Session ended successfully. The chosen restaurant is: ' + response.data.name);
            } else {
                alert('Session ended successfully. No restaurant was selected.');
            }

        } catch (error) {
            console.error('Failed to end the session', error);
            if (error.response && error.response.data) {
                alert('Failed to end the session: ' + error.response.data);
            } else {
                alert('Failed to end the session. Please try again.');
            }
        }
    }

    return (
        <div>
            {sessions.map((session) => (
                <div key={session.id}>
                    <h3>Session ID: {session.id}</h3>
                    <p>Creator: {session.creatorId}</p>
                    <ul>
                        {session.restaurants.map(restaurant => (
                            <li key={restaurant.id}>{restaurant.name}</li>
                        ))}
                    </ul>
                    <button onClick={() => handleEndSession(session.id)}>End Session</button>
                </div>
            ))}
        </div>
    );
}

export default SessionDisplay;