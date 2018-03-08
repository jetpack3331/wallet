import { requestJSON } from './http';

const createWalletAccount = walletAccount =>
  requestJSON('/wallets/accounts', 'POST', walletAccount);

const fetchMyWalletAccounts = () => requestJSON('/wallets/accounts/mine', 'GET');

const fetchWalletBalance = address => requestJSON(`/wallets/accounts/${address}/balance`, 'GET');
const fetchWalletTransactions = address => requestJSON(`/wallets/accounts/${address}/transactions`, 'GET');

export default {
  createWalletAccount,
  fetchMyWalletAccounts,
  fetchWalletBalance,
  fetchWalletTransactions,
};
