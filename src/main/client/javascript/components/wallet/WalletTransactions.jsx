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
  const inwards = address === transaction.to;
  const transTypeIcon = inwards ? <ArrowBack className="trans-in" /> : <ArrowForward className="trans-out"/>;


  return (
    <TableRow key={transaction.hash}>
      <TableCell>{transTypeIcon}</TableCell>
      <TableCell>
        <span className="transaction-date"><DateTime value={transaction.timeStamp} unix/></span>
      </TableCell>
      <TableCell>
        <span className="hash">{transaction.hash}</span>
        <span className="block"><strong>Block.</strong> <Link href={`https://etherscan.io/block/${transaction.blockNumber}`} className="block-link" target="_blank">{transaction.blockNumber}</Link></span>
      </TableCell>
      <TableCell>
        {inwards && <span className="wallet-id from"><strong>From.</strong> {transaction.from}</span>}
        {!inwards && <span className="wallet-id to"><strong>To.</strong> {transaction.to}</span>}
      </TableCell>
      <TableCell>
        <EtherDisplay className="value" value={transaction.value}/>
        <span className="fee"><strong>TX Fee</strong> <EtherDisplay value={transaction.gas * transaction.gasPrice}/></span>
      </TableCell>
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
    } else if (!transactions.length) {
      transactionsList = <div className="no-transactions">Looks like there are no transactions yet.</div>;
    } else {
      transactionsList = (
        <Table className="transaction-table">
          <TableHead>
            <TableRow>
              <TableCell />
              <TableCell>Date</TableCell>
              <TableCell>Hash/Block</TableCell>
              <TableCell>Wallet Id</TableCell>
              <TableCell>Value (ETH)</TableCell>
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
      <div className="transaction-container">
        <h5><strong>Transactions</strong></h5>
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
