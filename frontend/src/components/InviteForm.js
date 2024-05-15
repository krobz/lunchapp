import React, { useState } from 'react';
import api from '../services/api';

/**
 * Represents a form for inviting a user to a session.
 *
 * @param {Object} props - The properties for the InviteForm component.
 * @param {string} props.sessionId - The ID of the session to invite the user to.
 *
 * @returns {JSX.Element} The InviteForm component.
 */
const InviteForm = ({ sessionId }) => {
    const [inviteeName, setInviteeName] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Fetch the ID of the invitee using their name
            const userResponse = await api.get(`/name/${inviteeName}`);
            const inviteeId = userResponse.data.uuid;

            const inviterId = localStorage.getItem('userId');

            // Construct the invite request
            const inviteUsersRequest = { inviterId, inviteeId };

            // Call the invite API endpoint
            await api.post(`sessions/${sessionId}/invite`, inviteUsersRequest);

            alert('User invited successfully!');
        } catch (error) {
            console.error('Failed to invite user', error);
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