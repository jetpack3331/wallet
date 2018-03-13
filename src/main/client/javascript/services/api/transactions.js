import { requestJSON } from './http';

const sendSignedTransaction = signedTransaction =>
  requestJSON('/transactions', 'POST', { value: signedTransaction });

export default {
  sendSignedTransaction,
};
