import { Button, Grid } from 'material-ui';
import ChevronLeft from 'material-ui-icons/ChevronLeft';
import SendIcon from 'material-ui-icons/Send';
import PropTypes from 'prop-types';
import React, { Fragment } from 'react';
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
import TokenBalance from '../common/TokenBalance';
import DownloadKeystoreButton from './DownloadKeystoreButton';
import './ViewAddress.less';
import ViewPrivateKeyButton from './ViewPrivateKeyButton';

class ViewAddress extends React.Component {
  static propTypes = {
    walletAccount: model.walletAccount.isRequired,
    fetchWalletBalance: PropTypes.func.isRequired,
    navigateSendEther: PropTypes.func.isRequired,
    walletBalance: model.walletBalance,
    tokens: PropTypes.arrayOf(model.token).isRequired,
  };

  static defaultProps = {
    walletBalance: undefined,
  };

  componentDidMount() {
    this.props.fetchWalletBalance();
  }

  render() {
    const { walletBalance, walletAccount, tokens } = this.props;

    return (
      <Fragment>
        <Grid className="view-address" container spacing={0}>
          <Grid item xs={12}>
            <div className="top-actions">
              <Link to="/" className="back-link"><ChevronLeft/><span>Back to wallet</span></Link>
            </div>
          </Grid>
          <Grid item xs={12} sm={7} md={8}>
            <div className="wallet-header">
              <img src={KeysIcon} className="icon-wallet" alt="Wallet icon"/>
              <div className="details">
                <h1 className="display-1 inline-title"><strong>My Address</strong></h1>
                <p className="wallet-address" title="Wallet Address">{walletAccount.address}</p>
              </div>
            </div>
          </Grid>
          <Grid item xs={12} sm={5} md={4}>
            <div className="wallet-actions">
              <div className="wallet-action">
                {walletAccount.type !== 'MANAGED' &&
                <DownloadKeystoreButton walletAccount={walletAccount}/>
                }
                {walletAccount.type !== 'MANAGED' &&
                <ViewPrivateKeyButton walletAccount={walletAccount}/>
                }
              </div>
            </div>
          </Grid>
        </Grid>

        <Grid className="wallet-section" container spacing={0}>
          <Grid item xs={12} sm={7} lg={8}>
            <div className="section-main">
              <div className="icon-wrapper">
                <RoundLogo className="icon" src={EthereumLogo}/>
              </div>
              <div className="details">
                <div className="section-title">Ether</div>
                <div className="sub-text"><Link to={`/addresses/${walletAccount.address}/transactions/eth`}>View transactions</Link></div>
              </div>
            </div>
          </Grid>
          <Grid item xs={12} sm={3} lg={3}>
            <CurrencyBalance
              title="Balance"
              value={walletBalance && walletBalance.balance}
              code="ETH"
            />
          </Grid>
          <Grid className="section-actions" item xs={12} sm={2} lg={1}>
            <Button
              className="btn-secondary"
              variant="raised"
              size="small"
              onClick={this.props.navigateSendEther}
            >
              <SendIcon className="btn-icon-left"/>
              Send
            </Button>
          </Grid>
        </Grid>

        <Grid container spacing={0}>
          {tokens.map(tok => (
            <Grid item sm={6} md={4}>
              <TokenBalance token={tok} walletAddress={walletAccount.address} />
            </Grid>))}
        </Grid>

      </Fragment>
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

export default connect(mapStateToProps, mapDispatchToProps)(ViewAddress);
