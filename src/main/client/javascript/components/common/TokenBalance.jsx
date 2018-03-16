import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { fetchTokenBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getBalanceByContractAddressAndAddress } from '../../reducers';
import CurrencyBalance from './CurrencyBalance';
import './TokenBalance.less';

class TokenBalance extends React.Component {
  static propTypes = {
    walletAddress: PropTypes.string.isRequired,
    fetchTokenBalance: PropTypes.func.isRequired,
    token: model.token.isRequired,
    balance: model.walletTokenBalance,
  };

  static defaultProps = {
    balance: undefined,
  };


  componentDidMount() {
    const { token, walletAddress } = this.props;
    this.props.fetchTokenBalance(token.address, walletAddress);
  }


  render() {
    const { token, balance } = this.props;
    return (
      <CurrencyBalance
        className="token-balance"
        value={balance && balance.balance}
        title={token.symbol}
        decimals={token.decimals}
        strong={false}
      />
    );
  }
}

const mapStateToProps = (state, { walletAddress, token }) => ({
  balance: getBalanceByContractAddressAndAddress(state, token.address, walletAddress),
});
const mapDispatchToProps = {
  fetchTokenBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(TokenBalance);
