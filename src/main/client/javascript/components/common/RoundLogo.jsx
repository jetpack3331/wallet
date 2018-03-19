import { Avatar } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import './RoundLogo.less';

const RoundLogo = ({ src, alt, className }) => (
  <Avatar className={`${className} round-logo-container`} src={src} alt={alt}/>
);

RoundLogo.propTypes = {
  src: PropTypes.oneOfType([PropTypes.string, PropTypes.object]).isRequired,
  alt: PropTypes.string,
  className: PropTypes.string,
};

RoundLogo.defaultProps = {
  alt: '',
  className: '',
};

export default RoundLogo;
