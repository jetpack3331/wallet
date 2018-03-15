import React from 'react';
import * as PropTypes from 'prop-types';
import { Button, CircularProgress, Snackbar } from 'material-ui';
import { connect } from 'react-redux';
import { acknowledgeSentTransaction, fetchMyWalletAccounts } from '../actions/wallets';
import ViewAddress from '../components/wallet/ViewAddress';
import * as model from '../model';
import { getLastSentTransactionId, getWalletAccount, listWalletAccountsIsLoading } from '../reducers';
import CreateWallet from '../components/wallet/CreateWallet';
import './WalletContainer.less';


class ViewAddressPage extends React.Component {
  static propTypes = {
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    walletAccountsLoading: PropTypes.bool.isRequired,
    handleCloseSnackbar: PropTypes.func.isRequired,
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

    const handleClose = () => handleCloseSnackbar(walletAccount.address);

    return (
      <div className="wallet-container view-address-container">
        {!!walletAccount &&
        <Snackbar
          open={!!lastSentTransactionId}
          onClose={handleClose}
          SnackbarContentProps={{
            'aria-describedby': 'message-id',
          }}
          message={
            <span id="message-id">
              Your transaction has been submitted and will appear once processed.
              Please wait for it to be complete before sending another transaction.
            </span>}
          action={[
            <Button
              key="track"
              aria-label="Track on Etherscan"
              variant="flat"
              size="small"
              color="secondary"
              target="_blank"
              href={`https://etherscan.io/address/${walletAccount.address}`}
            >
              Track on Etherscan
            </Button>,
            <Button
              key="close"
              aria-label="Close"
              variant="flat"
              size="small"
              color="secondary"
              onClick={handleClose}
            >
              Close
            </Button>,
          ]}
        />
        }
        <div className="container">
          <div className="widgets">
            {this.props.walletAccountsLoading && <CircularProgress/>}

            {!this.props.walletAccountsLoading && walletAccount &&
            <ViewAddress walletAccount={walletAccount}/>
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

const mapStateToProps = (state, props) => ({
  walletAccount: getWalletAccount(state, props.params.walletAddress),
  walletAccountsLoading: listWalletAccountsIsLoading(state),
  lastSentTransactionId: getWalletAccount(state, props.params.walletAddress) &&
  getLastSentTransactionId(state, props.params.walletAddress),
});

const mapDispatchToProps = {
  fetchMyWalletAccounts,
  handleCloseSnackbar: address => acknowledgeSentTransaction(address),
};

export default connect(mapStateToProps, mapDispatchToProps)(ViewAddressPage);
