import { Wallet } from 'ethers';
import { bigNumberify, parseEther } from 'ethers/utils/index';
import { getGasPrices, getWalletAccount } from '../reducers';
import transactions from '../services/api/transactions';

export const signAndSendTransaction = (request, walletAddress, gasLimit, priceField) =>
  (dispatch, getState) => {
    const state = getState();
    const { secretStorageJson } = getWalletAccount(state, walletAddress);
    const gasPrices = getGasPrices(state);

    const transaction = {
      nonce: 2,
      gasPrice: bigNumberify(gasPrices[priceField]),
      to: request.to,
      value: parseEther(request.amount),
      gasLimit,
    };

    dispatch({ type: 'SEND_TRANSACTION_INPROGRESS' });
    Wallet.fromEncryptedWallet(secretStorageJson, request.password)
      .then((wallet) => {
        console.log('Wallet unlocked. Signing transaction ...', transaction);
        return wallet.sign(transaction);
      })
      .then((signedTransaction) => {
        console.log('Transaction signed. Sending ...');
        return transactions.sendSignedTransaction(signedTransaction);
      })
      .then((response) => {
        dispatch({ type: 'SEND_TRANSACTION_SUCCESS', response });
        return null;
      })
      .catch((error) => {
        dispatch({ type: 'SEND_TRANSACTION_FAILURE', error });
        throw error;
      });
  };
