import { Wallet } from 'ethers';
import { bigNumberify, parseEther, parseUnits } from 'ethers/utils';
import { SubmissionError } from 'redux-form';
import { getGasPrices, getToken, getWalletAccount } from '../reducers';
import transactions from '../services/api/transactions';
import { iface } from '../util/ethereum-abi';

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
        ...transaction,
        from: walletAccount.address,
        nonce: nonce.result,
        gasPrice: transaction.gasPrice.toString(),
        value: transaction.value && transaction.value.toString(),
      }))
    .then((txnRequest) => {
      console.log('Signing transaction ...', txnRequest);
      return transactions.signTransaction(txnRequest);
    })
    .then(response => response.result);

export const signAndSendTransaction =
  (request, walletAddress, gasLimit, priceField, options = {}) => (dispatch, getState) => {
    const state = getState();
    const walletAccount = getWalletAccount(state, walletAddress);
    const gasPrices = getGasPrices(state);

    const transaction = {
      gasPrice: bigNumberify(gasPrices[priceField]),
      to: request.to,
      value: parseEther(request.amount),
      gasLimit,
      ...options,
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

export const sendTokens = (request, walletAddress, tokenSymbol, priceField) =>
  (dispatch, getState) => {
    const state = getState();
    const token = getToken(state, tokenSymbol);

    if (!token) {
      throw new SubmissionError({ _error: `No such token: ${tokenSymbol}` });
    }

    const transferFunction = iface.functions.transfer(
      request.to,
      parseUnits(request.amount, token.decimals),
    );

    const options = {
      to: token.address,
      data: transferFunction.data,
      value: bigNumberify(0),
    };

    return dispatch(signAndSendTransaction(request, walletAddress, 100000, priceField, options));
  };
