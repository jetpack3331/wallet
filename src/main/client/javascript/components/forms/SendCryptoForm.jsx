import { bigNumberify, formatUnits, getAddress, parseUnits } from 'ethers/utils';
import { isNil } from 'lodash';
import { Button, CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React, { Fragment } from 'react';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { required } from 'redux-form-validators';
import * as model from '../../model';
import CurrencyDisplay from '../common/CurrencyDisplay';
import FormError from './FormError';

const ONE = bigNumberify(1);

class SendCryptoForm extends React.Component {
  static propTypes = {
    balance: PropTypes.string.isRequired,
    showPassword: PropTypes.bool.isRequired,
    handleSubmit: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
    error: PropTypes.string,
    submitting: PropTypes.bool.isRequired,
    transactionFee: PropTypes.number,
    token: model.token,
    minSend: PropTypes.string,
  };

  static defaultProps = {
    token: {
      symbol: 'ETH',
      address: '0x', // Dummy
      decimals: 18,
      name: 'Ether',
    },
    transactionFee: undefined,
    error: undefined,
    minSend: undefined,
  };

  validate = (message, condition) => (condition ? message : undefined);

  parse = value => parseUnits(value, this.props.token.decimals);
  format = value => formatUnits(value, this.props.token.decimals);
  maxSend = () => bigNumberify(this.props.balance).sub(this.props.transactionFee || '0');
  minSend = () => (this.props.minSend ? this.parse(this.props.minSend) : ONE);

  bigNumber = (value) => {
    const { decimals } = this.props.token;
    try {
      if (value) {
        parseUnits(value, decimals);
      }
      return undefined;
    } catch (err) {
      return `must be a numeric value with ${decimals} max decimals`;
    }
  };

  atLeastOneUnit = (value) => {
    const wei = (!isNil(value) || null) && this.parse(value);
    return this.validate(`cannot be below minimal unit of ${this.props.token.symbol}`, !isNil(wei) && wei.lt(ONE));
  };

  notBelowMin = (value) => {
    const minSend = this.parse(this.props.minSend);
    const wei = (!isNil(value) || null) && this.parse(value);
    return this.validate(`cannot be below ${this.props.minSend}`, !isNil(wei) && wei.lt(minSend));
  };

  minValue = value => (this.props.minSend ? this.notBelowMin(value) : this.atLeastOneUnit(value));

  notExceedingMax = (value) => {
    const maxSend = this.maxSend();
    const wei = value && this.parse(value);
    return this.validate(`cannot be above ${this.format(maxSend)}`, !!wei && wei.gt(maxSend));
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
      handleSubmit, submitting, onCancel, error, transactionFee, token, showPassword,
    } = this.props;

    const minSend = this.minSend();
    const maxSend = this.maxSend();
    const maxSendEther = this.format(maxSend);

    return (
      <Fragment>
        {maxSend.lt(minSend) &&
        <div>
          <p>
            <span>Your current balance is not high enough to</span>
            {transactionFee &&
            <span> cover the transaction fee
            of <CurrencyDisplay value={transactionFee} code="ETH" strong/> and
            </span>
            }
            <span> allow for the minimum transfer of <CurrencyDisplay
              value={minSend}
              code={token.symbol}
              decimals={token.decimals}
              strong
            />.
            </span>
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
                label={`Amount (${token.symbol})`}
                placeholder={`${this.props.minSend || 'up'} to ${maxSendEther}`}
                type="text"
                validate={[
                  required(),
                  this.bigNumber,
                  this.minValue,
                  this.notExceedingMax,
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
          {!!transactionFee &&
          <div className="form-info">
            Transaction fee: <CurrencyDisplay value={transactionFee} code="ETH" strong/>
          </div>
          }
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

export default reduxForm({ form: 'sendTokens' })(SendCryptoForm);
