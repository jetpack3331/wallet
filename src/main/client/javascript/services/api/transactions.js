import { requestJSON } from './http';

const signTransaction = txnRequest =>
  requestJSON('/transactions/sign', 'POST', txnRequest);

const sendSignedTransaction = signedTransaction =>
  requestJSON('/transactions', 'POST', { value: signedTransaction });

const fetchNextNonce = address => requestJSON(`/accounts/${address}/nonce`, 'GET');

export default {
  signTransaction,
  sendSignedTransaction,
  fetchNextNonce,
};
