import React from 'react';
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
import SessionForm from '../components/SessionForm';
import InviteForm from '../components/InviteForm';
import RestaurantsSubmission from '../components/RestaurantsSubmission';
import SessionDisplay from '../components/SessionDisplay';
import UserAdd from '../components/UserAdd';
import SessionCreator from '../components/SessionCreator';

/**
 * Router component for managing routing within the application.
 *
 * @returns {ReactElement} The router component.
 */
const Router = () => {
    return (
        <BrowserRouter>
            <nav>
                <ul>
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/add-user">Add User</Link></li>
                    <li><Link to="/invite">Invite</Link></li>
                    <li><Link to="/submit-restaurant">Submit Restaurant</Link></li>
                    <li><Link to="/create-session">Create Session</Link></li>
                    <li><Link to="/view-sessions">View Sessions</Link></li>
                </ul>
            </nav>
            <Routes>
                <Route path="/" element={<SessionForm />} />
                <Route path="/add-user" element={<UserAdd />} />
                <Route path="/invite" element={<InviteForm />} />
                <Route path="/submit-restaurant" element={<RestaurantsSubmission />} />
                <Route path="/create-session" element={<SessionCreator />} />
                <Route path="/view-sessions" element={<SessionDisplay />} />
            </Routes>
        </BrowserRouter>
    );
};

export default Router;
