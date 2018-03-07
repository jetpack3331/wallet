import { CircularProgress, Grid } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import NoWalletIcon from '../../images/icon-no-wallet.png';
import { createWalletAccount, fetchMyWalletAccounts } from '../actions/wallets';
import CreateWalletForm from '../components/forms/CreateWalletForm';
import { walletAccount } from '../model';
import { getFirstWalletAccount, listWalletAccountsIsLoading } from '../reducers';
import './WalletPage.less';
import DownloadKeystoreButton from '../components/wallet/DownloadKeystoreButton';

const iconWallet = require('../../images/icon-wallet.png');

class WalletPage extends React.Component {
  static propTypes = {
    createWalletAccount: PropTypes.func.isRequired,
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    walletAccountsLoading: PropTypes.bool.isRequired,
    walletAccount,
  };

  static defaultProps = {
    walletAccount: undefined,
  };

  componentDidMount() {
    this.props.fetchMyWalletAccounts();
  }

  render() {
    return (
      <div className="wallet-page">
        <div className="container">
          <div className="widgets">
            <div>
              {this.props.walletAccountsLoading && <CircularProgress/>}

              {!this.props.walletAccountsLoading && this.props.walletAccount &&
              <Grid container spacing={24}>
                <Grid item xs={12} sm={6}>
                  <img src={iconWallet} className="icon-wallet" alt="Wallet icon"/>
                  <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <DownloadKeystoreButton walletAccount={this.props.walletAccount}/>
                </Grid>
                <Grid item xs={12}>
                  <p>Wallet created: {this.props.walletAccount.address}</p>
                </Grid>
              </Grid>
              }

              {!this.props.walletAccountsLoading && !this.props.walletAccount &&
              <div className="row no-wallet">
                <div className="main-icon"><img className="icon no-wallet" src={NoWalletIcon} alt="No Wallet"/></div>
                <h1 className="display-1"><strong>No Wallet</strong></h1>
                <p>Choose a password to create one now!</p>
                <p>
                  <strong>Important: </strong> We have no knowledge of your password and
                  cannot help if it is lost or forgotten. <br/>
                  Please take care in choosing a secure password, and also making sure you
                  remember it.
                </p>
                <CreateWalletForm
                  onSubmit={this.props.createWalletAccount}
                />
              </div>
              }


            </div>
          </div>
        </div>
        <footer><p className="footer-disclaimer">2018&copy; Hut34 Wallet | The Open Source Crypto Wallet</p></footer>
      </div>

    );
  }
}

const mapStateToProps = state => ({
  walletAccount: getFirstWalletAccount(state),
  walletAccountsLoading: listWalletAccountsIsLoading(state),
});

const mapDispatchToProps = {
  createWalletAccount,
  fetchMyWalletAccounts,
};


export default connect(mapStateToProps, mapDispatchToProps)(WalletPage);
