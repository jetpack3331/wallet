import { CircularProgress, Grid } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import { fetchGasPrices } from '../actions/gasPrices';
import { signAndSendTransaction } from '../actions/transactions';
import { fetchMyWalletAccounts, fetchWalletBalance } from '../actions/wallets';
import CurrencyDisplay from '../components/common/CurrencyDisplay';
import SendEtherForm from '../components/forms/SendEtherForm';
import * as model from '../model';
import { getGasPrices, getWalletAccount, getWalletBalance, listWalletAccountsIsLoading } from '../reducers';
import './SendEtherPage.less';
import './WalletContainer.less';

class SendEtherPage extends React.Component {
  static propTypes = {
    fetchWalletBalance: PropTypes.func.isRequired,
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    signAndSendTransaction: PropTypes.func.isRequired,
    fetchGasPrices: PropTypes.func.isRequired,
    cancelSend: PropTypes.func.isRequired,
    formDataLoading: PropTypes.bool.isRequired,
    params: PropTypes.object.isRequired,
    walletAccount: model.walletAccount,
    walletBalance: model.walletBalance,
    gasPrices: model.gasPrices,
  };

  static defaultProps = {
    walletAccount: undefined,
    walletBalance: undefined,
    gasPrices: undefined,
  };

  componentDidMount() {
    const { params } = this.props;
    if (!this.props.walletAccount) {
      this.props.fetchMyWalletAccounts();
    }
    this.props.fetchGasPrices();
    this.props.fetchWalletBalance(params.walletAddress);
  }


  render() {
    const { walletAddress } = this.props.params;
    const {
      walletBalance, walletAccount, formDataLoading, gasPrices,
    } = this.props;

    return (
      <div className="wallet-container send-ether-page">
        <div className="container">
          <div className="widgets">
            <Grid container spacing={24}>
              <Grid item xs={12} sm={7} md={7} lg={7}>
                <div className="wallet-header">
                  <div className="details">
                    <h1 className="display-1 inline-title"><strong>Send Ether</strong></h1>
                    <p className="wallet-address" title="Wallet Address"><strong>From address:</strong> {walletAddress}</p>
                  </div>
                </div>
              </Grid>
              <Grid item xs={12} sm={5} md={5} lg={5}>
                <div className="wallet-balance">
                  <strong>Current Balance</strong>
                  {!walletBalance && <CircularProgress/>}
                  {!!walletBalance &&
                    <span className="value">
                      <CurrencyDisplay className="label" value={walletBalance.balance} code="ETH" strong/>
                    </span>
                  }
                </div>
              </Grid>
              <Grid className="form-container" item xs={12}>
                {formDataLoading && <CircularProgress/>}
                {!formDataLoading && !walletAccount &&
                  <span>You do not own a wallet with address: {walletAddress}</span>
                }
                {!!walletAccount && !formDataLoading &&
                  <SendEtherForm
                    onSubmit={this.props.signAndSendTransaction}
                    onCancel={() => this.props.cancelSend()}
                    transactionFee={gasPrices.averagePrice * 21000}
                    balance={walletBalance.balance}
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

const mapStateToProps = (state, props) => ({
  walletAccount: getWalletAccount(state, props.params.walletAddress),
  walletBalance: getWalletBalance(state, props.params.walletAddress),
  gasPrices: getGasPrices(state),
  formDataLoading: listWalletAccountsIsLoading(state) || !getGasPrices(state)
    || !getWalletBalance(state, props.params.walletAddress),
});

const mapDispatchToProps = (dispatch, { params }) => ({
  signAndSendTransaction: data =>
    dispatch(signAndSendTransaction(data, params.walletAddress, 21000, 'averagePrice'))
      .then(() => dispatch(push(`/addresses/${params.walletAddress}`))),
  fetchMyWalletAccounts: () => dispatch(fetchMyWalletAccounts()),
  fetchWalletBalance: () => dispatch(fetchWalletBalance(params.walletAddress)),
  fetchGasPrices: () => dispatch(fetchGasPrices()),
  cancelSend: () => dispatch(push(`/addresses/${params.walletAddress}`)),
});

export default connect(mapStateToProps, mapDispatchToProps)(SendEtherPage);
