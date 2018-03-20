import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { required } from 'redux-form-validators';
import './ViewPrivateKeyForm.less';
import FormError from './FormError';

const ViewPrivateKeyForm = ({
  handleSubmit, submitting, error, showPassword,
}) => (
  <form onSubmit={handleSubmit}>
    <FormError value={error}/>
    <p><span className="warning">WARNING</span> Anyone with access to your private key can send transactions from this address.</p>
    {showPassword &&
    <Fragment>
      <p>If you are sure you wish to continue please enter the keystore password</p>
      <Field
        name="password"
        autoFocus
        component={TextField}
        label="Keystore password"
        type="password"
        validate={[
          required({ msg: 'Enter the keystore password' }),
        ]}
        disabled={submitting}
        fullWidth
      />
    </Fragment>
    }
  </form>
);

ViewPrivateKeyForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  submitting: PropTypes.bool.isRequired,
  error: PropTypes.string,
  showPassword: PropTypes.bool,
};

ViewPrivateKeyForm.defaultProps = {
  error: undefined,
  showPassword: true,
};

export default reduxForm({ form: 'viewPrivateKey' })(ViewPrivateKeyForm);
