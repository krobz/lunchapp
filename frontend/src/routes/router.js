import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import SessionForm from '../components/SessionForm';
import InviteForm from '../components/InviteForm';
import RestaurantSubmission from '../components/RestaurantSubmission';
import SessionDisplay from '../components/SessionDisplay';

const Router = () => {
    return (
        <BrowserRouter>
            <Switch>
                <Route path="/" exact component={SessionForm} />
                <Route path="/invite" component={InviteForm} />
                <Route path="/submit-restaurant" component={RestaurantSubmission} />
                <Route path="/view-sessions" component={SessionDisplay} />
            </Switch>
        </BrowserRouter>
    );
};

export default Router;
