import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle } from 'material-ui';
import { Wallet } from 'ethers';
import { SubmissionError, submit } from 'redux-form';
import { connect } from 'react-redux';
import Visibility from 'material-ui-icons/Visibility';
import VisibilityOff from 'material-ui-icons/VisibilityOff';
import ViewPrivateKeyForm from '../forms/ViewPrivateKeyForm';
import ViewPrivateKeyPanel from './ViewPrivateKeyPanel';
import wallets from '../../services/api/wallets';

const loadKey = (walletAccount, password) =>
  Wallet.fromEncryptedWallet(walletAccount.secretStorageJson, password)
    .then(wallet => wallet.privateKey);

const loadManagedKey = walletAccount =>
  wallets.fetchPrivateKey(walletAccount.address)
    .then(wallet => wallet.result);

const initialState = {
  privateKey: undefined,
  submitting: false,
};

class ViewPrivateKeyDialog extends React.Component {
  constructor() {
    super();
    this.state = { ...initialState };
  }

  submitForm = (values) => {
    this.setState({ submitting: true });

    if (this.props.walletAccount.type === 'MANAGED') {
      return loadManagedKey(this.props.walletAccount)
        .then((privateKey) => {
          this.setState({ privateKey, submitting: false });
        })
        .catch(() => {
          this.setState({ submitting: false });
          throw new SubmissionError({ _error: 'Unable to load private key. Please try again.' });
        });
    }

    return loadKey(this.props.walletAccount, values.password)
      .then((privateKey) => {
        this.setState({ privateKey, submitting: false });
      })
      .catch(() => {
        this.setState({ submitting: false });
        throw new SubmissionError({ _error: 'Unable to decrypt keystore. Please check the password and try again.' });
      });
  };

  clearState = () => {
    this.setState(initialState);
  };

  handleSubmit = () => {
    this.props.dispatch(submit('viewPrivateKey'));
  };

  render() {
    return (
      <Dialog
        open={this.props.open}
        onClose={this.props.onClose}
        onExited={this.clearState}
        fullWidth
        maxWidth="sm"
      >
        <DialogTitle>
          View private key
        </DialogTitle>
        {this.state.privateKey &&
        <Fragment>
          <DialogContent>
            <ViewPrivateKeyPanel privateKey={this.state.privateKey} onTimeout={this.props.onClose}/>
          </DialogContent>
          <DialogActions>
            <Button variant="flat" onTouchTap={this.props.onClose}>
              <VisibilityOff className="btn-icon-left"/>
              Close
            </Button>
          </DialogActions>
        </Fragment>
        }
        {!this.state.privateKey &&
        <Fragment>
          <DialogContent>
            <ViewPrivateKeyForm onSubmit={this.submitForm} showPassword={this.props.walletAccount.type === 'PRIVATE'}/>
          </DialogContent>
          <DialogActions>
            <Button variant="flat" onTouchTap={this.props.onClose}>
              Cancel
            </Button>
            <Button variant="flat" color="primary" onTouchTap={this.handleSubmit} disabled={this.state.submitting}>
              {!this.state.submitting && <Visibility className="btn-icon-left"/>}
              {!this.state.submitting && 'View key'}
              {this.state.submitting && <CircularProgress size={20}/>}
            </Button>
          </DialogActions>
        </Fragment>
        }
      </Dialog>
    );
  }
}

ViewPrivateKeyDialog.propTypes = {
  dispatch: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
  open: PropTypes.bool.isRequired,
  walletAccount: PropTypes.object.isRequired,
};

export default connect()(ViewPrivateKeyDialog);

