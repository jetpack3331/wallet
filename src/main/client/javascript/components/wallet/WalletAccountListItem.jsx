import React from 'react';
import PropTypes from 'prop-types';
import { Grid } from 'material-ui';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import * as model from '../../model';
import { fetchWalletBalance } from '../../actions/wallets';
import { getWalletBalance } from '../../reducers';
import EtherDisplay from '../EtherDisplay';
import './WalletAccountListItem.less';

class WalletAccountListItem extends React.Component {
  static propTypes = {
    walletAccount: model.walletAccount.isRequired,
    fetchAccountBalance: PropTypes.func.isRequired,
    accountBalance: PropTypes.object,
  };

  static defaultProps = {
    accountBalance: undefined,
  };

  componentDidMount() {
    this.props.fetchAccountBalance(this.props.walletAccount.address);
  }

  render() {
    const { walletAccount, accountBalance } = this.props;
    return (
      <div className="wallet-account-list-item" key={walletAccount.address}>
        <Link to={`/addresses/${walletAccount.address}`}>
          <Grid container spacing={24}>
            <Grid item xs={6}>
              <h2 className="title account-name"><strong>Ethereum</strong></h2>
              <p>{walletAccount.address}</p>
            </Grid>
            <Grid item xs={6}>
              <div className="flex-right">
                <p className="account-balance">
                  <strong>Balance</strong>
                  <span className="value">
                    {accountBalance &&
                    <EtherDisplay className="label" value={accountBalance.balance}/>
                    }
                    <span className="currency">ETH</span>
                  </span>
                </p>
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
});

const mapDispatchToProps = {
  fetchAccountBalance: fetchWalletBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(WalletAccountListItem);
