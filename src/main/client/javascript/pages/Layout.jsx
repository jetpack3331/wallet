import PropTypes from 'prop-types';
import React from 'react';

const Layout = ({ children }) => (
  <div className="default-layout">
    { children }
  </div>
);

Layout.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.node,
    PropTypes.arrayOf(PropTypes.node),
  ]).isRequired,
};

export default Layout;
