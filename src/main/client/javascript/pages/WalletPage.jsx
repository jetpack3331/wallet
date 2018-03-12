import { CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { fetchMyWalletAccounts } from '../actions/wallets';
import MyWallet from '../components/wallet/MyWallet';
import { walletAccount } from '../model';
import { getFirstWalletAccount, listWalletAccountsIsLoading } from '../reducers';
import CreateWallet from '../components/wallet/CreateWallet';
import './WalletPage.less';


class WalletPage extends React.Component {
  static propTypes = {
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
            {this.props.walletAccountsLoading && <CircularProgress/>}

            {!this.props.walletAccountsLoading && this.props.walletAccount &&
            <MyWallet walletAccount={this.props.walletAccount}/>
            }

            {!this.props.walletAccountsLoading && !this.props.walletAccount &&
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
});

const mapDispatchToProps = {
  fetchMyWalletAccounts,
};

export default connect(mapStateToProps, mapDispatchToProps)(WalletPage);
