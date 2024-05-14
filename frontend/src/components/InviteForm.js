import React, { useState, useEffect } from 'react';
import api from '../services/api';

const InviteForm = ({ sessionId }) => {
    const [userId, setUserId] = useState('');
    const [userList, setUserList] = useState([]);

    useEffect(() => {
        const getUsers = async () => {
            try {
                const response = await api.get('/users'); // API endpoint to get all users
                setUserList(response.data);
            } catch (error) {
                console.error('Failed to fetch users', error);
            }
        };
        getUsers();
    }, []);

    /**
     * Handles inviting a user
     *
     * @returns {Promise} - A promise that resolves when the user is invited successfully or rejects with an error
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post(`/${sessionId}/invite`, { sessionId, userId });
            alert('User invited successfully!');
        } catch (error) {
            console.error('Failed to invite user', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <select value={userId} onChange={(e) => setUserId(e.target.value)}>
                {userList.map((user) => (
                    <option key={user.uuid} value={user.uuid}>
                        {user.name}
                    </option>
                ))}
            </select>
            <button type="submit">Invite User</button>
        </form>
    );
};

export default InviteForm;