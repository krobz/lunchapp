import React from 'react';
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
import SessionDisplay from '../components/SessionDisplay';
import UserAdd from '../components/UserAdd';
import SessionCreator from '../components/SessionCreator';
import SessionNavigation from '../components/SessionNavigation';

// Router component for managing routing within the application.
const Router = () => {
    return (
        <BrowserRouter>
            <nav>
                <ul>
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/add-user">Add User</Link></li>
                    <li><Link to="/view-sessions">View Sessions</Link></li>
                    <li><Link to="/create-session">Create Session</Link></li>
                </ul>
            </nav>
            <Routes>
                <Route path="/add-user" element={<UserAdd />} />
                <Route path="/view-sessions/*" element={<SessionDisplay />} />
                <Route path="/create-session" element={<SessionCreator />} />
                <Route path="/session/:id/*" element={<SessionNavigation />} />
            </Routes>
        </BrowserRouter>
    );
};

export default Router;