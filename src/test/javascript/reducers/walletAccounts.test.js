import reducer from '../../../main/client/javascript/reducers/wallet/walletAccounts';

describe('Reducer: WalletAccounts', () => {
  const initialState = {
    byId: {},
    listIds: {
      loading: false,
      walletAccounts: [],
    },
  };

  const loadedState = {
    walletAccounts: {
      byId: {
        '0xADDRESS': {
          created: '2018-03-07T16:46:27.767+11:00',
          updated: '2018-03-07T16:46:27.767+11:00',
          address: '0xADDRESS',
          secretStorageJson: '{"crypto" : {}, "ciphertext" : "5318b4d5bcd28de64ee5559e671353e16f075ecae9f99c7a79a38af5f869aa46"}',
        },
      },
      listIds: {
        walletAccounts: [
          '0xADDRESS',
        ],
        loading: false,
      },
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
});
