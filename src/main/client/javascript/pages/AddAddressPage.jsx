import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import CreateWallet from '../components/wallet/CreateWallet';
import './WalletContainer.less';

const AddAddressPage = ({ onAddressCreated }) => (
  <div className="wallet-container">
    <div className="container">
      <div className="widgets">
        <CreateWallet onAddressCreated={onAddressCreated}/>
      </div>
    </div>
  </div>
);

AddAddressPage.propTypes = {
  onAddressCreated: PropTypes.func,
};

AddAddressPage.defaultProps = {
  onAddressCreated: () => {
  },
};

const mapDispatchToProps = dispatch => ({
  onAddressCreated: () => dispatch(push('/')),
});

export default connect(null, mapDispatchToProps)(AddAddressPage);
