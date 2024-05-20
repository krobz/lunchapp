import React from 'react';
import api from '../services/api';

const EndSession = ({ sessionId, onEndSession }) => {
    const handleEndSession = async () => {
        try {
            const response = await api.post(`/sessions/${sessionId}/end`);

            if (response.data) {
                alert('Session ended successfully. The chosen restaurant is: ' + response.data);
            } else {
                alert('Session ended successfully. No restaurant was selected.');
            }
            onEndSession();
        } catch (error) {
            console.error('Failed to end the session', error);
            alert('Failed to end the session: ' + (error.response && error.response.data ? error.response.data : 'Please try again.'));
        }
    };

    return (
        <button onClick={handleEndSession}>End Session</button>
    );
};

export default EndSession;

