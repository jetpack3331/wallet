import { requestJSON } from './http';

const createWalletAccount = walletAccount =>
  requestJSON('/wallets/accounts', 'POST', walletAccount);

const fetchMyWalletAccounts = () => requestJSON('/wallets/accounts/mine', 'GET');

export default {
  createWalletAccount,
  fetchMyWalletAccounts,
};
