import { Wallet } from 'ethers';
import { normalize } from 'normalizr';
import schemas from '../schemas';
import wallets from '../services/api/wallets';
import { asyncAction } from './actions';

export const fetchMyWalletAccounts = () => asyncAction(
  'WALLET_ACCOUNTS_FETCH',
  wallets.fetchMyWalletAccounts(), {
    responseTransformer: walletAccounts => normalize(walletAccounts, schemas.arrayOfWalletAccounts),
  },
);

export const fetchWalletBalance = address => asyncAction(
  'WALLET_BALANCE_FETCH',
  wallets.fetchWalletBalance(address), {
    responseTransformer: walletBalance => normalize(walletBalance, schemas.walletBalance),
  },
);

export const fetchWalletTransactions = address => asyncAction(
  'WALLET_TRANSACTIONS_FETCH',
  wallets.fetchWalletTransactions(address), {
    responseTransformer: walletTransactions =>
      normalize(walletTransactions, schemas.walletTransactions),
  },
);

export const createWalletAccount = request => (dispatch) => {
  dispatch({ type: 'CREATE_WALLET_ACCOUNT_INPROGRESS' });

  const wallet = Wallet.createRandom();

  console.log('Encrypting wallet ...');
  return wallet.encrypt(request.password)
    .then((secretStorageJson) => {
      console.log('Encryption complete. Saving wallet');
      return wallets.createWalletAccount({
        address: wallet.address,
        secretStorageJson,
      });
    })
    .then((walletAccount) => {
      dispatch({ type: 'CREATE_WALLET_ACCOUNT_SUCCESS', response: normalize(walletAccount, schemas.walletAccount) });
      return null;
    })
    .catch((error) => {
      dispatch({ type: 'CREATE_WALLET_ACCOUNT_FAILURE', error });
      throw error;
    });
};
