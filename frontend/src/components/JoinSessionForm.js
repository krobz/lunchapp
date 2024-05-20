import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const JoinSessionForm = () => {
    const [sessionId, setSessionId] = useState('');
    const navigate = useNavigate();

    const handleJoinSession = async (event) => {
        event.preventDefault();
        if (!sessionId) {
            alert('Please enter a session ID.');
            return;
        }
        try {
            await api.post(`/api/joinSession?sessionId=${sessionId}`);
            alert('Successfully joined the session!');
            navigate(`/session/${sessionId}`);
        } catch (error) {
            console.error('Failed to join the session:', error.response ? error.response.data : 'Unknown error');
            alert('Failed to join the session. User may not in the session. Please check the session ID and try again.');
        }
    };

    return (
        <div>
            <h1>Join a Session</h1>
            <form onSubmit={handleJoinSession}>
                <label>
                    Session ID:
                    <input
                        type="text"
                        value={sessionId}
                        onChange={e => setSessionId(e.target.value)}
                        placeholder="Enter session ID"
                    />
                </label>
                <button type="submit">Join Session</button>
            </form>
        </div>
    );
};

export default JoinSessionForm;
