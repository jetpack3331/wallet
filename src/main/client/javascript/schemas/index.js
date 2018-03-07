import { schema } from 'normalizr';

const user = new schema.Entity('users');
const arrayOfUsers = new schema.Array(user);

const walletAccount = new schema.Entity('walletAccounts', {}, { idAttribute: 'address' });
const arrayOfWalletAccounts = new schema.Array(walletAccount);

export default {
  user,
  arrayOfUsers,
  walletAccount,
  arrayOfWalletAccounts,
};
