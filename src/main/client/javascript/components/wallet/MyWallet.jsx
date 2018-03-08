import { Grid } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import WalletIcon from '../../../images/icon-wallet.png';
import { fetchWalletBalance } from '../../actions/wallets';
import { walletAccount, walletBalance } from '../../model';
import { getWalletBalance } from '../../reducers';
import EtherDisplay from '../EtherDisplay';
import DownloadKeystoreButton from './DownloadKeystoreButton';


class MyWallet extends React.Component {
  static propTypes = {
    walletAccount: walletAccount.isRequired,
    fetchWalletBalance: PropTypes.func.isRequired,
    walletBalance,
  };

  static defaultProps = {
    walletBalance: undefined,
  };

  componentDidMount() {
    this.props.fetchWalletBalance(this.props.walletAccount.address);
  }

  render() {
    return (
      <Grid container spacing={24}>
        <Grid item xs={12} sm={7} md={7} lg={7}>
          <div className="wallet-header">
            <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
            <div className="details">
              <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
              <p className="wallet-address" title="Wallet Address"><strong>Address:</strong> {this.props.walletAccount.address}</p>
            </div>
          </div>
        </Grid>
        <Grid item xs={12} sm={5} md={3} lg={4}>
          {this.props.walletBalance &&
            <p className="wallet-balance">
              <strong>Wallet Balance</strong>
              <EtherDisplay value={this.props.walletBalance.balance}/>
            </p>
          }
        </Grid>
        <Grid item xs={12} sm={12} md={2} lg={1}>
          <div className="wallet-actions">
            <DownloadKeystoreButton walletAccount={this.props.walletAccount}/>
          </div>
        </Grid>
      </Grid>
    );
  }
}

const mapStateToProps = (state, props) => ({
  walletBalance: getWalletBalance(state, props.walletAccount.address),
});

const mapDispatchToProps = {
  fetchWalletBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(MyWallet);
