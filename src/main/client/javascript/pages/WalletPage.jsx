import { Button } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { createWalletAccount } from '../actions/wallets';
import './HomePage.less';
import { walletAccount } from '../model';
import { getFirstWalletAccount } from '../reducers';

const WalletPage = props => (
  <div className="wallet-page">
    <h1 className="display-2">My Wallet</h1>
    <div className="widgets">
      <div>
        {props.walletAccount &&
        <div>Wallet created: {props.walletAccount.address}</div>
        }

        {!props.walletAccount &&

        <Button
          variant="raised"
          onClick={() => props.createWalletAccount('somepassword123')}
        >
          Create Wallet Account
        </Button>

        }


      </div>
    </div>
  </div>
);

WalletPage.propTypes = {
  createWalletAccount: PropTypes.func.isRequired,
  walletAccount,
};

WalletPage.defaultProps = {
  walletAccount: undefined,
};

const mapStateToProps = state => ({
  walletAccount: getFirstWalletAccount(state),
});

const mapDispatchToProps = {
  createWalletAccount,
};


export default connect(mapStateToProps, mapDispatchToProps)(WalletPage);
