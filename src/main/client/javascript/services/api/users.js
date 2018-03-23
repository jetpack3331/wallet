import { formEncode, request, requestJSON } from './http';

const login = credentials =>
  request('/login', 'POST', formEncode({ ...credentials, 'remember-me': true }), { 'Content-Type': 'application/x-www-form-urlencoded' });

const logout = () =>
  request('/logout', 'POST');

const me = () =>
  requestJSON('/users/me', 'GET');

const acceptTerms = () =>
  requestJSON('/users/terms', 'POST', null);

export default {
  login,
  logout,
  me,
  acceptTerms,
};
