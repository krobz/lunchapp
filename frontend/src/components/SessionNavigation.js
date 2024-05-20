import React, { useState, useEffect } from 'react';
import { Route, Routes, Link, useParams, Outlet } from 'react-router-dom';
import InviteForm from './InviteForm';
import RestaurantsSubmission from './RestaurantsSubmission';
import EndSession from './EndSession';
import RestaurantsDisplay from './RestaurantsDisplay';

const SessionNavigation = () => {
    const [isCreator, setIsCreator] = useState(false);
    const [sessionEnded, setSessionEnded] = useState(false);
    const { id } = useParams();

    useEffect(() => {
        const storedSessionId = localStorage.getItem('createdSessionId');
        if (storedSessionId === id) {
            setIsCreator(true);
        }
    }, [id]);

    const handleEndSession = () => {
        setSessionEnded(true);
    };

    return (
        <>
            {isCreator && (
                <nav>
                    <ul>
                        <li><Link to={`/session/${id}/invite`}>Invite</Link></li>
                        <li><Link to={`/session/${id}/end`} onClick={handleEndSession}>End Session</Link></li>
                    </ul>
                </nav>
            )}
            <nav>
                <ul>
                    <li><Link to={`/session/${id}/submit-restaurant`}>Submit Restaurant</Link></li>
                    {!sessionEnded && <li><Link to={`/session/${id}/restaurants`}>View Submitted Restaurants</Link></li>}
                </ul>
            </nav>
            <Routes>
                <Route path="invite" element={<InviteForm sessionId={id} />} />
                <Route path="submit-restaurant" element={<RestaurantsSubmission sessionId={id} />} />
                <Route path="end" element={<EndSession sessionId={id} onEndSession={handleEndSession} />} />
                <Route path="restaurants" element={<RestaurantsDisplay sessionId={id} />} />
            </Routes>
            <Outlet />
        </>
    );
};

export default SessionNavigation;
