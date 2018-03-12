/* eslint-disable no-console */
import Cookies from 'universal-cookie';
import store from '../../store';
import { logoutToHome, setSessionExpiry } from '../../actions/auth';

const baseUrl = '/api';

const cookies = new Cookies();

const commonHeaders = () => ({
  Pragma: 'no-cache',
  'Cache-Control': 'no-cache', // required so IE11 does not automatically cache all GET requests
  'X-Requested-With': 'XMLHttpRequest', // Suppress the gray basic auth dialog in the browser on 401
  'X-XSRF-TOKEN': cookies.get('XSRF-TOKEN'), // Ensure CSRF token is sent with every request
});

export const formEncode = obj => Object.keys(obj).map(k => `${k}=${encodeURIComponent(obj[k])}`).join('&');

const redirectToHomeIfUnauthenticated = (response) => {
  if (response.status === 401) {
    store.dispatch(logoutToHome());
    return Promise.reject(new Error('Unauthenticated'));
  }
  return response;
};

const extractSessionExpiresHeader = (response) => {
  if (response.ok && response.headers.has('Session-Expiry')) {
    const value = +response.headers.get('Session-Expiry');
    store.dispatch(setSessionExpiry(value));
  }
  return response;
};

const processResponse = (response) => {
  if (response.ok) {
    return response;
  }

  return response.text().then((text) => {
    let error;

    try {
      // Attempt to parse body as JSON, fallback to plain text if parsing fails
      const data = JSON.parse(text);
      error = new Error(data.message);
      error.type = data.type;
    } catch (e) {
      // Fallback to plain text
      error = new Error(response.statusText);
    }

    error.status = response.status;
    error.payload = text;

    throw error;
  });
};

export const request = (path, method = 'GET', body = null, headers = {}) => {
  const url = `${baseUrl}${path}`;

  console.log(`${method} ${url}`);

  const config = {
    method,
    headers: { ...commonHeaders(), ...headers },
    credentials: 'include',
  };

  // Edge browsers will fail silently if you give a body, even a null one, to a GET request
  if (body) {
    config.body = body;
  }

  return fetch(url, config).then(response =>
    Promise.resolve(response)
      .then(redirectToHomeIfUnauthenticated)
      .then(extractSessionExpiresHeader)
      .then(processResponse));
};

const hasHeader = (headers = {}, headerName) =>
  Object.keys(headers).some(key => key.toLowerCase() === headerName.toLowerCase());

const requestWithData = (path, method, data, headers = {}) => {
  const headerContentType = 'Content-Type';
  // Don't modify for FormData or request with existing content-type header set
  if (data instanceof FormData || hasHeader(headers, headerContentType)) {
    return request(path, method, data, headers);
  }
  // Otherwise default to JSON
  return request(path, method, JSON.stringify(data), {
    [headerContentType]: 'application/json',
    ...headers,
  });
};

export const requestJSON = (path, method, data, headers = {}) =>
  (data ? requestWithData(path, method, data, headers) : request(path, method, null, headers))
    .then(response => response.text())
    .then(responseText => responseText && JSON.parse(responseText));
