import { formatEther } from 'ethers/utils';
import * as PropTypes from 'prop-types';
import React from 'react';

const EtherDisplay = ({ value, ...rest }) => (
  <span {...rest}>{ value && `${formatEther(value)}` }</span>
);

EtherDisplay.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
};

EtherDisplay.defaultProps = {
  value: undefined,
};

export default EtherDisplay;
