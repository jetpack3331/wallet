import React from 'react';
import BuildIcon from 'material-ui-icons/Build';
import ProtectedEnvironmentForm from '../components/forms/ProtectedEnvironmentForm';
import './HomePage.less';

const ProtectedEnvironmentPage = () => (
  <div className="home-page">
    <div className="container">
      <div className="login-container">
        <div className="main-icon"><BuildIcon className="logo" alt="HUT34 Wallet"/></div>
        <h1 className="display-1"><strong>Protected Testing Environment</strong></h1>


        <p>This environment contains untested features and is for authorised testers only.</p>
        <ProtectedEnvironmentForm/>
      </div>
    </div>
  </div>
);

export default ProtectedEnvironmentPage;
