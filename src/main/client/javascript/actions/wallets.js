import { Wallet } from 'ethers';
import { normalize } from 'normalizr';
import schemas from '../schemas';
import wallets from '../services/api/wallets';

export const createWalletAccount = password => (dispatch) => {
  dispatch({ type: 'CREATE_WALLET_ACCOUNT_INPROGRESS' });

  const wallet = Wallet.createRandom();

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
      dispatch({ type: 'CREATE_WALLET_ACCOUNT_SUCCESS', response: normalize(walletAccount, schemas.walletAccount) });
      return null;
    })
    .catch((error) => {
      dispatch({ type: 'CREATE_WALLET_ACCOUNT_FAILURE', error });
      throw error;
    });
};
