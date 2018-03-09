import PropTypes from 'prop-types';
import React from 'react';
import { FormattedDate } from 'react-intl';
import Time from './Time';
import './TimeAgo.less';


const DateTime = ({ value, unix }) => {
  const dateVal = unix ? new Date(value * 1000) : value;

  return (
    <span className="date-time"><FormattedDate value={dateVal}/> <Time value={dateVal}/></span>

  );
};

DateTime.propTypes = {
  value: (PropTypes.instanceOf(Date) || PropTypes.string || PropTypes.number).isRequired,
  unix: PropTypes.bool,
};

DateTime.defaultProps = {
  unix: false,
};

export default DateTime;
