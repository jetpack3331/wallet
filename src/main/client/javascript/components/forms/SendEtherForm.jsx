import { bigNumberify, formatEther, parseEther, getAddress } from 'ethers/utils';
import { isNil } from 'lodash';
import { Button, CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React, { Fragment } from 'react';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { numericality, required } from 'redux-form-validators';
import CurrencyDisplay from '../common/CurrencyDisplay';
import FormError from './FormError';

class SendEtherForm extends React.Component {
  static propTypes = {
    transactionFee: PropTypes.number.isRequired,
    balance: PropTypes.string.isRequired,
    showPassword: PropTypes.bool.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
    error: PropTypes.string,
    submitting: PropTypes.bool.isRequired,
  };

  static defaultProps = {
    error: undefined,
  };

  minSendEther = '0.0001';

  validate = (message, condition) => (condition ? message : undefined);

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

  ethereumAddress = (value) => {
    try {
      if (value) {
        getAddress(value.toLowerCase());
      }
      return undefined;
    } catch (err) {
      return 'must be a valid Ethereum address';
    }
  };

  render() {
    const {
      handleSubmit, submitting, onCancel, error, balance, transactionFee, showPassword,
    } = this.props;

    const minSend = parseEther(this.minSendEther);
    const maxSend = bigNumberify(balance).sub(transactionFee);
    const maxSendEther = formatEther(maxSend);

    return (
      <Fragment>
        {maxSend.lt(minSend) &&
        <div>
          <p>
            Your current balance is not high enough to cover the
            transaction fee of <CurrencyDisplay value={transactionFee} code="ETH" strong/> and
            allow for the minimum transfer of <CurrencyDisplay value={minSend} code="ETH" strong/>.
          </p>
          <div className="actions">
            <Button
              className="btn-primary"
              variant="raised"
              type="button"
              onClick={onCancel}
            >
              Return to wallet
            </Button>
          </div>
        </div>
        }
        {maxSend.gte(minSend) &&
        <form className="form-wide" onSubmit={handleSubmit} autoComplete="off">
          <FormError value={error}/>
          <div className="fields">
            <div className="field">
              <Field
                autoFocus
                name="to"
                component={TextField}
                label="Send to address"
                type="text"
                validate={[required(), this.ethereumAddress]}
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
                validate={[
                  required(),
                  numericality(),
                  this.notBelowMinEther,
                  this.notExceedingMaxEther,
                ]}
                disabled={submitting}
                fullWidth
              />
            </div>
            {showPassword &&
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
            }
          </div>
          <div className="form-info">
            Transaction fee: <CurrencyDisplay value={transactionFee} code="ETH" strong/>
          </div>
          <div className="actions">
            {!submitting &&
            <Fragment>
              <Button
                variant="flat"
                type="button"
                onClick={onCancel}
              >
                Cancel
              </Button>
              <Button
                className="btn-primary"
                variant="raised"
                type="submit"
              >
                Send
              </Button>
            </Fragment>
            }
            {submitting && <CircularProgress/>}
          </div>
        </form>
        }
      </Fragment>
    );
  }
}

export default reduxForm({ form: 'sendEth' })(SendEtherForm);
