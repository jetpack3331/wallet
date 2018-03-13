import { bigNumberify, formatEther, parseEther } from 'ethers/utils';
import { isNil } from 'lodash';
import { Button, CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { required } from 'redux-form-validators';
import EtherDisplay from '../EtherDisplay';

class SendEtherForm extends React.Component {
  static propTypes = {
    transactionFee: PropTypes.number.isRequired,
    balance: PropTypes.string.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
    error: PropTypes.string,
    submitting: PropTypes.bool.isRequired,
  };

  static defaultProps = {
    error: undefined,
  };

  minSendEther = '0.0001';

  validate = (message, condition) => {
    return condition ? message : undefined;
  };

  notBelowMinEther = (value) => {
    const minSend = parseEther(this.minSendEther);
    const wei = (!isNil(value) || null) && parseEther(value);
    return this.validate(`cannot be below ${this.minSendEther}`, !isNil(wei) && wei.lt(minSend));
  };

  notExceedingMaxEther = (value) => {
    const maxSend = bigNumberify(this.props.balance).sub(bigNumberify(this.props.transactionFee));
    const wei = value && parseEther(value);
    return this.validate(`cannot be above ${formatEther(maxSend)}`, !!wei && wei.gt(maxSend));
  };

  render() {
    const {
      handleSubmit, submitting, onCancel, error, balance, transactionFee,
    } = this.props;

    const maxSend = bigNumberify(balance).sub(transactionFee);
    const maxSendEther = formatEther(maxSend);

    return (
      <form className="form-wide" onSubmit={handleSubmit} autoComplete="off">
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <div className="fields">
          <div className="field">
            <Field
              autoFocus
              name="to"
              component={TextField}
              label="Send to address"
              type="text"
              validate={[required()]}
              disabled={submitting}
              multiline
              fullWidth
            />
          </div>
          <div className="field">
            <Field
              name="amount"
              component={TextField}
              label="Amount (ETH)"
              placeholder={`${this.minSendEther} to ${maxSendEther}`}
              type="text"
              validate={[required(), this.notBelowMinEther, this.notExceedingMaxEther]}
              disabled={submitting}
              fullWidth
            />
          </div>
          <div className="field">
            <Field
              name="password"
              component={TextField}
              label="Keystore password"
              type="password"
              validate={[required()]}
              disabled={submitting}
              fullWidth
            />
          </div>
        </div>
        <div className="form-info">
          Transaction fee: <EtherDisplay value={transactionFee}/> ETH
        </div>
        <div className="actions">
          <Button
            variant="flat"
            type="button"
            onClick={onCancel}
          >
            Cancel
          </Button>
          {!submitting &&
          <Button
            className="btn-primary"
            variant="raised"
            type="submit"
          >
            Send
          </Button>
          }
          {submitting && <CircularProgress/>}
        </div>
      </form>
    );
  }
}

export default reduxForm({ form: 'sendEth' })(SendEtherForm);
