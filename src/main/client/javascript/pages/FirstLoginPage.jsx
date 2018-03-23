import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import AcceptTermsForm from '../components/forms/AcceptTermsForm';
import { acceptTerms, logoutToHome } from '../actions/auth';
import './FirstLoginPage.less';

const FirstLoginPage = ({ onAcceptTerms, onRejectTerms }) => (
  <div className="wallet-container first-login-page">
    <div className="container">
      <div className="widgets">
        <div className="container">
          <h1 className="display-1">Not Found</h1>

          <div className="terms-container">
            <AcceptTermsForm onSubmit={onAcceptTerms} onCancel={onRejectTerms}/>
          </div>

        </div>
      </div>
    </div>
  </div>
);

FirstLoginPage.propTypes = {
  onAcceptTerms: PropTypes.func.isRequired,
  onRejectTerms: PropTypes.func.isRequired,
};

const mapDispatchToProps = {
  onAcceptTerms: acceptTerms,
  onRejectTerms: logoutToHome,
};

export default connect(null, mapDispatchToProps)(FirstLoginPage);
