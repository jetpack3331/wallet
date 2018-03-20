import { bigNumberify } from 'ethers/utils/index';
import { Grid } from 'material-ui';
import ChevronLeft from 'material-ui-icons/ChevronLeft';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import * as model from '../../model';
import { getTokens, getWalletBalance } from '../../reducers';
import TokenBalance from '../common/TokenBalance';
import AddressWithBalance from './AddressWithBalance';
import './ViewAddress.less';

const ViewAddress = ({ walletAccount, walletBalance, tokens }) => {
  const nonZeroBalance = walletBalance && bigNumberify(walletBalance.balance).gt(0);

  return (
    <div className="view-address">
      <div className="top-actions">
        <Link to="/" className="back-link"><ChevronLeft/><span>Back to wallet</span></Link>
      </div>
      <AddressWithBalance walletAccount={walletAccount} linkToTransactions />

      <h2 className="tokens-title">Tokens</h2>
      <Grid className="tokens" container spacing={0}>
        {tokens.map(tok => (
          <Grid key={tok.address} className="token" item xs={6} sm={6} md={3}>
            <TokenBalance
              token={tok}
              walletAddress={walletAccount.address}
              hasEtherBalance={nonZeroBalance}
              detailed
            />
          </Grid>))}
      </Grid>

    </div>
  );
};

ViewAddress.propTypes = {
  walletAccount: model.walletAccount.isRequired,
  walletBalance: model.walletBalance,
  tokens: PropTypes.arrayOf(model.token).isRequired,
};

ViewAddress.defaultProps = {
  walletBalance: undefined,
};

const mapStateToProps = (state, { walletAccount }) => ({
  tokens: getTokens(state),
  walletBalance: getWalletBalance(state, walletAccount.address),
});

export default connect(mapStateToProps)(ViewAddress);
