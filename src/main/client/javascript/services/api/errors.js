import { requestJSON } from './http';

const logComponentError = error => requestJSON('/error/component', 'POST', error);
const logReduxError = error => requestJSON('/error/redux', 'POST', error);
const logUnhandledError = error => requestJSON('/error/unhandled', 'POST', error);

export default {
  logUnhandledError,
  logReduxError,
  logComponentError,
};
