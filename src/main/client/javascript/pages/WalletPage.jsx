import { CircularProgress, Snackbar } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import { acknowledgeSentTransaction, fetchMyWalletAccounts } from '../actions/wallets';
import MyWallet from '../components/wallet/MyWallet';
import * as model from '../model';
import { getFirstWalletAccount, getLastSentTransactionId, listWalletAccountsIsLoading } from '../reducers';
import CreateWallet from '../components/wallet/CreateWallet';
import './WalletContainer.less';


class WalletPage extends React.Component {
  static propTypes = {
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    walletAccountsLoading: PropTypes.bool.isRequired,
    handleCloseSnackbar: PropTypes.bool.isRequired,
    lastSentTransactionId: PropTypes.string,
    walletAccount: model.walletAccount,
  };

  static defaultProps = {
    walletAccount: undefined,
    lastSentTransactionId: undefined,
  };

  componentDidMount() {
    this.props.fetchMyWalletAccounts();
  }

  render() {
    const { lastSentTransactionId, handleCloseSnackbar, walletAccount } = this.props;
    return (
      <div className="wallet-container">
        {!!walletAccount &&
          <Snackbar
            open={!!lastSentTransactionId}
            onClose={() => handleCloseSnackbar(walletAccount.address)}
            SnackbarContentProps={{
              'aria-describedby': 'message-id',
            }}
            message={
              <span id="message-id">Your transaction has been submitted.
              You will be able to track progress shortly by <Link target="_blank" href={`https://etherscan.io/address/${walletAccount.address}`}>clicking here</Link>.
              </span>}
          />
        }
        <div className="container">
          <div className="widgets">
            {this.props.walletAccountsLoading && <CircularProgress/>}

            {!this.props.walletAccountsLoading && walletAccount &&
            <MyWallet walletAccount={walletAccount}/>
            }

            {!this.props.walletAccountsLoading && !walletAccount &&
            <CreateWallet/>
            }
          </div>
        </div>
      </div>

    );
  }
}

const mapStateToProps = state => ({
  walletAccount: getFirstWalletAccount(state),
  walletAccountsLoading: listWalletAccountsIsLoading(state),
  lastSentTransactionId: getFirstWalletAccount(state) &&
    getLastSentTransactionId(state, getFirstWalletAccount(state).address),
});

const mapDispatchToProps = {
  fetchMyWalletAccounts,
  handleCloseSnackbar: address => acknowledgeSentTransaction(address),
};

export default connect(mapStateToProps, mapDispatchToProps)(WalletPage);
