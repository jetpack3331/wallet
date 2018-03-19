import React from 'react';
import PropTypes from 'prop-types';
import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from 'material-ui';
import FileDownload from 'material-ui-icons/FileDownload';

const DownloadKeystoreDialog = ({ open, onClose, walletAddress }) => (
  <Dialog
    open={open}
    onClose={onClose}
    fullWidth
    maxWidth="sm"
  >
    <DialogTitle>
      Download keystore
    </DialogTitle>
    <DialogContent>
      <p>
        Your keystore can be downloaded in standard UTC JSON format used by most Etherium clients.
        This is encrypted with the password you provided when creating the wallet.
      </p>
    </DialogContent>
    <DialogActions>
      <Button variant="flat" onTouchTap={onClose}>
        Cancel
      </Button>
      <Button
        variant="flat"
        size="small"
        href={`/api/wallets/accounts/${walletAddress}/download`}
        onTouchTap={onClose}
        fullWidth
      >
        <FileDownload className="btn-icon-left"/>
        Download
      </Button>
    </DialogActions>
  </Dialog>
);

DownloadKeystoreDialog.propTypes = {
  onClose: PropTypes.func.isRequired,
  open: PropTypes.bool.isRequired,
  walletAddress: PropTypes.string.isRequired,
};

export default DownloadKeystoreDialog;

