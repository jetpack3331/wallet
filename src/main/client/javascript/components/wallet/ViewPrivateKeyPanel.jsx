import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { TextField } from 'material-ui';

class ViewPrivateKeyPanel extends Component {
  state = { timeRemaining: 30 };

  componentDidMount() {
    this.startTimer();
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
  };

  countDown = () => {
    const timeRemaining = this.state.timeRemaining - 1;
    this.setState({ timeRemaining });
    if (timeRemaining <= 0) {
      this.clearTimer();
      this.props.onTimeout();
    }
  };

  render() {
    const { privateKey } = this.props;
    return (
      <div>
        <p>
          Your private key is shown below.
          It will be automatically hidden in {this.state.timeRemaining} seconds.
        </p>
        <TextField
          className="private-key-field"
          multiline
          type="text"
          inputProps={{
            readOnly: true,
          }}
          value={privateKey}
          fullWidth
        />
      </div>
    );
  }
}

ViewPrivateKeyPanel.propTypes = {
  privateKey: PropTypes.string.isRequired,
  onTimeout: PropTypes.func.isRequired,
};

export default ViewPrivateKeyPanel;
