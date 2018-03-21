import { Grid } from 'material-ui';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import KeysIcon from '../../../images/icons/icon-keys.png';
import { fetchWalletBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getTokens, getWalletBalance } from '../../reducers';
import CurrencyBalance from '../common/CurrencyBalance';
import TokenBalance from '../common/TokenBalance';
import './WalletAccountListItem.less';

class WalletAccountListItem extends React.Component {
  static propTypes = {
    walletAccount: model.walletAccount.isRequired,
    fetchAccountBalance: PropTypes.func.isRequired,
    tokens: PropTypes.arrayOf(model.token).isRequired,
    accountBalance: PropTypes.object,
  };

  static defaultProps = {
    accountBalance: undefined,
  };

  componentDidMount() {
    this.props.fetchAccountBalance(this.props.walletAccount.address);
  }

  render() {
    const { walletAccount, accountBalance, tokens } = this.props;
    const isPrivate = walletAccount.type === 'PRIVATE';
    return (
      <div className="wallet-account-list-item" key={walletAccount.address}>
        <Link to={`/addresses/${walletAccount.address}`}>
          <Grid container spacing={24}>
            <Grid item xs={12} sm={6}>
              <div className="item-main">
                <img src={KeysIcon} className="icon-wallet" alt="Address icon"/>
                <div>
                  <h2 className="title account-name">Address
                    {isPrivate && <small> (password secured)</small>}
                  </h2>
                  <p className="account-address">{walletAccount.address}</p>
                </div>
              </div>
            </Grid>
            <Grid item xs={12} sm={6}>
              <CurrencyBalance
                title="Balance"
                value={accountBalance && accountBalance.balance}
                code="ETH"
              />
            </Grid>
            {tokens.map(tok => (
              <Grid
                key={model.tokenBalanceId(tok.address, walletAccount.address)}
                item
                xs={6}
                sm={4}
                md={2}
              >
                <TokenBalance
                  walletAddress={walletAccount.address}
                  token={tok}
                />
              </Grid>))}
          </Grid>
        </Link>
      </div>
    );
  }
}

const mapStateToProps = (state, props) => ({
  accountBalance: getWalletBalance(state, props.walletAccount.address),
  tokens: getTokens(state),
});

const mapDispatchToProps = {
  fetchAccountBalance: fetchWalletBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(WalletAccountListItem);
