import React from 'react';
import PropTypes from 'prop-types';
import { Button, Tooltip } from 'material-ui';
import LockOutline from 'material-ui-icons/LockOutline';

const DownloadKeystoreButton = ({ walletAccount }) => (
  <Tooltip title="Download encrypted keystore" enterDelay={300}>
    <Button
      className="btn-primary"
      variant="raised"
      size="small"
      href={`/api/wallets/accounts/${walletAccount.address}/download`}
      fullWidth
    >
      <LockOutline className="btn-icon-left"/>
      Keystore
    </Button>
  </Tooltip>
);

DownloadKeystoreButton.propTypes = {
  walletAccount: PropTypes.object.isRequired,
};

export default DownloadKeystoreButton;
