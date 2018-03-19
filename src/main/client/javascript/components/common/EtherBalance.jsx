import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { fetchWalletBalance } from '../../actions/wallets';
import * as model from '../../model';
import { getWalletBalance } from '../../reducers';
import CurrencyBalance from './CurrencyBalance';

class EtherBalance extends React.Component {
  static propTypes = {
    walletAddress: PropTypes.string.isRequired,
    fetchWalletBalance: PropTypes.func.isRequired,
    title: PropTypes.string,
    walletBalance: model.walletBalance,
  };

  static defaultProps = {
    title: 'Balance',
    walletBalance: undefined,
  };


  componentDidMount() {
    this.props.fetchWalletBalance(this.props.walletAddress);
  }


  render() {
    const { walletBalance, title } = this.props;
    return (
      <CurrencyBalance
        value={walletBalance && walletBalance.balance}
        title={title}
        code="ETH"
      />
    );
  }
}

const mapStateToProps = (state, { walletAddress }) => ({
  walletBalance: getWalletBalance(state, walletAddress),
});
const mapDispatchToProps = {
  fetchWalletBalance,
};

export default connect(mapStateToProps, mapDispatchToProps)(EtherBalance);
