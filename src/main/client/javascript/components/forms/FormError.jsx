import PropTypes from 'prop-types';
import React from 'react';
import './FormError.less';


const FormError = ({ value }) => (
  <div>{value && <p className="form-error">{value}</p>}</div>
);

FormError.propTypes = {
  value: PropTypes.string,
};

FormError.defaultProps = {
  value: undefined,
};

export default FormError;
