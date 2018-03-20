import { CircularProgress, Grid } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import { fetchGasPrices } from '../actions/gasPrices';
import { sendTokens } from '../actions/transactions';
import { fetchMyWalletAccounts, fetchTokenBalanceBySymbol } from '../actions/wallets';
import CurrencyBalance from '../components/common/CurrencyBalance';
import SendCryptoForm from '../components/forms/SendCryptoForm';
import * as model from '../model';
import { getBalanceByTokenSymbolAndAddress, getGasPrices, getToken, getWalletAccount } from '../reducers';
import './SendEtherPage.less';
import './WalletContainer.less';

class SendTokenPage extends React.Component {
  static propTypes = {
    token: model.token.isRequired,
    fetchTokenBalance: PropTypes.func.isRequired,
    sendTokens: PropTypes.func.isRequired,
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    fetchGasPrices: PropTypes.func.isRequired,
    cancelSend: PropTypes.func.isRequired,
    params: PropTypes.object.isRequired,
    walletAccount: model.walletAccount,
    tokenBalance: model.walletTokenBalance,
    gasPrices: model.gasPrices,
  };

  static defaultProps = {
    walletAccount: undefined,
    tokenBalance: undefined,
    gasPrices: undefined,
  };

  componentDidMount() {
    if (!this.props.walletAccount) {
      this.props.fetchMyWalletAccounts();
    }
    this.props.fetchGasPrices();
    this.props.fetchTokenBalance();
  }


  render() {
    const { walletAddress } = this.props.params;
    const {
      walletAccount, tokenBalance, token, gasPrices,
    } = this.props;
    const formDataLoading = !walletAccount || !tokenBalance || !gasPrices;

    return (
      <div className="wallet-container send-ether-page">
        <div className="container">
          <div className="widgets">
            <Grid container spacing={24}>
              <Grid item xs={12} sm={7} md={7} lg={7}>
                <div className="wallet-header">
                  <div className="details">
                    <h1 className="display-1 inline-title"><strong>Send {token.name}</strong></h1>
                    <p className="wallet-address" title="Wallet Address"><strong>From address:</strong> {walletAddress}</p>
                  </div>
                </div>
              </Grid>
              <Grid item xs={12} sm={5} md={5} lg={5}>
                <CurrencyBalance
                  title="Current Balance"
                  value={tokenBalance && tokenBalance.balance}
                  code={token.symbol}
                  decimals={token.decimals}
                />
              </Grid>
              <Grid className="form-container" item xs={12}>
                {formDataLoading && <CircularProgress/>}
                {!formDataLoading && !walletAccount &&
                  <span>You do not own a wallet with address: {walletAddress}</span>
                }
                {!!walletAccount && !formDataLoading &&
                  <SendCryptoForm
                    onSubmit={this.props.sendTokens}
                    onCancel={() => this.props.cancelSend()}
                    balance={tokenBalance.balance}
                    token={token}
                    showPassword
                  />
                }
              </Grid>
            </Grid>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state, { params }) => ({
  walletAccount: getWalletAccount(state, params.walletAddress),
  token: getToken(state, params.tokenSymbol),
  tokenBalance: getBalanceByTokenSymbolAndAddress(state, params.tokenSymbol, params.walletAddress),
  gasPrices: getGasPrices(state),
});

const mapDispatchToProps = (dispatch, { params }) => ({
  fetchMyWalletAccounts: () => dispatch(fetchMyWalletAccounts()),
  sendTokens: data => dispatch(sendTokens(data, params.walletAddress, params.tokenSymbol, 'averagePrice'))
    .then(() => dispatch(push(`/addresses/${params.walletAddress}`))),
  fetchTokenBalance: () =>
    dispatch(fetchTokenBalanceBySymbol(params.walletAddress, params.tokenSymbol)),
  cancelSend: () => dispatch(push(`/addresses/${params.walletAddress}`)),
  fetchGasPrices: () => dispatch(fetchGasPrices()),
});

export default connect(mapStateToProps, mapDispatchToProps)(SendTokenPage);
