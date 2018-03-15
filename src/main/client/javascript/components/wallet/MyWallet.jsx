import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Grid } from 'material-ui';
import { connect } from 'react-redux';
import WalletIcon from '../../../images/icon-wallet.png';
import * as model from '../../model';
import { fetchWalletBalance } from '../../actions/wallets';
import { getTotalBalance } from '../../reducers';
import EtherDisplay from '../EtherDisplay';
import WalletAccountListItem from './WalletAccountListItem';
import './MyWallet.less';

const MyWalletHeader = ({ totalBalance }) => (
  <Fragment>
    <Grid item xs={12} sm={7} md={6} lg={7}>
      <div className="wallet-header">
        <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
        <div className="details">
          <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
        </div>
      </div>
    </Grid>
    <Grid item xs={12} sm={5} md={6} lg={5}>
      {totalBalance &&
      <p className="wallet-balance">
        <strong>Total Balance</strong>
        <span className="value">
          <EtherDisplay className="label" value={totalBalance}/>
          <span className="currency">ETH</span>
        </span>
      </p>
      }
    </Grid>
  </Fragment>
);

MyWalletHeader.propTypes = {
  totalBalance: PropTypes.object,
};

MyWalletHeader.defaultProps = {
  totalBalance: undefined,
};


const WalletAccountList = ({ walletAccounts }) => (
  <Grid item xs={12}>
    <div className="wallet-account-list">
      {walletAccounts.map(walletAccount => (
        <WalletAccountListItem walletAccount={walletAccount} key={walletAccount.address}/>
      ))}
    </div>
  </Grid>
);

WalletAccountList.propTypes = {
  walletAccounts: PropTypes.arrayOf(model.walletAccount).isRequired,
};


const MyWallet = ({ walletAccounts, totalBalance }) => (
  <Grid className="my-wallet" container spacing={24}>
    <MyWalletHeader totalBalance={totalBalance}/>
    <WalletAccountList walletAccounts={walletAccounts}/>
  </Grid>
);

MyWallet.propTypes = {
  walletAccounts: PropTypes.arrayOf(model.walletAccount).isRequired,
  totalBalance: PropTypes.object,
};

MyWallet.defaultProps = {
  totalBalance: undefined,
};

const mapStateToProps = state => ({
  totalBalance: getTotalBalance(state),
});

const mapDispatchToProps = {
  fetchWalletBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(MyWallet);
