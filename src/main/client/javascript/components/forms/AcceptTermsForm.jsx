import { Button, CircularProgress, FormControlLabel } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import { Field, reduxForm } from 'redux-form';
import { Link } from 'react-router';
import { Checkbox } from 'redux-form-material-ui';
import { acceptance } from 'redux-form-validators';
import './CreateWalletForm.less';
import FormError from './FormError';

const AcceptTermsForm = props => (
  <form className="accept-terms-form form-wide" onSubmit={props.handleSubmit}>
    <FormError value={props.error}/>
    <div>
      Please read and agree to the
      <strong>
        <Link href="https://docsend.com/view/sxfgj6w" target="_blank"> terms and conditions </Link>
      </strong>
      to continue
    </div>
    <div>
      <FormControlLabel
        control={
          <Field
            name="termsAndConditions"
            component={Checkbox}
            color="primary"
            validate={acceptance({ msg: 'You must accept terms and conditions to continue' })}
          />}
        label="I agree to the terms and conditions"
      />
    </div>
    <div>
      <Button
        variant="flat"
        onClick={props.onCancel}
      >
        Get me out of here
      </Button>
      {!props.submitting &&
      <Button
        className="btn-primary"
        variant="raised"
        type="submit"
        disabled={!props.valid}
      >
        I got it. Let&apos;s go
      </Button>
      }
      {props.submitting && <CircularProgress/>}
    </div>
  </form>
);

AcceptTermsForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  error: PropTypes.string,
  submitting: PropTypes.bool.isRequired,
  valid: PropTypes.bool.isRequired,
};

AcceptTermsForm.defaultProps = {
  error: undefined,
};

export default reduxForm({ form: 'acceptTerms' })(AcceptTermsForm);
