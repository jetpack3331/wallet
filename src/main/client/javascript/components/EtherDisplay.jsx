import * as PropTypes from 'prop-types';
import React from 'react';
import { fromTokenBase, getDecimalFromEtherUnit } from '../util/crypto-units';

const EtherDisplay = ({ value }) => (
  <span className="value">
    <span className="label">{ value && `${fromTokenBase(value, getDecimalFromEtherUnit('ether'))}` }</span>
    <span className="currency">{ value && 'ETH' }</span>
  </span>
);

EtherDisplay.propTypes = {
  value: PropTypes.string,
};

EtherDisplay.defaultProps = {
  value: undefined,
};

export default EtherDisplay;
