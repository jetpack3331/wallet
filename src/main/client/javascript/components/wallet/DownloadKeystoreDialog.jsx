import React from 'react';
import PropTypes from 'prop-types';
import { Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle } from 'material-ui';
import FileDownload from 'material-ui-icons/FileDownload';
import { SubmissionError, submit } from 'redux-form';
import { connect } from 'react-redux';
import FileSaver from 'file-saver';
import { Wallet } from 'ethers';
import wallets from '../../services/api/wallets';
import DownloadKeystoreForm from '../forms/DownloadKeystoreForm';

const privateKeystore = walletAccount =>
  walletAccount.type === 'PRIVATE';

const encryptManagedKeystore = (walletAccount, password) =>
  wallets.fetchPrivateKey(walletAccount.address)
    .then(response => new Wallet(response.result).encrypt(password))
    .then(secretStorageJson => ({
      ...walletAccount,
      secretStorageJson,
    }));

const loadKeystore = (walletAccount, password) => (
  privateKeystore(walletAccount) ?
    Promise.resolve(walletAccount.secretStorageJson) :
    encryptManagedKeystore(walletAccount, password)
);

const getFilename = (walletAccount) => {
  const time = walletAccount.created ? new Date(walletAccount.created) : new Date();
  const timestamp = time.toISOString().replace(/:/g, '-');
  return `UTC--${timestamp}--${walletAccount.address}`;
};

const saveKeystore = (walletAccount) => {
  const blob = new Blob([walletAccount.secretStorageJson], { type: 'application/json;charset=utf-8' });
  FileSaver.saveAs(blob, getFilename(walletAccount), true);
};

const initialState = {
  submitting: false,
};

class DownloadKeystoreDialog extends React.Component {
  constructor() {
    super();
    this.state = { ...initialState };
  }

  submitForm = (values) => {
    this.setState({ submitting: true });
    return loadKeystore(this.props.walletAccount, values.password)
      .then(saveKeystore)
      .then(() => this.setState({ submitting: false }))
      .catch((error) => {
        console.log('error', error);
        this.setState({ submitting: false });
        throw new SubmissionError({ _error: 'Unable to download keystore. Please try again.' });
      });
  };

  clearState = () => {
    this.setState(initialState);
  };

  handleSubmit = () => {
    this.props.dispatch(submit('downloadKeystore'));
  };

  render() {
    const { open, onClose, walletAccount } = this.props;
    return (
      <Dialog
        open={open}
        onClose={onClose}
        onExited={this.clearState}
        fullWidth
        maxWidth="sm"
      >
        <DialogTitle>
          Download keystore
        </DialogTitle>
        <DialogContent>
          <DownloadKeystoreForm
            onSubmit={this.submitForm}
            showPassword={!privateKeystore(walletAccount)}
          />
        </DialogContent>
        <DialogActions>
          <Button variant="flat" onClick={onClose}>
            Cancel
          </Button>
          <Button variant="flat" color="primary" onClick={this.handleSubmit} disabled={this.state.submitting}>
            {!this.state.submitting && <FileDownload className="btn-icon-left"/>}
            {!this.state.submitting && 'Download'}
            {this.state.submitting && <CircularProgress size={20}/>}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }
}

DownloadKeystoreDialog.propTypes = {
  dispatch: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
  open: PropTypes.bool.isRequired,
  walletAccount: PropTypes.object.isRequired,
};

export default connect()(DownloadKeystoreDialog);
