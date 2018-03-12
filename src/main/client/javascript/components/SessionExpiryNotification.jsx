import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Snackbar } from 'material-ui';
import { connect } from 'react-redux';
import { getSessionExpiresAt } from '../reducers';
import { fetchUser, logoutToHome } from '../actions/auth';

class SessionExpiryNotification extends Component {
  static propTypes = {
    sessionExpiresAt: PropTypes.number.isRequired,
    onExtendSession: PropTypes.func.isRequired,
    onSessionTimeout: PropTypes.func.isRequired,
  };

  state = {
    showSnackbar: false,
    secondsRemaining: -1,
  };

  componentWillReceiveProps(nextProps) {
    if (!this.timer && nextProps.sessionExpiresAt > 0) {
      this.startTimer();
    }
  }

  componentWillUnmount() {
    this.clearTimer();
  }

  startTimer = () => {
    if (!this.timer) {
      this.timer = setInterval(this.countDown, 1000);
    }
  };

  clearTimer = () => {
    if (this.timer) {
      clearInterval(this.timer);
    }
    this.setState({ showSnackbar: false });
  };

  countDown = () => {
    const msRemaining = this.props.sessionExpiresAt - new Date().getTime();
    const secondsRemaining = Math.round(msRemaining / 1000);
    const showSnackbar = secondsRemaining <= 60;
    this.setState({ showSnackbar, secondsRemaining });
    if (secondsRemaining <= 0) {
      this.clearTimer();
      this.props.onSessionTimeout();
    }
  };

  render() {
    return (
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
        open={this.state.showSnackbar}
        onClose={this.handleClose}
        message={<span id="message-id">Your session will expire in <strong>{this.state.secondsRemaining}</strong> seconds</span>}
        action={[
          <Button key="undo" color="secondary" size="small" onClick={this.props.onExtendSession}>
            Extend
          </Button>,
        ]}
      />
    );
  }
}

const mapStateToProps = state => ({
  sessionExpiresAt: getSessionExpiresAt(state),
});

const mapDispatchToProps = {
  onExtendSession: fetchUser,
  onSessionTimeout: logoutToHome,
};

export default connect(mapStateToProps, mapDispatchToProps)(SessionExpiryNotification);
