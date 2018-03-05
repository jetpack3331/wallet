import { CircularProgress } from 'material-ui';
import { string } from 'prop-types';
import React from 'react';
import './LoadingIndicator.less';

const LoadingIndicator = ({ text, ...rest }) => (
  <div className="loading-indicator">
    <CircularProgress className="spinner" {...rest} />
    <p className="text">{text || 'Loading...'}</p>
  </div>
);

LoadingIndicator.propTypes = {
  text: string,
};

LoadingIndicator.defaultProps = {
  text: null,
};

export default LoadingIndicator;
