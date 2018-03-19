import { BigNumber } from 'ethers/utils/index';
import PropTypes from 'prop-types';
import React from 'react';
import * as model from '../../model';
import CurrencyDisplay from './CurrencyDisplay';
import TokenIcon from '../../../images/icons/icon-token.png';
import RoundLogo from './RoundLogo';
import './TokenDetails.less';

const TokenDetails = ({ token, balance }) => {

  const icon = (token.logo && token.logo.src) || TokenIcon;
  return (
    <div className="token-details">
      <div>
        <RoundLogo src={icon} alt={token.name}/>
      </div>
      <div className="token-name">{token.name}</div>
      <CurrencyDisplay
        value={balance}
        code={token.symbol}
        decimals={token.decimals}
        strong
      />
    </div>
  )
};

TokenDetails.propTypes = {
  token: model.token.isRequired,
  balance: PropTypes.oneOfType([PropTypes.string,
    PropTypes.number, PropTypes.instanceOf(BigNumber)]),
};

TokenDetails.defaultProps = {
  balance: undefined,
};

export default TokenDetails;
