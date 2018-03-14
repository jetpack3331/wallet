import React from 'react';
import { IndexRoute, Route, Router } from 'react-router';
import { history } from '../store';
import { initSession } from './hooks';
import Layout from '../pages/Layout';
import NotFoundPage from '../pages/NotFoundPage';
import SendEtherPage from '../pages/SendEtherPage';
import LandingPage from '../pages/LandingPage';
import WalletPage from '../pages/WalletPage';
import AddAddressPage from '../pages/AddAddressPage';

/**
 * Define frontend routes.
 */
const getRoutes = () => (
  <Router history={history}>
    <Route path="/" component={Layout} onEnter={initSession}>
      <IndexRoute component={LandingPage}/>
      <Route path="addAddress" component={AddAddressPage}/>
      <Route path="addresses/:walletAddress" component={WalletPage}/>
      <Route path="addresses/:walletAddress/send" component={SendEtherPage}/>
      <Route path="*" component={NotFoundPage}/>
    </Route>
  </Router>
);

export default getRoutes;
