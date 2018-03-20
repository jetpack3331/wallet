import { Contract } from 'ethers/contracts';
import { getDefaultProvider } from 'ethers/providers';
import { abi } from '../../util/ethereum-abi';
import { requestJSON } from './http';

const provider = getDefaultProvider();
const createWalletAccount = walletAccount =>
  requestJSON('/wallets/accounts', 'POST', walletAccount);

const fetchMyWalletAccounts = () => requestJSON('/wallets/accounts/mine', 'GET');

const fetchWalletBalance = address => provider.getBalance(address)
  .then(balance => ({ address, balance: balance.toString() }));

const fetchTokenBalance = (contractAddress, address) => {
  const contract = new Contract(contractAddress, abi, provider);
  return contract.balanceOf(address)
    .then(balance => ({ contractAddress, address, balance: balance.toString() }));
};

const fetchWalletTransactions = address => requestJSON(`/wallets/accounts/${address}/transactions`, 'GET');

const fetchPrivateKey = address =>
  requestJSON(`/wallets/accounts/${address}/privateKey`, 'GET');

export default {
  createWalletAccount,
  fetchMyWalletAccounts,
  fetchWalletBalance,
  fetchTokenBalance,
  fetchWalletTransactions,
  fetchPrivateKey,
};
