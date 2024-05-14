import React, { useEffect, useState } from 'react';
import api from '../services/api';

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
                </div>
            ))}
        </div>
    );
};

export default SessionDisplay;
