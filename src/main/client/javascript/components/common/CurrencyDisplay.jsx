import { formatUnits, BigNumber } from 'ethers/utils';
import * as PropTypes from 'prop-types';
import React from 'react';
import './CurrencyDisplay.less';

const CurrencyDisplay = ({
  value, decimals, code, strong, ...rest
}) => {
  const { className = '', ...others } = rest;
  const codeSuffix = !!code && <span className="currency-code">{code}</span>;

  return (
    <span className={`${className} currency-display ${strong ? 'currency-strong' : ''}`} {...others}>
      {value && <span>{formatUnits(value, decimals)}{codeSuffix}</span>}
    </span>
  );
};

CurrencyDisplay.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number, PropTypes.instanceOf(BigNumber)]),
  decimals: PropTypes.number,
  code: PropTypes.string,
  strong: PropTypes.bool,
};

CurrencyDisplay.defaultProps = {
  value: undefined,
  decimals: 18,
  strong: false,
  code: undefined,
};

export default CurrencyDisplay;
