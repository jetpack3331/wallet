import { combineReducers } from 'redux';

export default reducersObject => (state, action) => {
  const newState = action.type === 'REQUEST_LOGOUT_SUCCESS' ? undefined : state;
  const reducers = combineReducers(reducersObject);
  return reducers(newState, action);
};
