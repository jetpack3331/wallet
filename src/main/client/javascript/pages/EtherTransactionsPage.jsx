import { Grid } from 'material-ui';
import ChevronLeft from 'material-ui-icons/ChevronLeft';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import { fetchMyWalletAccounts } from '../actions/wallets';
import AddressWithBalance from '../components/wallet/AddressWithBalance';
import WalletTransactions from '../components/wallet/WalletTransactions';
import * as model from '../model';
import { getWalletAccount } from '../reducers';
import './WalletContainer.less';

class EtherTransactionsPage extends React.Component {
  static propTypes = {
    fetchMyWalletAccounts: PropTypes.func.isRequired,
    walletAccount: model.walletAccount,
  };

  static defaultProps = {
    walletAccount: undefined,
  };

  componentDidMount() {
    this.props.fetchMyWalletAccounts();
  }

  render() {
    const { params: { walletAddress }, walletAccount } = this.props;

    return (
      <div className="wallet-container ether-transactions">
        <div className="container">
          <div className="widgets">
            <div className="top-actions">
              <Link to={`/addresses/${walletAddress}`} className="back-link"><ChevronLeft/><span>Back to address</span></Link>
            </div>
            {walletAccount &&
            <div>
              <AddressWithBalance walletAccount={walletAccount} />
              <Grid container spacing={24}>
                <Grid className="form-container" item xs={12}>
                  <WalletTransactions address={walletAddress}/>
                </Grid>
              </Grid>
            </div>
            }
          </div>
        </div>
      </div>
    );
  }
}

EtherTransactionsPage.propTypes = {
  params: PropTypes.object.isRequired,
};

const mapStateToProps = (state, props) => ({
  walletAccount: getWalletAccount(state, props.params.walletAddress),
});

const mapDispatchToProps = {
  fetchMyWalletAccounts,
};

export default connect(mapStateToProps, mapDispatchToProps)(EtherTransactionsPage);

