import { formatUnits, BigNumber } from 'ethers/utils';
import { isNil } from 'lodash';
import { CircularProgress } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import './CurrencyDisplay.less';

const CurrencyDisplay = ({
  value, decimals, code, strong, noSpinner, ...rest
}) => {
  const { className = '', ...others } = rest;
  const codeSuffix = !!code && <span className="currency-code">{code}</span>;

  return (
    <span className={`${className} currency-display ${strong ? 'currency-strong' : ''}`} {...others}>
      {!isNil(value) && <span>{formatUnits(value, decimals)}{codeSuffix}</span>}
      {!noSpinner && isNil(value) && <CircularProgress className="currency-loading" size={20}/>}
    </span>
  );
};

CurrencyDisplay.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number, PropTypes.instanceOf(BigNumber)]),
  decimals: PropTypes.number,
  code: PropTypes.string,
  strong: PropTypes.bool,
  noSpinner: PropTypes.bool,
};

CurrencyDisplay.defaultProps = {
  value: undefined,
  decimals: 18,
  strong: false,
  noSpinner: false,
  code: undefined,
};

export default CurrencyDisplay;
