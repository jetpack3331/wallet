import { push } from 'react-router-redux';
import users from '../services/api/users';
import { asyncAction } from './actions';

export const setUser = user => ({
  type: 'SET_USER',
  user,
});

export const fetchUser = () => (dispatch) => {
  dispatch({ type: 'REQUEST_LOGGED_IN_USER' });

  return users.me()
    .then((user) => {
      dispatch({ type: 'REQUEST_LOGGED_IN_USER_SUCCESS', loggedIn: !!user });
      dispatch(setUser(user));
      return user;
    })
    .catch((error) => {
      dispatch({ type: 'REQUEST_LOGGED_IN_USER_FAILURE', error });
      throw error;
    });
};

export const login = credentials => (dispatch) => {
  dispatch({ type: 'REQUEST_LOGIN' });

  return users.login(credentials)
    .then(users.me)
    .then((user) => {
      dispatch({ type: 'REQUEST_LOGIN_SUCCESS', user });
      return user;
    })
    .catch((error) => {
      dispatch({ type: 'REQUEST_LOGIN_FAILURE', error });
      throw error;
    });
};

export const logout = () => (dispatch) => {
  dispatch({ type: 'REQUEST_LOGOUT' });

  return users.logout()
    .then(() => {
      dispatch({ type: 'REQUEST_LOGOUT_SUCCESS' });
      return null;
    })
    .catch((error) => {
      dispatch({ type: 'REQUEST_LOGOUT_FAILURE', error });
      throw error;
    });
};

export const logoutToHome = () => dispatch =>
  dispatch(logout())
    .then(dispatch(push('/')));

export const setSessionExpiry = value => dispatch =>
  dispatch({ type: 'SET_SESSION_EXPIRY', value });

export const acceptTerms = () => asyncAction(
  'ACCEPT_TERMS',
  users.acceptTerms(), {
    onSuccess: (user, dispatch) => dispatch(setUser(user)),
  },
);
