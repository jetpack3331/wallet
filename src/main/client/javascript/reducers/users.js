import { combineReducers } from 'redux';
import { createSelector } from 'reselect';

const byId = (state = {}, action) => {
  if (action.response && action.response.entities) {
    return {
      ...state,
      ...action.response.entities.users,
    };
  }

  return state;
};

const listIds = (state = { users: [], loading: true }, action) => {
  switch (action.type) {
    case 'INVITE_USER_SUCCESS':
      return {
        ...state,
        users: [...state.users, action.response.result],
      };
    case 'FETCH_USERS_SUCCESS':
      return {
        loading: false,
        users: [...action.response.result],
      };
    case 'FETCH_USERS_INPROGRESS':
      return {
        ...state,
        loading: true,
      };
    default:
      return state;
  }
};

export const getAllIsLoading = state => state.listIds.loading;

export const getAll = createSelector(
  state => state.listIds.users,
  state => state.byId,
  (users, usersById) => users.map(id => usersById[id]),
);
export const getById = (state, id) => state.byId[id];

export default combineReducers({
  byId,
  listIds,
});
