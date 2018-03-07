import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import NoWalletIcon from '../../images/icon-no-wallet.png';
import { createWalletAccount } from '../actions/wallets';
import CreateWalletForm from '../components/forms/CreateWalletForm';
import { walletAccount } from '../model';
import { getFirstWalletAccount } from '../reducers';
import './HomePage.less';

const WalletPage = props => (
  <div className="wallet-page">
    <div className="container">
      <div className="widgets">
        <div>
          {props.walletAccount &&
          <div className="row wallets">
            <h1 className="display-1"><strong>My Wallet</strong></h1>
            <p>Wallet created: {props.walletAccount.address}</p>
          </div>
          }

          {!props.walletAccount &&
          <div className="row no-wallet">
            <div className="main-icon"><img className="icon no-wallet" src={NoWalletIcon} alt="No Wallet"/></div>
            <h1 className="display-1"><strong>No Wallet</strong></h1>
            <p>Choose a password to create one now!</p>
            <p>
              <strong>Important: </strong> We have no knowledge of your password and
              cannot help if it is lost or forgotten. <br/>
              Please take care in choosing a secure password, and also making sure you remember it.
            </p>
            <CreateWalletForm
              onSubmit={props.createWalletAccount}
            />
          </div>
          }


        </div>
      </div>
    </div>
    <footer><p className="footer-disclaimer">2018&copy; Hut34 Wallet | The Open Source Crypto Wallet</p></footer>
  </div>

);

WalletPage.propTypes = {
  createWalletAccount: PropTypes.func.isRequired,
  walletAccount,
};

WalletPage.defaultProps = {
  walletAccount: undefined,
};

const mapStateToProps = state => ({
  walletAccount: getFirstWalletAccount(state),
});

const mapDispatchToProps = {
  createWalletAccount,
};


export default connect(mapStateToProps, mapDispatchToProps)(WalletPage);
