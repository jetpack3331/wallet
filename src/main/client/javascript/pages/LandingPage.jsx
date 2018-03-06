import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { getIsAuthenticated } from '../reducers';
import HomePage from './HomePage';
import WalletPage from './WalletPage';

const LandingPage = ({ isAuthenticated }) => (
  isAuthenticated ? <WalletPage/> : <HomePage/>
);

LandingPage.propTypes = {
  isAuthenticated: PropTypes.bool.isRequired,
};

const mapStateToProps = state => ({
  isAuthenticated: getIsAuthenticated(state),
});

export default connect(mapStateToProps)(LandingPage);
