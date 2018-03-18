import { Button, CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { reduxForm } from 'redux-form';
import Add from 'material-ui-icons/Add';
import './CreateWalletForm.less';
import FormError from './FormError';

const CreateManagedWalletForm = props => (
  <form className="create-managed-wallet-form" onSubmit={props.handleSubmit}>
    <FormError value={props.error}/>
    <div className="actions">
      {!props.submitting &&
      <Button
        className="btn-primary"
        variant="raised"
        type="submit"
        size="large"
      >
        <Add className="btn-icon-left"/>
        Create Address
      </Button>
      }
      {props.submitting && <CircularProgress/>}
    </div>
  </form>
);

CreateManagedWalletForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  error: PropTypes.string,
  submitting: PropTypes.bool.isRequired,
};

CreateManagedWalletForm.defaultProps = {
  error: undefined,
};

export default reduxForm({ form: 'createManagedWallet' })(CreateManagedWalletForm);
