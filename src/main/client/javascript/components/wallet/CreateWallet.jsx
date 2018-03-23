import React, { Fragment } from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Button } from 'material-ui';
import NoWalletIcon from '../../../images/icon-no-wallet.png';
import { createManagedWalletAccount, createWalletAccount, importPrivateKeyWalletAccount } from '../../actions/wallets';
import CreateWalletForm from '../../components/forms/CreateWalletForm';
import ImportWalletForm from '../../components/forms/ImportWalletForm';
import './CreateWallet.less';
import CreateManagedWalletForm from '../forms/CreateManagedWalletForm';

const CreateNewWalletFragment = ({ onCreateWalletAccount, onCancel }) => (
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
      onCancel={onCancel}
    />
  </Fragment>
);

CreateNewWalletFragment.propTypes = {
  onCreateWalletAccount: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};

const CreateManagedWalletFragment = ({ onCreateWalletAccount, onSwitchMode }) => (
  <Fragment>
    <p><strong>One click to create an address!</strong></p>
    <p className="important-message">
      <strong>Important: </strong> This will create an address managed by HUT34 Wallet.<br/>
      Your private key will be encrypted and the password stored in your Google Drive.<br/>
      We only have access to this file when you are logged into HUT34 Wallet.<br/>
      You can export your private key and/or keystore at any time.
    </p>
    <CreateManagedWalletForm
      onSubmit={onCreateWalletAccount}
    />
    <Button className="btn-margin" variant="flat" onClick={() => onSwitchMode('password')}>Use password</Button>
    <Button className="btn-margin" variant="flat" onClick={() => onSwitchMode('import')}>Import existing</Button>
  </Fragment>
);

CreateManagedWalletFragment.propTypes = {
  onCreateWalletAccount: PropTypes.func.isRequired,
  onSwitchMode: PropTypes.func.isRequired,
};

const ImportWalletFragment = ({ onImportWalletAccount, onCancel }) => (
  <Fragment>
    <p>
      <strong>
        Your existing private key will be encrypted using the password you supply.<br/>
        The password can be different from the one used for the keystore you are importing from.
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
      onCancel={onCancel}
    />
  </Fragment>
);

ImportWalletFragment.propTypes = {
  onImportWalletAccount: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};


class CreateWallet extends React.Component {
  static propTypes = {
    createWalletAccount: PropTypes.func.isRequired,
    createManagedWalletAccount: PropTypes.func.isRequired,
    importPrivateKeyWalletAccount: PropTypes.func.isRequired,
    onAddressCreated: PropTypes.func,
  };

  static defaultProps = {
    onAddressCreated: () => {
    },
  };

  state = {
    createMode: 'create',
  };

  switchMode = (mode) => {
    this.setState({ createMode: mode });
  };

  createWalletAccount = request => (
    this.props.createWalletAccount(request)
      .then(() => this.props.onAddressCreated())
  );

  createManagedWalletAccount = request => (
    this.props.createManagedWalletAccount(request)
      .then(() => this.props.onAddressCreated())
  );

  importPrivateKeyWalletAccount = request => (
    this.props.importPrivateKeyWalletAccount(request)
      .then(() => this.props.onAddressCreated())
  );

  render() {
    return (
      <div className="row no-wallet">
        <div className="main-icon"><img className="logo" src={NoWalletIcon} alt="No Wallet"/></div>
        <h1 className="display-1"><strong>Add Address</strong></h1>
        {this.state.createMode === 'create' &&
        <CreateManagedWalletFragment
          onCreateWalletAccount={this.createManagedWalletAccount}
          onSwitchMode={this.switchMode}
        />
        }
        {this.state.createMode === 'password' &&
        <CreateNewWalletFragment
          onCreateWalletAccount={this.createWalletAccount}
          onCancel={() => this.switchMode('create')}
        />
        }
        {this.state.createMode === 'import' &&
        <ImportWalletFragment
          onImportWalletAccount={this.importPrivateKeyWalletAccount}
          onCancel={() => this.switchMode('create')}
        />
        }
      </div>
    );
  }
}

const mapDispatchToProps = {
  createManagedWalletAccount,
  createWalletAccount,
  importPrivateKeyWalletAccount,
};

export default connect(null, mapDispatchToProps)(CreateWallet);
