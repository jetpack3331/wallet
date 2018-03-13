import { requestJSON } from './http';

const sendSignedTransaction = signedTransaction =>
  requestJSON('/transactions', 'POST', { value: signedTransaction });
const fetchNextNonce = address => requestJSON(`/accounts/${address}/nonce`, 'GET');

export default {
  sendSignedTransaction,
  fetchNextNonce,
};
