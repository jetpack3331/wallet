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

const createWalletKeystore = (dispatch, action, wallet, password) => {
  console.log('Encrypting wallet ...');
  return wallet.encrypt(password)
    .then((secretStorageJson) => {
      console.log('Encryption complete. Saving wallet');
      return wallets.createWalletAccount({
        address: wallet.address,
        secretStorageJson,
      });
    })
    .then((walletAccount) => {
      dispatch({ type: `${action}_SUCCESS`, response: normalize(walletAccount, schemas.walletAccount) });
      return null;
    })
    .catch((error) => {
      dispatch({ type: `${action}_FAILURE`, error });
      throw error;
    });
};

export const createWalletAccount = request => (dispatch) => {
  dispatch({ type: 'CREATE_WALLET_ACCOUNT_INPROGRESS' });
  const wallet = Wallet.createRandom();
  return createWalletKeystore(dispatch, 'CREATE_WALLET_ACCOUNT', wallet, request.password);
};

export const importPrivateKeyWalletAccount = request => (dispatch) => {
  dispatch({ type: 'IMPORT_WALLET_ACCOUNT_INPROGRESS' });
  const key = request.privateKey.substr(0, 2) === '0x' ? request.privateKey : `0x${request.privateKey}`;
  const wallet = new Wallet(key);
  return createWalletKeystore(dispatch, 'IMPORT_WALLET_ACCOUNT', wallet, request.password);
};
