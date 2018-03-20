import PropTypes from 'prop-types';
import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { fetchTokenBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getBalanceByContractAddressAndAddress } from '../../reducers';
import CurrencyBalance from './CurrencyBalance';
import './TokenBalance.less';
import TokenDetails from './TokenDetails';

class TokenBalance extends React.Component {
  static propTypes = {
    walletAddress: PropTypes.string.isRequired,
    fetchTokenBalance: PropTypes.func.isRequired,
    token: model.token.isRequired,
    balance: model.walletTokenBalance,
    detailed: PropTypes.bool,
  };

  static defaultProps = {
    balance: undefined,
    detailed: false,
  };


  componentDidMount() {
    const { token, walletAddress } = this.props;
    this.props.fetchTokenBalance(token.address, walletAddress);
  }


  render() {
    const {
      token, balance, detailed, walletAddress,
    } = this.props;
    return (
      <Fragment>
        {detailed &&
          <TokenDetails
            walletAddress={walletAddress}
            token={token}
            balance={balance && balance.balance}
          />
        }
        {!detailed &&
        <CurrencyBalance
          className="token-balance"
          value={balance && balance.balance}
          title={token.symbol}
          decimals={token.decimals}
          strong={false}
        />
        }
      </Fragment>
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
