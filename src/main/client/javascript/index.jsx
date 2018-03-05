import 'normalize.css';
import React from 'react';
import ReactDOM from 'react-dom';
import injectTapEventPlugin from 'react-tap-event-plugin';
import '../less/styles/main.less';

import App from './components/App';
import { attachErrorLogger } from './util/errors';

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();
attachErrorLogger();

// Define render as a function so we can re-render when using Hot Module Replacement
const render = (Component) => {
  ReactDOM.render(<Component />, document.getElementById('root'));
};

// Perform initial render
render(App);

// Wire in Hot Module Replacement
if (module.hot) {
  module.hot.accept('./components/App', () => render(App));
}

