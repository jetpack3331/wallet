import React from 'react';
import PropTypes from 'prop-types';
import { Button } from 'material-ui';
import LockOutline from 'material-ui-icons/LockOutline';

const DownloadKeystoreButton = ({ walletAccount }) => (
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
);

DownloadKeystoreButton.propTypes = {
  walletAccount: PropTypes.object.isRequired,
};

export default DownloadKeystoreButton;
