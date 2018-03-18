import { Button, CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { confirmation, format, length, required } from 'redux-form-validators';
import './ImportWalletForm.less';
import FormError from './FormError';

const ImportPrivateKeyWalletForm = props => (
  <form className="import-private-key-wallet-form" onSubmit={props.handleSubmit}>
    <FormError value={props.error}/>
    <div className="fields">
      <div className="field">
        <Field
          autoFocus
          name="privateKey"
          component={TextField}
          label="Private key"
          multiline
          type="text"
          validate={[
            required(),
            format({ with: /^(0x|0X)?[a-fA-F0-9]{64}$/, msg: 'must be private key in hexadecimal format' }),
          ]}
          disabled={props.submitting}
          fullWidth
        />
      </div>
      <div className="field">
        <Field
          name="password"
          component={TextField}
          label="Set a password"
          type="password"
          validate={[
            required(),
            length({ min: 8 }),
            format({ with: /.*\d+.*/i, msg: 'must have one or more numeric digits' }),
            format({ with: /.*\D+.*/i, msg: 'must have one or more non-numeric character' }),
          ]}
          disabled={props.submitting}
          fullWidth
        />
      </div>
      <div className="field">
        <Field
          name="passwordConfirm"
          component={TextField}
          label="Confirm password"
          type="password"
          validate={[
            confirmation({ field: 'password', msg: 'passwords do not match' }),
          ]}
          disabled={props.submitting}
          fullWidth
        />
      </div>
    </div>
    <div className="actions">
      <Button
        variant="flat"
        onClick={props.onCancel}
      >
        Cancel
      </Button>
      {!props.submitting &&
      <Button
        className="btn-primary"
        variant="raised"
        type="submit"
      >
        Import Address
      </Button>
      }
      {props.submitting && <CircularProgress/>}
    </div>
  </form>
);

ImportPrivateKeyWalletForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  error: PropTypes.string,
  submitting: PropTypes.bool.isRequired,
};

ImportPrivateKeyWalletForm.defaultProps = {
  error: undefined,
};

export default reduxForm({ form: 'importPrivateKeyWallet' })(ImportPrivateKeyWalletForm);
