import { routerReducer as routing } from 'react-router-redux';
import { combineReducers } from 'redux';
import { reducer as form } from 'redux-form';
import auth, * as fromAuth from './auth';
import walletAccounts, * as fromWalletAccounts from './wallet/walletAccounts';
import walletBalances, * as fromWalletBalances from './wallet/walletBalances';
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
  walletBalances,
});

export const getLoggedInUser = state =>
  fromAuth.getUser(state.auth);

export const getIsAuthenticated = state =>
  fromAuth.getIsAuthenticated(state.auth);

export const getFirstWalletAccount = state =>
  fromWalletAccounts.getFirst(state.walletAccounts);
export const listWalletAccountsIsLoading = state =>
  fromWalletAccounts.listWalletAccountsIsLoading(state.walletAccounts);

export const getWalletBalance = (state, address) =>
  fromWalletBalances.getById(state.walletBalances, address);

export default rootReducer;
