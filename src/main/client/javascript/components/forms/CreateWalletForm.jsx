import { Button } from 'material-ui';
import React from 'react';
import * as PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { required } from 'redux-form-validators';

const CreateWalletForm = props => (
  <form onSubmit={props.handleSubmit}>
    {props.error && <p style={{ color: 'red' }}>{props.error}</p>}
    <Field
      name="password"
      component={TextField}
      label="Password"
      type="password"
      validate={[
        required({ msg: 'Password is required' }),
      ]}
    />
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
