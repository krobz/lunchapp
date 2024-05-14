import React from 'react';
import { useDispatch } from 'react-redux';
import api from '../services/api';
import { useUserId } from '../hooks/useUserId';

const SessionForm = () => {
    const dispatch = useDispatch();
    const userId = useUserId();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/create', { creatorId: userId });
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