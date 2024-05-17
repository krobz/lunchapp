import React from 'react';
import api from '../services/api';

/**
 * Represents the component for ending a session.
 *
 * @param {Object} props - The component properties.
 * @param {string} props.sessionId - The session ID.
 * @returns {JSX.Element} The EndSession component.
 */
const EndSession = ({ sessionId }) => {
    const handleEndSession = async () => {
        try {
            const userId = localStorage.getItem('userId'); // Fetch the ID of the user from local storage
            const endSessionRequest = { userId };

            const response = await api.post(`/sessions/${sessionId}/end`, endSessionRequest);

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
    };

    return (
        <button onClick={handleEndSession}>End Session</button>
    );
};

export default EndSession;
