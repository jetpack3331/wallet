import applyEachSeries from 'async/applyEachSeries';
import { intersection } from 'lodash';
import { fetchUser } from '../actions/auth';
import { getIsAuthenticated, getLoggedInUser, getTermsAccepted } from '../reducers';

import store from '../store';

const { dispatch, getState } = store;

const withLogin = nextPage => ({ pathname: '/login', query: { next: nextPage } });

const withLandingPage = () => ({ pathname: '/' });

export const composeOnEnterHooks = (...hooks) => (nextState, replace, callback) => {
  applyEachSeries(hooks, nextState, replace, (error) => {
    if (error) {
      console.error('hook error:', error);
    } // eslint-disable-line no-console
    callback();
  });
};

export const initSession = (nextState, replace, callback) => {
  const isAuthenticated = getIsAuthenticated(getState());

  if (!isAuthenticated) {
    dispatch(fetchUser())
      .then(() => callback())
      .catch(error => callback(error));
  } else {
    callback();
  }
};

export const checkUserAuth = (nextState, replace, callback) => {
  // Only landing page is public
  if (nextState.location.pathname !== '/') {
    const isAuthenticated = getIsAuthenticated(getState());
    const termsAccepted = getTermsAccepted(getState());

    if (!isAuthenticated || !termsAccepted) {
      replace(withLandingPage());
    }
  }
  callback();
};

export const loginRequired = (nextState, replace, callback) => {
  const isAuthenticated = getIsAuthenticated(getState());
  if (isAuthenticated) {
    callback();
    return;
  }

  dispatch(fetchUser()).then(
    () => {
      callback();
    },
    (error) => {
      replace(withLogin(nextState.location.pathname));
      callback(error);
    },
  );
};

export const hasAnyRole = (...roles) => (nextState, replace, callback) => {
  const user = getLoggedInUser(getState());

  const hasRequiredRole = user && intersection(roles, user.roles).length > 0;
  if (hasRequiredRole) {
    callback();
    return;
  }

  replace(withLogin(nextState.location.pathname));
  callback(new Error('User lacks required role'));
};
