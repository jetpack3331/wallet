import { utils } from 'ethers';
import resetOnLogout from '../resetOnLogout';


const byId = (state = {}, action) => {
  if (action.response && action.response.entities) {
    return {
      ...state,
      ...action.response.entities.walletBalances,
    };
  }
  return state;
};

export const getById = (state, id) => state.byId[id];

export const getTotalBalance = (state, addresses) =>
  addresses.reduce((total, address) => {
    const addressBalance = getById(state, address);
    return addressBalance ? total.add(utils.bigNumberify(addressBalance.balance)) : total;
  }, utils.bigNumberify(0));

export default resetOnLogout({
  byId,
});
