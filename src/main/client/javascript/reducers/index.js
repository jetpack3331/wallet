import { routerReducer as routing } from 'react-router-redux';
import { combineReducers } from 'redux';
import { reducer as form } from 'redux-form';
import auth, * as fromAuth from './auth';
import gasPrices, * as fromGasPrices from './gasPrices';
import tokens, * as fromTokens from './tokens';
import walletAccounts, * as fromWalletAccounts from './wallet/walletAccounts';
import walletBalances, * as fromWalletBalances from './wallet/walletBalances';
import walletTokenBalances, * as fromWalletTokenBalances from './wallet/walletTokenBalances';
import walletTransactions, * as fromWalletTransactions from './wallet/walletTransactions';
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
  walletTransactions,
  walletBalances,
  walletTokenBalances,
  gasPrices,
  tokens,
});

export const getLoggedInUser = state =>
  fromAuth.getUser(state.auth);

export const getIsAuthenticated = state =>
  fromAuth.getIsAuthenticated(state.auth);

export const getSessionExpiresAt = state =>
  fromAuth.getSessionExpiresAt(state.auth);

export const getWalletAccount = (state, address) =>
  fromWalletAccounts.getById(state.walletAccounts, address);

export const getAllWalletAccounts = state =>
  fromWalletAccounts.getAll(state.walletAccounts);

export const listWalletAccountsIsLoading = state =>
  fromWalletAccounts.listWalletAccountsIsLoading(state.walletAccounts);

export const getAllWalletAddresses = state =>
  fromWalletAccounts.getAllAddresses(state.walletAccounts);

export const getWalletBalance = (state, address) =>
  fromWalletBalances.getById(state.walletBalances, address);

export const getTotalBalance = state =>
  fromWalletBalances.getTotalBalance(state.walletBalances, getAllWalletAddresses(state));

export const getWalletTransactions = (state, address) =>
  fromWalletTransactions.getById(state.walletTransactions, address);

export const getLastSentTransactionId = (state, address) =>
  fromWalletTransactions.getLastSentTransactionId(state.walletTransactions, address);

export const getGasPrices = state => fromGasPrices.get(state.gasPrices);
export const getTokens = state => fromTokens.getAll(state.tokens);

export const getBalanceByContractAddressAndAddress = (state, contractAddress, address) =>
  fromWalletTokenBalances.getByContractAddressAndAddress(
    state.walletTokenBalances,
    contractAddress, address,
  );

export default rootReducer;
