import React from 'react';
import { Link } from 'react-router';
import './HomePage.less';
import NoWalletIcon from '../../images/icon-no-wallet.png';
import GoogleIcon from '../../images/g-logo.png';

const HomePage = () => (
  <div className="home-page">
    <div className="container">
      <div className="login-container">
        <div className="main-icon"><img className="logo" src={NoWalletIcon} alt="HUT34 Wallet"/></div>
        <h1 className="display-1"><strong>Sign in to create a wallet</strong></h1>


        <p>Sign in using your Google account.</p>
        <Link class="google-login" to="/login/google" target="_self"><img className="google-icon" src={GoogleIcon} alt="G"/> Sign in using Google</Link>
      </div>
    </div>
    <footer><p className="footer-disclaimer">2018&copy; Hut34 Wallet | The Open Source Crypto Wallet</p></footer>
  </div>
);

export default HomePage;
