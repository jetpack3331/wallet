import { shape, string, arrayOf } from 'prop-types';

export const walletAccount = shape({
  address: string,
  secretStorageJson: string,
});

export const walletBalance = shape({
  address: string,
  balance: string,
});

export const walletTransaction = shape({
  blockNumber: string,
  blockHash: string,
  timeStamp: string,
  hash: string,
  nonce: string,
  transactionIndex: string,
  from: string,
  to: string,
  value: string,
  gas: string,
  gasPrice: string,
  input: string,
  contractAddress: string,
  cumulativeGasUsed: string,
  txreceipt_status: string,
  gasUsed: string,
  confirmations: string,
  isError: string,
});

export const walletTransactions = shape({
  address: string,
  transactions: arrayOf(walletTransaction),
});
