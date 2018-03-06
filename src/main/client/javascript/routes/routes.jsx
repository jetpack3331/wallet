import React from 'react';
import { IndexRoute, Route, Router } from 'react-router';
import Layout from '../pages/Layout';
import NotFoundPage from '../pages/NotFoundPage';
import { history } from '../store';
import { initSession } from './hooks';
import LandingPage from '../pages/LandingPage';

/**
 * Define frontend routes.
 */
const getRoutes = () => (
  <Router history={history}>
    <Route path="/" component={Layout} onEnter={initSession}>
      <IndexRoute component={LandingPage}/>
      <Route path="*" component={NotFoundPage}/>
    </Route>
  </Router>
);

export default getRoutes;
