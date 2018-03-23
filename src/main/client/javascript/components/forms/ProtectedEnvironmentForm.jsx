import { Button } from 'material-ui';
import React from 'react';
import { Field, reduxForm } from 'redux-form';
import { TextField } from 'redux-form-material-ui';
import { required } from 'redux-form-validators';

const ProtectedEnvironmentForm = () => (
  <form method="post" action="/protected-auth">
    <Field
      name="password"
      autoFocus
      component={TextField}
      label="Password"
      type="password"
      validate={[
        required(),
      ]}
    />
    <div className="actions">
      <Button
        className="btn-primary"
        variant="raised"
        type="submit"
      >
        Enter
      </Button>
    </div>

  </form>
);


export default reduxForm({ form: 'protectedEnvironment' })(ProtectedEnvironmentForm);
