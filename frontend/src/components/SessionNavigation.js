import React, { useState, useEffect } from 'react';
import { Route, Routes, Link, useParams, Outlet } from 'react-router-dom';
import InviteForm from './InviteForm';
import RestaurantsSubmission from './RestaurantsSubmission';
import EndSession from './EndSession';
import RestaurantsDisplay from './RestaurantsDisplay';

// Component for individual session navigation
const SessionNavigation = () => {
    const [isCreator, setIsCreator] = useState(false);
    const { id } = useParams();

    useEffect(() => {
        const storedSessionId = localStorage.getItem('createdSessionId');

        // Check if the current session is the one created by the user
        if (storedSessionId === id) {
            setIsCreator(true);
        }
    }, [id]);

    return (
        <>
            {isCreator && (
                <nav>
                    <ul>
                        <li><Link to={`/session/${id}/invite`}>Invite</Link></li>
                        <li><Link to={`/session/${id}/end`}>End Session</Link></li>
                    </ul>
                </nav>
            )}
            <nav>
                <ul>
                    <li><Link to={`/session/${id}/submit-restaurant`}>Submit Restaurant</Link></li>
                    <li><Link to={`/session/${id}/restaurants`}>View Submitted Restaurants</Link></li>
                </ul>
            </nav>
            <Routes>
                <Route path="invite" element={<InviteForm sessionId={id} />} />
                <Route path="submit-restaurant" element={<RestaurantsSubmission sessionId={id} />} />
                <Route path="end" element={<EndSession sessionId={id} />} />
                <Route path="restaurants" element={<RestaurantsDisplay sessionId={id} />} />
            </Routes>
            <Outlet />
        </>
    );
};

export default SessionNavigation;
