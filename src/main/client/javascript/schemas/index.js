import { schema } from 'normalizr';
import { tokenBalanceId } from '../model';

const user = new schema.Entity('users');
const arrayOfUsers = new schema.Array(user);

const walletAccount = new schema.Entity('walletAccounts', {}, { idAttribute: 'address' });
const arrayOfWalletAccounts = new schema.Array(walletAccount);

const walletBalance = new schema.Entity('walletBalances', {}, { idAttribute: 'address' });
const walletTokenBalance = new schema.Entity(
  'walletTokenBalances', {},
  { idAttribute: e => tokenBalanceId(e.contractAddress, e.address) },
);
const walletTransactions = new schema.Entity('walletTransactions', {}, { idAttribute: 'address' });


export default {
  user,
  arrayOfUsers,
  walletAccount,
  arrayOfWalletAccounts,
  walletBalance,
  walletTokenBalance,
  walletTransactions,
};
