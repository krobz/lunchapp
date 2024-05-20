import React, { useState } from 'react';
import api from '../services/api';

const InviteForm = ({ sessionId }) => {
    const [inviteeName, setInviteeName] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userResponse = await api.get(`users/name/${inviteeName}`);
            const inviteeId = userResponse.data.id;

            const response = await api.post(`sessions/${sessionId}/invite`, { inviteeId });

            alert(`User invited successfully! SessionId: ${response.data}`);
        } catch (error) {
            console.error('Failed to invite user', error);
            alert('Failed to invite user');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Enter the user's name"
                value={inviteeName}
                onChange={(e) => setInviteeName(e.target.value)}
            />
            <button type="submit">Invite User</button>
        </form>
    );
};

export default InviteForm;
