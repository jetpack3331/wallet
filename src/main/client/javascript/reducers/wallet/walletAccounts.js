import { createSelector } from 'reselect';
import resetOnLogout from '../resetOnLogout';

const byId = (state = {}, action) => {
  if (action.response && action.response.entities) {
    return {
      ...state,
      ...action.response.entities.walletAccounts,
    };
  }
  return state;
};

const listIds = (state = { walletAccounts: [], loading: false }, action) => {
  switch (action.type) {
    case 'CREATE_WALLET_ACCOUNT_SUCCESS':
      return {
        ...state,
        walletAccounts: [...state.walletAccounts, action.response.result],
      };
    case 'WALLET_ACCOUNTS_FETCH_INPROGRESS':
      return {
        ...state,
        loading: true,
      };
    case 'WALLET_ACCOUNTS_FETCH_SUCCESS':
      return {
        ...state,
        walletAccounts: [...action.response.result],
        loading: false,
      };
    default:
      return state;
  }
};

export const getAll = createSelector(
  state => state.listIds.walletAccounts,
  state => state.byId,
  (walletAccounts, walletAccountsById) => walletAccounts.map(id => walletAccountsById[id]),
);

export const getById = (state, id) => state.byId[id];
export const getFirst = state => (state.listIds.walletAccounts.length &&
  getById(state, state.listIds.walletAccounts[0])) || null;
export const listWalletAccountsIsLoading = state => state.listIds.loading;

export default resetOnLogout({
  byId,
  listIds,
});
