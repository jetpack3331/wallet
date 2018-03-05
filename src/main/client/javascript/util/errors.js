import parser from 'stacktrace-parser';
import api from '../services/api';

function parseStackTrace(stack) {
  return parser.parse(stack)
    .map((item) => {
      const symbol = item.methodName.split('.');
      const clazz = ['JS', ...(symbol.slice(0, -1) || [])].join('.');
      const method = symbol[symbol.length - 1];
      return ({
        file: item.file,
        line: item.lineNumber,
        clazz,
        method,
      });
    });
}

export function logComponentError(error, componentStack) {
  api.errors.logComponentError({
    message: error.message,
    trace: parseStackTrace(error.stack),
    componentStack,
  });
}

export function logReduxError(error, getState, lastAction) {
  api.errors.logReduxError({
    message: error.message,
    trace: parseStackTrace(error.stack),
    type: lastAction.type,
    lastAction: JSON.stringify(lastAction, null, 2).substr(0, 500),
  });

  // Don't double report
  // eslint-disable-next-line no-param-reassign
  error.reported = true;
  throw error;
}

export function logUnhandledError(error) {
  api.errors.logUnhandledError({
    message: error.message,
    trace: parseStackTrace(error.stack),
  });
}

export function attachErrorLogger() {
  window.addEventListener('error', ({ error }) => {
    // If error is not present was probably an error in a script from
    //   another domain - unlikely we care about it
    if (error && !error.reported) {
      logUnhandledError(error);
    }
  });
}
