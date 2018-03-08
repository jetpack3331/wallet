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
        <Grid item xs={12} sm={6}>
          <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
          <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
        </Grid>
        <Grid item xs={12} sm={6}>
          <DownloadKeystoreButton walletAccount={this.props.walletAccount}/>
        </Grid>
        <Grid item xs={12}>
          <p>Wallet address: {this.props.walletAccount.address}</p>
          {this.props.walletBalance &&
            <p>Wallet balance: <EtherDisplay value={this.props.walletBalance.balance}/></p>
          }
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
