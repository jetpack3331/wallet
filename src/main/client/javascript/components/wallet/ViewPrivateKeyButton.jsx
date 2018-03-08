import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Button, Tooltip } from 'material-ui';
import VpnKey from 'material-ui-icons/VpnKey';
import ViewPrivateKeyDialog from './ViewPrivateKeyDialog';

class ViewPrivateKeyButton extends React.Component {
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
        <Tooltip title="View private key" enterDelay={300}>
          <Button
            className="btn-primary btn-view-private-key"
            variant="raised"
            size="small"
            onClick={this.openDialog}
            fullWidth
          >
            <VpnKey className="btn-icon-left"/>
            Key
          </Button>
        </Tooltip>
        <ViewPrivateKeyDialog
          walletAccount={this.props.walletAccount}
          open={this.state.dialogOpen}
          onClose={this.closeDialog}
        />
      </Fragment>
    );
  }
}

ViewPrivateKeyButton.propTypes = {
  walletAccount: PropTypes.object.isRequired,
};

export default ViewPrivateKeyButton;
