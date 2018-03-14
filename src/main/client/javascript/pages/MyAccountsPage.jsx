import { CircularProgress } from 'material-ui';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { fetchMyWalletAccounts } from '../actions/wallets';
import * as model from '../model';
import { getAllWalletAccounts, listWalletAccountsIsLoading } from '../reducers';
import CreateWallet from '../components/wallet/CreateWallet';
import MyAccounts from '../components/wallet/MyAccounts';
import './MyAccountsPage.less';


class MyAccountsPage extends React.Component {
  static propTypes = {
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    walletAccountsLoading: PropTypes.bool.isRequired,
    walletAccounts: PropTypes.arrayOf(model.walletAccount),
  };

  static defaultProps = {
    walletAccounts: undefined,
  };

  componentDidMount() {
    this.props.fetchMyWalletAccounts();
  }

  render() {
    return (
      <div className="my-accounts-page">
        <div className="container">
          {this.props.walletAccountsLoading && <CircularProgress/>}

          {!this.props.walletAccountsLoading && this.props.walletAccounts &&
          <MyAccounts walletAccounts={this.props.walletAccounts}/>
          }

          {!this.props.walletAccountsLoading && !this.props.walletAccounts &&
          <CreateWallet/>
          }
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  walletAccounts: getAllWalletAccounts(state),
  walletAccountsLoading: listWalletAccountsIsLoading(state),
});

const mapDispatchToProps = {
  fetchMyWalletAccounts,
};

export default connect(mapStateToProps, mapDispatchToProps)(MyAccountsPage);
