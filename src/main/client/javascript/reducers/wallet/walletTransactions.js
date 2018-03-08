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

export const getById = (state, id) => state.byId[id];

export default resetOnLogout({
  byId,
});
