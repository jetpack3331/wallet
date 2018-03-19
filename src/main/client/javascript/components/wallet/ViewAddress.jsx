import { Grid } from 'material-ui';
import ChevronLeft from 'material-ui-icons/ChevronLeft';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import * as model from '../../model';
import { getTokens } from '../../reducers';
import TokenBalance from '../common/TokenBalance';
import AddressWithBalance from './AddressWithBalance';
import './ViewAddress.less';

const ViewAddress = ({ walletAccount, tokens }) => (
  <div className="view-address">
    <div className="top-actions">
      <Link to="/" className="back-link"><ChevronLeft/><span>Back to wallet</span></Link>
    </div>
    <AddressWithBalance walletAccount={walletAccount} linkToTransactions />

    <h2 className="tokens-title">Tokens</h2>
    <Grid className="tokens" container spacing={0}>
      {tokens.map(tok => (
        <Grid key={tok.address} className="token" item xs={6} sm={6} md={3}>
          <TokenBalance token={tok} walletAddress={walletAccount.address} detailed />
        </Grid>))}
    </Grid>

  </div>
);

ViewAddress.propTypes = {
  walletAccount: model.walletAccount.isRequired,
  tokens: PropTypes.arrayOf(model.token).isRequired,
};

const mapStateToProps = state => ({
  tokens: getTokens(state),
});

export default connect(mapStateToProps)(ViewAddress);
