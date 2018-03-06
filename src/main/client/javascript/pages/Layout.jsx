import PropTypes from 'prop-types';
import React from 'react';
import { AppBar, IconButton, Menu, MenuItem, Toolbar } from 'material-ui';
import AccountCircle from 'material-ui-icons/AccountCircle';
import { connect } from 'react-redux';
import './Layout.less';
import { getLoggedInUser } from '../reducers';

const open = false;
const logoImage = require('../../images/logo-hut34-wallet.png');

const Layout = ({ user, children }) => (
  <div className="layout">
    <AppBar className="app-bar" position="static" color="inherit">
      <Toolbar>
        <div className="title-container">
          <img className="logo" src={logoImage} alt="HUT34 Wallet"/>
        </div>
        {user && user.name && (
          <div>
            <IconButton
              aria-owns={open ? 'menu-appbar' : null}
              aria-haspopup="true"
              onClick={this.handleMenu}
              color="inherit"
            >
              <AccountCircle/>
            </IconButton>
            <span>{user.name}</span>
            <Menu
              id="menu-appbar"
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={open}
              onClose={this.handleClose}
            >
              <MenuItem onClick={this.handleClose}>Logout</MenuItem>
            </Menu>
          </div>
        )}
      </Toolbar>
    </AppBar>
    <div className="default-layout">
      {children}
    </div>
  </div>
);

Layout.propTypes = {
  user: PropTypes.object,
  children: PropTypes.oneOfType([
    PropTypes.node,
    PropTypes.arrayOf(PropTypes.node),
  ]).isRequired,
};

Layout.defaultProps = {
  user: null,
};

const mapStateToProps = state => ({
  user: getLoggedInUser(state),
});

const actions = {};

export default connect(mapStateToProps, actions)(Layout);
