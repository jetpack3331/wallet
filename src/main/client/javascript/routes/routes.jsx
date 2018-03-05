import React from 'react';
import { IndexRoute, Route, Router } from 'react-router';
import HomePage from '../pages/HomePage';
import Layout from '../pages/Layout';
import NotFoundPage from '../pages/NotFoundPage';
import { history } from '../store';

/**
 * Define frontend routes.
 */
const getRoutes = () => (
  <Router history={history}>
    <Route path="/" component={Layout}>
      <IndexRoute component={HomePage} />
      <Route path="*" component={NotFoundPage} />
    </Route>
  </Router>
);

export default getRoutes;
