import { routerReducer as routing } from 'react-router-redux';
import { combineReducers } from 'redux';
import { reducer as form } from 'redux-form';
import auth, * as fromAuth from './auth';
import walletAccounts, * as fromWalletAccounts from './walletAccounts';
import users from './users';

/**
 * Root reducer for the app.
 */
const rootReducer = combineReducers({
  form,
  routing,
  auth,
  users,
  walletAccounts,
});

export const getLoggedInUser = state =>
  fromAuth.getUser(state.auth);

export const getIsAuthenticated = state =>
  fromAuth.getIsAuthenticated(state.auth);

export const getFirstWalletAccount = state =>
  fromWalletAccounts.getFirst(state.walletAccounts);

export default rootReducer;
