import { Wallet } from 'ethers';
import { bigNumberify, parseEther } from 'ethers/utils/index';
import { SubmissionError } from 'redux-form';
import { getGasPrices, getWalletAccount } from '../reducers';
import transactions from '../services/api/transactions';

export const signAndSendTransaction = (request, walletAddress, gasLimit, priceField) =>
  (dispatch, getState) => {
    const state = getState();
    const { secretStorageJson } = getWalletAccount(state, walletAddress);
    const gasPrices = getGasPrices(state);

    const transaction = {
      gasPrice: bigNumberify(gasPrices[priceField]),
      to: request.to,
      value: parseEther(request.amount),
      gasLimit,
    };

    const fetchNonce = transactions.fetchNextNonce(walletAddress);
    const encryptWallet = Wallet.fromEncryptedWallet(secretStorageJson, request.password);

    dispatch({ type: 'SEND_TRANSACTION_INPROGRESS' });
    return Promise.all([fetchNonce, encryptWallet])
      .then((responses) => {
        const nonce = responses[0];
        const wallet = responses[1];
        transaction.nonce = nonce.result;
        console.log('Wallet unlocked and nonce fetched. Signing transaction ...', transaction);
        return wallet.sign(transaction);
      })
      .then((signedTransaction) => {
        console.log('Transaction signed. Sending ...');
        return transactions.sendSignedTransaction(signedTransaction);
      })
      .then((response) => {
        dispatch({ type: 'SEND_TRANSACTION_SUCCESS', response });
        console.log('Transaction success', response);
        return response;
      })
      .catch((error) => {
        dispatch({ type: 'SEND_TRANSACTION_FAILURE', error });
        throw new SubmissionError({ _error: error.message });
      });
  };
