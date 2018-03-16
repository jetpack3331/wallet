import { Grid } from 'material-ui';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
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
    return (
      <div className="wallet-account-list-item" key={walletAccount.address}>
        <Link to={`/addresses/${walletAccount.address}`}>
          <Grid container spacing={24}>
            <Grid item xs={6}>
              <h2 className="title account-name"><strong>Ethereum</strong></h2>
              <p>{walletAccount.address}</p>
            </Grid>
            <Grid item xs={6}>
              <div className="address-balances">
                <CurrencyBalance
                  title="Balance"
                  value={accountBalance && accountBalance.balance}
                  code="ETH"
                />
                <div>
                  {tokens.map(tok =>
                    (<TokenBalance
                      key={model.tokenBalanceId(tok.address, walletAccount.address)}
                      walletAddress={walletAccount.address}
                      token={tok}
                    />))}
                </div>
              </div>
            </Grid>
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
