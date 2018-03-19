import { Wallet } from 'ethers';
import { bigNumberify, parseEther } from 'ethers/utils/index';
import { SubmissionError } from 'redux-form';
import { getGasPrices, getWalletAccount } from '../reducers';
import transactions from '../services/api/transactions';

const signPrivateTransaction = (walletAccount, transaction, password) => {
  const fetchNonce = transactions.fetchNextNonce(walletAccount.address);
  const decryptWallet = Wallet.fromEncryptedWallet(walletAccount.secretStorageJson, password);

  return Promise.all([fetchNonce, decryptWallet])
    .then((responses) => {
      const nonce = responses[0];
      const wallet = responses[1];
      const txn = {
        ...transaction,
        nonce: nonce.result,
      };
      console.log('Wallet unlocked and nonce fetched. Signing transaction ...', txn);
      return wallet.sign(txn);
    });
};

const signManagedTransaction = (walletAccount, transaction) =>
  transactions.fetchNextNonce(walletAccount.address)
    .then(nonce => (
      {
        from: walletAccount.address,
        to: transaction.to,
        nonce: nonce.result,
        gasPrice: transaction.gasPrice.toString(),
        gasLimit: transaction.gasLimit,
        value: transaction.value.toString(),
      }))
    .then((txnRequest) => {
      console.log('Signing transaction ...', txnRequest);
      return transactions.signTransaction(txnRequest);
    })
    .then(response => response.result);

export const signAndSendTransaction = (request, walletAddress, gasLimit, priceField) =>
  (dispatch, getState) => {
    const state = getState();
    const walletAccount = getWalletAccount(state, walletAddress);
    const gasPrices = getGasPrices(state);

    const transaction = {
      gasPrice: bigNumberify(gasPrices[priceField]),
      to: request.to,
      value: parseEther(request.amount),
      gasLimit,
    };

    dispatch({ type: 'SEND_TRANSACTION_INPROGRESS' });
    const signTransactionPromise = walletAccount.type === 'MANAGED' ?
      signManagedTransaction(walletAccount, transaction) :
      signPrivateTransaction(walletAccount, transaction, request.password);

    return signTransactionPromise
      .then((signedTransaction) => {
        console.log('Transaction signed. Sending ...');
        return transactions.sendSignedTransaction(signedTransaction);
      })
      .then((response) => {
        dispatch({ type: 'SEND_TRANSACTION_SUCCESS', address: walletAccount.address, response });
        console.log('Transaction success', response);
        return response;
      })
      .catch((error) => {
        dispatch({ type: 'SEND_TRANSACTION_FAILURE', error });
        throw new SubmissionError({ _error: error.message });
      });
  };
