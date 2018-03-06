import { Button } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { createWalletAccount } from '../actions/wallets';
import './HomePage.less';
import { walletAccount } from '../model';
import { getFirstWalletAccount } from '../reducers';
import NoWalletIcon from '../../images/icon-no-wallet.png';

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
            <p>Add one now</p>
            <Button
              className="btn-primary"
              variant="raised"
              onClick={() => props.createWalletAccount('somepassword123')}
            >
              Create Wallet Account
            </Button>
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
