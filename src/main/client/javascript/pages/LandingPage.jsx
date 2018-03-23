import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { getIsAuthenticated, getTermsAccepted } from '../reducers';
import HomePage from './HomePage';
import MyWalletPage from './MyWalletPage';
import FirstLoginPage from './FirstLoginPage';

const LandingPage = ({ isAuthenticated, termsAccepted }) => {
  if (isAuthenticated && termsAccepted) {
    return <MyWalletPage/>;
  }

  if (isAuthenticated) {
    return <FirstLoginPage/>;
  }

  return <HomePage/>;
};

LandingPage.propTypes = {
  isAuthenticated: PropTypes.bool.isRequired,
  termsAccepted: PropTypes.bool.isRequired,
};

const mapStateToProps = state => ({
  isAuthenticated: getIsAuthenticated(state),
  termsAccepted: getTermsAccepted(state),
});

export default connect(mapStateToProps)(LandingPage);
