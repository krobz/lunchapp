import React from 'react';
import { Provider } from 'react-redux';
import store from './redux/store';
import Router from './routes/Router';

const App = () => {
  return (
      <Provider store={store}>
        <div>
          <h1>Session Management Application</h1>
          <Router />
        </div>
      </Provider>
  );
};

export default App;
