import { normalize } from 'normalizr';
import schemas from '../../../../main/client/javascript/schemas/index';
import reducer from '../../../../main/client/javascript/reducers/wallet/walletAccounts';

describe('Reducer: WalletAccounts', () => {
  const initialState = {
    byId: {},
    listIds: {
      loading: false,
      walletAccounts: [],
    },
  };

  const createRawWallet = address => ({
    created: '2018-03-07T16:46:27.767+11:00',
    updated: '2018-03-07T16:46:27.767+11:00',
    address,
    secretStorageJson: '{"crypto" : {}, "ciphertext" : "5318b4d5bcd28de64ee5559e671353e16f075ecae9f99c7a79a38af5f869aa46"}',
  });

  const loadedState = {
    byId: {
      '0xADDRESS1': createRawWallet('0xADDRESS1'),
    },
    listIds: {
      walletAccounts: [
        '0xADDRESS1',
      ],
      loading: false,
    },
  };

  test('correctly sets initial state', () => {
    const state = reducer(undefined, {});

    expect(state).toEqual(initialState);
  });

  test('resets to initial state on logout', () => {
    const state = reducer(loadedState, { type: 'REQUEST_LOGOUT_SUCCESS' });

    expect(state).toEqual(initialState);
  });

  describe('IMPORT_WALLET_ACCOUNT', () => {
    test('adds wallet to byId', () => {
      const rawWallet = createRawWallet('0xADDRESS2');
      const normalised = normalize(rawWallet, schemas.walletAccount);
      const state = reducer(loadedState, { type: 'IMPORT_WALLET_ACCOUNT_SUCCESS', response: normalised });

      expect(state.byId['0xADDRESS2']).toEqual(rawWallet);
    });

    test('adds wallet to listIds', () => {
      const rawWallet = createRawWallet('0xADDRESS2');
      const normalised = normalize(rawWallet, schemas.walletAccount);
      const state = reducer(loadedState, { type: 'IMPORT_WALLET_ACCOUNT_SUCCESS', response: normalised });

      expect(state.listIds.walletAccounts.length).toBe(2);
      expect(state.listIds.walletAccounts).toContain('0xADDRESS2');
    });
  });
});
