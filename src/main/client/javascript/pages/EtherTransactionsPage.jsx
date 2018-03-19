import { Grid } from 'material-ui';
import ChevronLeft from 'material-ui-icons/ChevronLeft';
import * as PropTypes from 'prop-types';
import React from 'react';
import { Link } from 'react-router';
import EtherBalance from '../components/common/EtherBalance';
import WalletTransactions from '../components/wallet/WalletTransactions';
import './WalletContainer.less';

const EtherTransactionsPage = ({ params: { walletAddress } }) => (
  <div className="wallet-container">
    <div className="container">
      <div className="widgets">
        <Grid container spacing={24}>
          <Grid item xs={12} sm={7} md={7} lg={7}>
            <Grid item xs={12}>
              <div className="top-actions">
                <Link to={`/addresses/${walletAddress}`} className="back-link"><ChevronLeft/><span>Back to address</span></Link>
              </div>
            </Grid>
            <div className="wallet-header">
              <div className="details">
                <h1 className="display-1 inline-title"><strong>Ether Transactions</strong></h1>
                <p className="wallet-address" title="Wallet Address"><strong>Address:</strong> {walletAddress}</p>
              </div>
            </div>
          </Grid>
          <Grid item xs={12} sm={5} md={5} lg={5}>
            <EtherBalance walletAddress={walletAddress}/>
          </Grid>
          <Grid className="form-container" item xs={12}>
            <WalletTransactions address={walletAddress}/>
          </Grid>
        </Grid>
      </div>
    </div>
  </div>
);

EtherTransactionsPage.propTypes = {
  params: PropTypes.object.isRequired,
};

export default EtherTransactionsPage;