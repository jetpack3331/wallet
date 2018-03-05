import { browserHistory } from 'react-router';
import { routerMiddleware, syncHistoryWithStore } from 'react-router-redux';
import { applyMiddleware, createStore } from 'redux';
import reduxCatch from 'redux-catch';
import { composeWithDevTools } from 'redux-devtools-extension';
import thunk from 'redux-thunk';
import rootReducer from './reducers';
import { logReduxError } from './util/errors';

const middleware = [routerMiddleware(browserHistory), thunk, reduxCatch(logReduxError)];

const store = createStore(rootReducer, composeWithDevTools(applyMiddleware(...middleware)));

// Create an enhanced history that syncs navigation events with the store
export const history = syncHistoryWithStore(browserHistory, store);

export default store;
