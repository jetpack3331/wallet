import { shape, string } from 'prop-types';

export const walletAccount = shape({
  address: string,
  secretStorageJson: string,
});

export const walletBalance = shape({
  address: string,
  balance: string,
});
