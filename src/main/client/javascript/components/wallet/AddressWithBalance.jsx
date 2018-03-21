import { bigNumberify } from 'ethers/utils/index';
import { Button, Grid } from 'material-ui';
import SendIcon from 'material-ui-icons/Send';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import { push } from 'react-router-redux';
import KeysIcon from '../../../images/icons/icon-keys.png';
import EthereumLogo from '../../../images/logos/ethereum-logo.png';
import { fetchWalletBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getTokens, getWalletBalance } from '../../reducers';
import CurrencyBalance from '../common/CurrencyBalance';
import RoundLogo from '../common/RoundLogo';
import DownloadKeystoreButton from './DownloadKeystoreButton';
import ViewPrivateKeyButton from './ViewPrivateKeyButton';

class AddressWithBalance extends React.Component {
  static propTypes = {
    walletAccount: model.walletAccount.isRequired,
    fetchWalletBalance: PropTypes.func.isRequired,
    navigateSendEther: PropTypes.func.isRequired,
    walletBalance: model.walletBalance,
    linkToTransactions: PropTypes.bool,
  };

  static defaultProps = {
    walletBalance: undefined,
    linkToTransactions: false,
  };

  componentDidMount() {
    this.props.fetchWalletBalance();
  }

  render() {
    const { walletBalance, walletAccount, linkToTransactions } = this.props;
    const { address: walletAddress } = walletAccount;
    const nonZeroBalance = walletBalance && bigNumberify(walletBalance.balance).gt(0);

    return (
      <div className="address-with-balance">
        <Grid container spacing={0}>
          <Grid item xs={12} sm={7} md={8}>
            <div className="wallet-header">
              <img src={KeysIcon} className="icon-wallet" alt="Address icon"/>
              <div className="details">
                <h1 className="display-1 inline-title"><strong>My Address</strong></h1>
                <p className="wallet-address" title="Wallet Address">{walletAddress}</p>
              </div>
            </div>
          </Grid>
          <Grid item xs={12} sm={5} md={4}>
            <div className="wallet-actions">
              <div className="wallet-action">
                <DownloadKeystoreButton walletAccount={walletAccount}/>
                <ViewPrivateKeyButton walletAccount={walletAccount}/>
              </div>
            </div>
          </Grid>
        </Grid>

        <Grid className="wallet-section" container spacing={0}>
          <Grid item xs={12} sm={6} lg={8}>
            <div className="section-main">
              <div className="icon-wrapper">
                <RoundLogo className="icon" src={EthereumLogo}/>
              </div>
              <div className="details">
                <div className="section-title">Ether</div>
                {linkToTransactions &&
                <div className="sub-text"><Link to={`/addresses/${walletAddress}/transactions/eth`}>View transactions</Link></div>
                }
              </div>
            </div>
          </Grid>
          <Grid className="section-actions" item xs={12} sm={6} lg={4}>
            <CurrencyBalance
              title="Balance"
              value={walletBalance && walletBalance.balance}
              code="ETH"
            />
            {nonZeroBalance &&
            <Button
              className="btn-secondary"
              variant="raised"
              size="small"
              onClick={this.props.navigateSendEther}
            >
              <SendIcon className="icon-small btn-icon-left"/>
              Send
            </Button>
            }
          </Grid>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = (state, props) => ({
  walletBalance: getWalletBalance(state, props.walletAccount.address),
  tokens: getTokens(state),
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  fetchWalletBalance: () => dispatch(fetchWalletBalance(ownProps.walletAccount.address)),
  navigateSendEther: () => dispatch(push(`/addresses/${ownProps.walletAccount.address}/send`)),
});

export default connect(mapStateToProps, mapDispatchToProps)(AddressWithBalance);
