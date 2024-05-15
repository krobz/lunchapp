import React from 'react';
import { useDispatch } from 'react-redux';
import api from '../services/api';

/**
 * Represents a form component for creating a session.
 * @constructor
 */
const SessionForm = () => {
    const dispatch = useDispatch();
    const userId = localStorage.getItem('userId');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('sessions/create', { creatorId: userId });
            // Storing session id in local storage
            localStorage.setItem('sessionId', response.data.id);

            dispatch({
                type: 'CREATE_SESSION',
                payload: response.data,
            });
        } catch (error) {
            console.error('Failed to create session', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <button type="submit">Create Session</button>
        </form>
    );
};

export default SessionForm;