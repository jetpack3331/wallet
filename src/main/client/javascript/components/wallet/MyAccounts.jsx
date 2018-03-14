import React from 'react';
import PropTypes from 'prop-types';
import { Grid } from 'material-ui';
import { connect } from 'react-redux';
import WalletIcon from '../../../images/icon-wallet.png';
import * as model from '../../model';
import { fetchWalletBalance } from '../../actions/wallets';
import { getTotalBalance } from '../../reducers';
import EtherDisplay from '../EtherDisplay';
import WalletAccountListItem from './WalletAccountListItem';
import './MyAccounts.less';

const MyAccounts = ({ walletAccounts, totalBalance }) => (
  <Grid className="my-accounts" container spacing={24}>
    <Grid item xs={12} sm={7} md={6} lg={7}>
      <div className="account-header">
        <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
        <div className="details">
          <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
        </div>
      </div>
    </Grid>
    <Grid item xs={12} sm={5} md={6} lg={5}>
      {totalBalance &&
      <p className="total-balance">
        <strong>Total Balance</strong>
        <span className="value">
          <EtherDisplay className="label" value={totalBalance}/>
          <span className="currency">ETH</span>
        </span>
      </p>
      }
    </Grid>
    <Grid item xs={12}>
      <div className="wallet-account-list">
        {walletAccounts.map(walletAccount => (
          <WalletAccountListItem walletAccount={walletAccount} key={walletAccount.address}/>
        ))}
      </div>
    </Grid>
  </Grid>
);

MyAccounts.propTypes = {
  walletAccounts: PropTypes.arrayOf(model.walletAccount).isRequired,
  totalBalance: PropTypes.object,
};

MyAccounts.defaultProps = {
  totalBalance: undefined,
};

const mapStateToProps = state => ({
  totalBalance: getTotalBalance(state),
});

const mapDispatchToProps = {
  fetchWalletBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(MyAccounts);
