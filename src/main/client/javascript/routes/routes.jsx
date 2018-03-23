import React from 'react';
import { IndexRoute, Route, Router } from 'react-router';
import EtherTransactionsPage from '../pages/EtherTransactionsPage';
import ProtectedEnvironmentPage from '../pages/ProtectedEnvironmentPage';
import SendTokenPage from '../pages/SendTokenPage';
import { history } from '../store';
import { initSession, checkUserAuth, composeOnEnterHooks } from './hooks';
import Layout from '../pages/Layout';
import NotFoundPage from '../pages/NotFoundPage';
import SendEtherPage from '../pages/SendEtherPage';
import LandingPage from '../pages/LandingPage';
import ViewAddressPage from '../pages/ViewAddressPage';
import AddAddressPage from '../pages/AddAddressPage';

/**
 * Define frontend routes.
 */
const getRoutes = () => (
  <Router history={history}>
    <Route path="/protected-environment" component={Layout}>
      <IndexRoute component={ProtectedEnvironmentPage}/>
    </Route>
    <Route path="/" component={Layout} onEnter={composeOnEnterHooks(initSession, checkUserAuth)}>
      <IndexRoute component={LandingPage}/>
      <Route path="addresses/add" component={AddAddressPage}/>
      <Route path="addresses/:walletAddress" component={ViewAddressPage}/>
      <Route path="addresses/:walletAddress/transactions/eth" component={EtherTransactionsPage}/>
      <Route path="addresses/:walletAddress/:tokenSymbol/send" component={SendTokenPage}/>
      <Route path="addresses/:walletAddress/send" component={SendEtherPage}/>
      <Route path="*" component={NotFoundPage}/>
    </Route>
  </Router>
);

export default getRoutes;
