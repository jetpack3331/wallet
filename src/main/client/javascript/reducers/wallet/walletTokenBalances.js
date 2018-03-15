import { tokenBalanceId } from '../../model';
import resetOnLogout from '../resetOnLogout';


const byContractAddressAndAddress = (state = {}, action) => {
  if (action.response && action.response.entities) {
    return {
      ...state,
      ...action.response.entities.walletTokenBalances,
    };
  }
  return state;
};

export const getByContractAddressAndAddress = (state, contractAddress, address) =>
  state.byContractAddressAndAddress[tokenBalanceId(contractAddress, address)];


export default resetOnLogout({
  byContractAddressAndAddress,
});
