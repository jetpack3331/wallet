import React from 'react';
import PropTypes from 'prop-types';
import { Button, Grid } from 'material-ui';
import ArrowForward from 'material-ui-icons/ArrowForward';
import ChevronLeft from 'material-ui-icons/ChevronLeft';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import { Link } from 'react-router';
import WalletIcon from '../../../images/icon-wallet.png';
import { fetchWalletBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getWalletBalance } from '../../reducers';
import EtherDisplay from '../EtherDisplay';
import DownloadKeystoreButton from './DownloadKeystoreButton';
import ViewPrivateKeyButton from './ViewPrivateKeyButton';
import WalletTransactions from './WalletTransactions';
import './MyWallet.less';

class MyWallet extends React.Component {
  static propTypes = {
    walletAccount: model.walletAccount.isRequired,
    fetchWalletBalance: PropTypes.func.isRequired,
    navigateSendEther: PropTypes.func.isRequired,
    walletBalance: model.walletBalance,
  };

  static defaultProps = {
    walletBalance: undefined,
  };

  componentDidMount() {
    this.props.fetchWalletBalance();
  }

  render() {
    const { walletBalance, walletAccount } = this.props;

    return (
      <Grid className="my-wallet" container spacing={0}>
        <Grid item xs={12}>
          <div className="top-actions">
            <Link to="/" className="back-link"><ChevronLeft/><span>Back to wallet</span></Link>
          </div>
        </Grid>
        <Grid item xs={12} sm={7} md={6} lg={7}>
          <div className="wallet-header">
            <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
            <div className="details">
              <h1 className="display-1 inline-title"><strong>My Address</strong></h1>
              <p className="wallet-address" title="Wallet Address"><strong>Address:</strong> {walletAccount.address}</p>
            </div>
          </div>
        </Grid>
        <Grid item xs={12} sm={5} md={3} lg={3}>
          {walletBalance &&
          <p className="wallet-balance">
            <strong>Wallet Balance</strong>
            <span className="value">
              <EtherDisplay className="label" value={walletBalance.balance}/>
              <span className="currency">ETH</span>
            </span>
          </p>
          }
        </Grid>
        <Grid item xs={12} sm={12} md={3} lg={2}>
          <div className="wallet-actions">
            <div className="wallet-action">
              <DownloadKeystoreButton walletAccount={walletAccount}/>
              <ViewPrivateKeyButton walletAccount={walletAccount}/>
            </div>
          </div>
        </Grid>
        <Grid item xs={12}>
          <div className="wallet-actions">
            {walletBalance && walletBalance.balance && walletBalance.balance > 0 &&
            <div className="wallet-action">
              <Button
                className="btn-secondary"
                variant="raised"
                size="small"
                onClick={this.props.navigateSendEther}
              >
                <ArrowForward className="btn-icon-left"/>
                Send Ether
              </Button>
            </div>
            }
          </div>
          <WalletTransactions address={walletAccount.address}/>
        </Grid>
      </Grid>
    );
  }
}

const mapStateToProps = (state, props) => ({
  walletBalance: getWalletBalance(state, props.walletAccount.address),
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  fetchWalletBalance: () => dispatch(fetchWalletBalance(ownProps.walletAccount.address)),
  navigateSendEther: () => dispatch(push(`/addresses/${ownProps.walletAccount.address}/send`)),
});

export default connect(mapStateToProps, mapDispatchToProps)(MyWallet);
