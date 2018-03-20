import { combineReducers } from 'redux';
import * as tokenState from '../conf/tokens.json';

const tokens = (state = tokenState) => state;

export const getAll = state => state.tokens;
export const get = (state, symbol) => state.tokens.find(t => t.symbol === symbol);

export default combineReducers({
  tokens,
});

