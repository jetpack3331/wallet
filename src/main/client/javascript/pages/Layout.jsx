import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { AppBar, IconButton, Menu, MenuItem, Toolbar } from 'material-ui';
import AccountCircle from 'material-ui-icons/AccountCircle';
import { connect } from 'react-redux';
import { push } from 'react-router-redux';
import './Layout.less';
import { getLoggedInUser } from '../reducers';
import { logout } from '../actions/auth';

const logoImage = require('../../images/logo-hut34-wallet.png');

class Layout extends Component {
  propTypes = {
    user: PropTypes.object,
    handleLogout: PropTypes.func.isRequired,
    children: PropTypes.oneOfType([
      PropTypes.node,
      PropTypes.arrayOf(PropTypes.node),
    ]).isRequired,
  };

  defaultProps = {
    user: undefined,
  };

  state = {
    anchorEl: undefined,
  };

  handleMenu = (event) => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleClose = () => {
    this.setState({ anchorEl: null });
  };

  render() {
    const { user, children, handleLogout } = this.props;
    const { anchorEl } = this.state;

    return (
      <div className="layout">
        <AppBar className="app-bar" position="static" color="inherit">
          <Toolbar>
            <div className="title-container">
              <img className="logo" src={logoImage} alt="HUT34 Wallet"/>
            </div>
            {user && user.name && (
              <div>
                <IconButton
                  aria-owns={anchorEl ? 'menu-appbar' : null}
                  aria-haspopup="true"
                  onClick={this.handleMenu}
                  color="inherit"
                >
                  <AccountCircle/>
                </IconButton>
                <span>{user.name}</span>
                <Menu
                  id="menu-appbar"
                  anchorEl={anchorEl}
                  anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                  }}
                  transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                  }}
                  open={Boolean(anchorEl)}
                  onClose={this.handleClose}
                >
                  <MenuItem onClick={handleLogout}>Logout</MenuItem>
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
  }
}

const mapStateToProps = state => ({
  user: getLoggedInUser(state),
});

const actions = dispatch => ({
  handleLogout: () =>
    dispatch(logout())
      .then(() => dispatch(push('/'))),
});

export default connect(mapStateToProps, actions)(Layout);
