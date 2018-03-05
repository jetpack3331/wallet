import Reboot from 'material-ui/Reboot';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import React, { Fragment } from 'react';
import { AppContainer } from 'react-hot-loader';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import getRoutes from '../routes';
import store from '../store';
import theme from '../theme';
import ErrorBoundary from './ErrorBoundary';

const App = () => (
  <Fragment>
    <Reboot />
    <AppContainer>
      <ErrorBoundary>
        <Provider store={store}>
          <IntlProvider locale="en-AU">
            <MuiThemeProvider theme={theme}>
              {getRoutes(store.getState, store.dispatch)}
            </MuiThemeProvider>
          </IntlProvider>
        </Provider>
      </ErrorBoundary>
    </AppContainer>
  </Fragment>);

export default App;
