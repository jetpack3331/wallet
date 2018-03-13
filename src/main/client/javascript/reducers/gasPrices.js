import { combineReducers } from 'redux';

const gasPrices = (state = null, action) => {
  switch (action.type) {
    case 'GAS_PRICE_FETCH_INPROGRESS':
      return null;
    case 'GAS_PRICE_FETCH_SUCCESS':
      return { ...action.response };
    default:
      return state;
  }
};

export const get = state => state.gasPrices;

export default combineReducers({
  gasPrices,
});
