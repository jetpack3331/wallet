import * as PropTypes from 'prop-types';
import React from 'react';
import { fromTokenBase, getDecimalFromEtherUnit } from '../util/crypto-units';

const EtherDisplay = ({ value, ...rest }) => (
  <span {...rest}>{ value && `${fromTokenBase(value, getDecimalFromEtherUnit('ether'))}` }</span>
);

EtherDisplay.propTypes = {
  value: PropTypes.string,
};

EtherDisplay.defaultProps = {
  value: undefined,
};

export default EtherDisplay;
