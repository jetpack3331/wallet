import React, { Component } from 'react';
import { CircularProgress, Table, TableBody, TableCell, TableHead, TableRow } from 'material-ui';
import ArrowBack from 'material-ui-icons/ArrowBack';
import ArrowForward from 'material-ui-icons/ArrowForward';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import { fetchWalletTransactions } from '../../actions/wallets';
import { walletTransactions, walletTransaction } from '../../model';
import { getWalletTransactions } from '../../reducers';
import DateTime from '../common/DateTime';
import EtherDisplay from '../EtherDisplay';

const TransactionRow = ({ address, transaction }) => {
  const inOrOut = from => (from === address ? <ArrowForward className="trans-out"/> : <ArrowBack className="trans-in" />);
  const targetAddress = addr => (addr === address ? 'me' : addr);

  return (
    <TableRow key={transaction.hash}>
      <TableCell>{inOrOut(transaction.from)}</TableCell>
      <TableCell>{transaction.hash}</TableCell>
      <TableCell><Link href={`https://etherscan.io/block/${transaction.blockNumber}`} target="_blank">{transaction.blockNumber}</Link></TableCell>
      <TableCell><DateTime value={transaction.timeStamp} unix/></TableCell>
      <TableCell>{targetAddress(transaction.from)}</TableCell>
      <TableCell>{targetAddress(transaction.to)}</TableCell>
      <TableCell><EtherDisplay value={transaction.value}/></TableCell>
      <TableCell><EtherDisplay value={transaction.gas * transaction.gasPrice}/></TableCell>
    </TableRow>
  );
};

TransactionRow.propTypes = {
  address: PropTypes.string.isRequired,
  transaction: walletTransaction.isRequired,
};


class WalletTransactions extends Component {
  static propTypes = {
    address: PropTypes.string.isRequired,
    fetchWalletTransactions: PropTypes.func.isRequired,
    walletTransactions,
  };

  static defaultProps = {
    walletTransactions: undefined,
  };

  componentDidMount() {
    this.props.fetchWalletTransactions(this.props.address);
  }

  render() {
    const { address } = this.props;
    const transactions = this.props.walletTransactions
      && this.props.walletTransactions.transactions;

    let transactionsList;

    if (!transactions) {
      transactionsList = <CircularProgress/>;
    } else {
      transactionsList = (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell />
              <TableCell>Hash</TableCell>
              <TableCell>Block</TableCell>
              <TableCell>Time</TableCell>
              <TableCell>From</TableCell>
              <TableCell>To</TableCell>
              <TableCell>Value</TableCell>
              <TableCell>Tx Fee</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {transactions && transactions.map(trans =>
              (
                <TransactionRow address={address} transaction={trans}/>
              ))}
          </TableBody>
        </Table>
      );
    }

    return (
      <div>
        {transactionsList}
      </div>
    );
  }
}

const mapStateToProps = (state, props) => ({
  walletTransactions: getWalletTransactions(state, props.address),
});

const mapDispatchToProps = {
  fetchWalletTransactions,
};

export default connect(mapStateToProps, mapDispatchToProps)(WalletTransactions);
