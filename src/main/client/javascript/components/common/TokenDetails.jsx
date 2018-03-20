import { BigNumber, bigNumberify } from 'ethers/utils';
import { Button } from 'material-ui';
import SendIcon from 'material-ui-icons/Send';
import PropTypes from 'prop-types';
import React from 'react';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import TokenIcon from '../../../images/icons/icon-token.png';
import * as model from '../../model';
import CurrencyDisplay from './CurrencyDisplay';
import RoundLogo from './RoundLogo';
import './TokenDetails.less';

const TokenDetails = ({ token, balance, navigateSendToken }) => {
  const icon = (token.logo && token.logo.src) || TokenIcon;
  const nonZeroBalance = balance && bigNumberify(balance).gt(0);

  return (
    <div className="token-details">
      <div className="logo-action">
        <RoundLogo src={icon} alt={token.name}/>
        {nonZeroBalance &&
        <Button
          className="btn-secondary"
          variant="raised"
          size="small"
          onClick={navigateSendToken}
        >
          <SendIcon className="icon-small btn-icon-left"/>
          Send
        </Button>
        }
      </div>
      <div className="token-name">{token.name}</div>
      <CurrencyDisplay
        value={balance}
        code={token.symbol}
        decimals={token.decimals}
        strong
      />
    </div>
  );
};

TokenDetails.propTypes = {
  token: model.token.isRequired,
  navigateSendToken: PropTypes.func.isRequired,
  balance: PropTypes.oneOfType([PropTypes.string,
    PropTypes.number, PropTypes.instanceOf(BigNumber)]),
};

TokenDetails.defaultProps = {
  balance: undefined,
};

const mapDispatchToProps = (dispatch, ownProps) => ({
  navigateSendToken: () => dispatch(push(`/addresses/${ownProps.walletAddress}/${ownProps.token.symbol}/send`)),
});

export default connect(null, mapDispatchToProps)(TokenDetails);
