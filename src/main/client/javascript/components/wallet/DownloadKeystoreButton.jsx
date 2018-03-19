import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Button } from 'material-ui';
import LockOutline from 'material-ui-icons/LockOutline';
import DownloadKeystoreDialog from './DownloadKeystoreDialog';

class DownloadKeystoreButton extends Component {
  state = {
    dialogOpen: false,
  };

  openDialog = () => {
    this.setState({ dialogOpen: true });
  };

  closeDialog = () => {
    this.setState({ dialogOpen: false });
  };

  render() {
    return (
      <Fragment>
        <Button
          className="btn-primary"
          variant="raised"
          size="small"
          onClick={this.openDialog}
          fullWidth
        >
          <LockOutline className="btn-icon-left"/>
          Keystore
        </Button>
        <DownloadKeystoreDialog
          walletAddress={this.props.walletAddress}
          open={this.state.dialogOpen}
          onClose={this.closeDialog}
        />
      </Fragment>
    );
  }
}

DownloadKeystoreButton.propTypes = {
  walletAddress: PropTypes.string.isRequired,
};

export default DownloadKeystoreButton;
