import resetOnLogout from '../resetOnLogout';

const byId = (state = {}, action) => {
  if (action.response && action.response.entities) {
    return {
      ...state,
      ...action.response.entities.walletTransactions,
    };
  }
  return state;
};

const lastSentById = (state = {}, action) => {
  switch (action.type) {
    case 'SEND_TRANSACTION_SUCCESS':
      return {
        ...state,
        [action.address]: action.response && action.response.result,
      };
    case 'WALLET_TRANSACTION_SENT_ACK': {
      const { [action.address]: omit, ...rest } = state;
      return rest;
    }
    default:
      return state;
  }
};

export const getById = (state, id) => state.byId[id];
export const getLastSentTransactionId = (state, address) => state.lastSentById[address];

export default resetOnLogout({
  byId,
  lastSentById,
});
