import { isFunction } from 'lodash';

export const asyncAction =
  (prefix, promiseOrPromiseCreator, options = {}) => (dispatch, getState) => {
    const {
      responseTransformer = res => res,
      onSuccess = () => undefined,
      onFailure = () => undefined,
    } = options;

    console.log(`${prefix}_INPROGRESS`);
    dispatch({
      type: `${prefix}_INPROGRESS`,
    });

    let promise;
    if (isFunction(promiseOrPromiseCreator)) {
      promise = promiseOrPromiseCreator(getState);
    } else {
      promise = promiseOrPromiseCreator;
    }

    return promise
      .then((res) => {
        console.log(`${prefix}_SUCCESS`);
        dispatch({
          type: `${prefix}_SUCCESS`,
          response: responseTransformer(res),
        });
        onSuccess(res, dispatch, getState);
      }, (error) => {
        console.error('Async request error:', error);
        dispatch({
          type: `${prefix}_FAILED`,
          errorMessage: error.message,
        });
        onFailure(error, dispatch, getState);
        throw error;
      })
      .catch((error) => {
        console.error('Unhandled error', error);
        throw error;
      });
  };

export default {
  asyncAction,
};
