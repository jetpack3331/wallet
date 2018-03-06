import { schema } from 'normalizr';

const user = new schema.Entity('users');
const arrayOfUsers = new schema.Array(user);

const walletAccount = new schema.Entity('walletAccounts');

export default {
  user,
  arrayOfUsers,
  walletAccount,
};
