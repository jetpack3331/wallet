import { requestJSON } from './http';

const createWalletAccount = walletAccount =>
  requestJSON('/wallets/accounts', 'POST', walletAccount);

export default {
  createWalletAccount,
};
