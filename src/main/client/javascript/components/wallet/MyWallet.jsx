import { Button, Grid } from 'material-ui';
import PropTypes from 'prop-types';
import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import Add from 'material-ui-icons/Add';
import { Link } from 'react-router';
import WalletIcon from '../../../images/icon-wallet.png';
import { fetchWalletBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getTotalBalance } from '../../reducers';
import CurrencyBalance from '../common/CurrencyBalance';
import './MyWallet.less';
import WalletAccountListItem from './WalletAccountListItem';

const MyWalletHeader = ({ totalBalance }) => (
  <Fragment>
    <Grid item xs={12} sm={5} md={6} lg={7}>
      <div className="wallet-header">
        <img src={WalletIcon} className="icon-wallet" alt="Wallet icon"/>
        <div className="details">
          <h1 className="display-1 inline-title"><strong>My Wallet</strong></h1>
        </div>
      </div>
    </Grid>
    <Grid item xs={12} sm={7} md={6} lg={5}>
      <div className="header-right">
        <CurrencyBalance
          title="Wallet Balance"
          value={totalBalance}
          code="ETH"
        />
        <div className="wallet-actions">
          <div className="wallet-action">
            <Button
              component={Link}
              to="/addAddress"
              className="btn-primary"
              size="small"
            >
              <Add className="btn-icon-left"/>
              Add
            </Button>
          </div>
        </div>
      </div>
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
