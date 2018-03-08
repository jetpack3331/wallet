import PropTypes from 'prop-types';
import React from 'react';
import { FormattedTime } from 'react-intl';

const Time = ({ value }) => (
  <FormattedTime value={value}/>
);

Time.propTypes = {
  value: PropTypes.instanceOf(Date).isRequired,
};

export default Time;
