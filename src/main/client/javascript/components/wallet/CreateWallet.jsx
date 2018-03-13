import React, { Fragment } from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Button } from 'material-ui';
import NoWalletIcon from '../../../images/icon-no-wallet.png';
import { createWalletAccount, importPrivateKeyWalletAccount } from '../../actions/wallets';
import CreateWalletForm from '../../components/forms/CreateWalletForm';
import ImportWalletForm from '../../components/forms/ImportWalletForm';
import './CreateWallet.less';

const CreateNewWalletFragment = ({ onCreateWalletAccount }) => (
  <Fragment>
    <p><strong>Choose a password to create one now!</strong></p>
    <p className="important-message">
      <strong>Important: </strong> We have no knowledge of your password and
      cannot help if it is lost or forgotten. <br/>
      Please take care in choosing a secure password, and also making sure you
      remember it.
    </p>
    <CreateWalletForm
      onSubmit={onCreateWalletAccount}
    />
  </Fragment>
);

CreateNewWalletFragment.propTypes = {
  onCreateWalletAccount: PropTypes.func.isRequired,
};

const ImportWalletFragment = ({ onImportWalletAccount }) => (
  <Fragment>
    <p>
      <strong>
        Your existing private key and password will be converted into an encrypted keystore
      </strong>
    </p>
    <p className="important-message">
      <strong>Important: </strong>
      We do not store your private key or password and cannot help if
      they are lost or forgotten. <br/>
      Please take care in choosing a secure password, and also making sure you
      remember it.
    </p>
    <ImportWalletForm
      onSubmit={onImportWalletAccount}
    />
  </Fragment>
);

ImportWalletFragment.propTypes = {
  onImportWalletAccount: PropTypes.func.isRequired,
};


class CreateWallet extends React.Component {
  static propTypes = {
    createWalletAccount: PropTypes.func.isRequired,
    importPrivateKeyWalletAccount: PropTypes.func.isRequired,
  };

  state = {
    createMode: 'create',
  };

  switchMode = () => {
    this.setState({ createMode: this.state.createMode === 'create' ? 'import' : 'create' });
  };

  render() {
    return (
      <div className="row no-wallet">
        <div className="main-icon"><img className="icon no-wallet" src={NoWalletIcon} alt="No Wallet"/></div>
        <h1 className="display-1"><strong>No Wallet</strong></h1>
        {this.state.createMode === 'create' &&
        <CreateNewWalletFragment onCreateWalletAccount={this.props.createWalletAccount}/>
        }
        {this.state.createMode === 'import' &&
        <ImportWalletFragment onImportWalletAccount={this.props.importPrivateKeyWalletAccount}/>
        }
        <Button variant="flat" onClick={this.switchMode}>
          Or&nbsp;
          {this.state.createMode === 'create' && 'import existing'}
          {this.state.createMode === 'import' && 'create new'}
        </Button>
      </div>
    );
  }
}

const mapDispatchToProps = {
  createWalletAccount,
  importPrivateKeyWalletAccount,
};

export default connect(null, mapDispatchToProps)(CreateWallet);
