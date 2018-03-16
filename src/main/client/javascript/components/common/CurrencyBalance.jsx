import { BigNumber } from 'ethers/utils';
import * as PropTypes from 'prop-types';
import React from 'react';
import './CurrencyBalance.less';
import CurrencyDisplay from './CurrencyDisplay';

const CurrencyBalance = ({
  title, value, decimals, code, strong, className,
}) => (
  <div className={`${className} currency-balance`}>
    <div className="balance-title">{title}</div>
    <div className="value">
      <CurrencyDisplay value={value} code={code} decimals={decimals} strong={strong}/>
    </div>
  </div>
);

CurrencyBalance.propTypes = {
  title: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number, PropTypes.instanceOf(BigNumber)]),
  decimals: PropTypes.number,
  code: PropTypes.string,
  className: PropTypes.string,
  strong: PropTypes.bool,
};

CurrencyBalance.defaultProps = {
  value: undefined,
  decimals: 18,
  code: undefined,
  className: '',
  strong: true,
};

export default CurrencyBalance;
