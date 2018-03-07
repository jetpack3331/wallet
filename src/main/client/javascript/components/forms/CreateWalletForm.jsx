import { Button } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { confirmation, format, length, required } from 'redux-form-validators';

const CreateWalletForm = props => (
  <form onSubmit={props.handleSubmit}>
    {props.error && <p style={{ color: 'red' }}>{props.error}</p>}
    <div>
      <Field
        autofocus
        name="password"
        component={TextField}
        label="Password"
        type="password"
        validate={[
          required(),
          length({ min: 8 }),
          format({ with: /.*\d+.*/i, msg: 'must have one or more numeric digits' }),
          format({ with: /.*\D+.*/i, msg: 'must have one or more non-numeric character' }),
        ]}
      />
    </div>
    <div>
      <Field
        name="passwordConfirm"
        component={TextField}
        label="Confirm password"
        type="password"
        validate={[
          confirmation({ field: 'password', msg: 'passwords do not match' }),
        ]}
      />
    </div>
    <div className="actions">
      <Button
        className="btn-primary"
        variant="raised"
        type="submit"
      >
        Create Wallet
      </Button>
    </div>
  </form>
);

CreateWalletForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  error: PropTypes.string,
};

CreateWalletForm.defaultProps = {
  error: undefined,
};

export default reduxForm({ form: 'createWallet' })(CreateWalletForm);
