import { requestJSON } from './http';

const createWalletAccount = walletAccount =>
  requestJSON('/wallets/accounts', 'POST', walletAccount);

const fetchMyWalletAccounts = () => requestJSON('/wallets/accounts/mine', 'GET');

const getBalance = address => requestJSON(`/wallets/accounts/${address}/balance`, 'GET');

export default {
  createWalletAccount,
  fetchMyWalletAccounts,
  getBalance,
};
