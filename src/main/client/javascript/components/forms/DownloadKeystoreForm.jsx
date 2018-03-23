import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { confirmation, format, length, required } from 'redux-form-validators';
import './ViewPrivateKeyForm.less';
import FormError from './FormError';

const DownloadKeystoreForm = ({
  handleSubmit, submitting, error, showPassword,
}) => (
  <form onSubmit={handleSubmit}>
    <FormError value={error}/>
    {showPassword &&
    <Fragment>
      <p>
        Your keystore can be downloaded in standard UTC JSON format used by most Ethereum clients.
      </p>
      <p>
        This will create a new keystore, encrypted using the password you provide here. This will not
        affect access to your online keystore via Hut34 Wallet.
      </p>
      <div className="fields">
        <div className="field">
          <Field
            autoFocus
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
            disabled={submitting}
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
            disabled={submitting}
            fullWidth
          />
        </div>
      </div>
    </Fragment>
    }
    {!showPassword &&
    <p>
      Your keystore can be downloaded in standard UTC JSON format used by most Ethereum clients.
      This is encrypted with the password you provided when creating the wallet.
    </p>
    }
  </form>
);

DownloadKeystoreForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  submitting: PropTypes.bool.isRequired,
  error: PropTypes.string,
  showPassword: PropTypes.bool,
};

DownloadKeystoreForm.defaultProps = {
  error: undefined,
  showPassword: false,
};

export default reduxForm({ form: 'downloadKeystore' })(DownloadKeystoreForm);
