import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { AppBar, IconButton, Menu, MenuItem, Toolbar } from 'material-ui';
import AccountCircle from 'material-ui-icons/AccountCircle';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import { push } from 'react-router-redux';
import './Layout.less';
import { getLoggedInUser } from '../reducers';
import { logout } from '../actions/auth';
import SessionExpiryNotification from '../components/SessionExpiryNotification';

const logoImage = require('../../images/logo-hut34-wallet.png');

class Layout extends Component {
  static propTypes = {
    user: PropTypes.object,
    handleLogout: PropTypes.func.isRequired,
    children: PropTypes.oneOfType([
      PropTypes.node,
      PropTypes.arrayOf(PropTypes.node),
    ]).isRequired,
  };

  static defaultProps = {
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
        <AppBar className="app-bar" position="static" color="primary">
          <Toolbar>
            <div className="title-container">
              <img className="logo" src={logoImage} alt="HUT34 Wallet"/>
            </div>
            {user && user.email && (
              <div>
                <IconButton
                  aria-owns={anchorEl ? 'menu-appbar' : null}
                  aria-haspopup="true"
                  onClick={this.handleMenu}
                >
                  <AccountCircle/>
                </IconButton>
                <span>{user.name}</span>
                <Menu
                  id="menu-appbar"
                  anchorEl={anchorEl}
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
          <footer>
            <p className="footer-disclaimer">
              2018&copy; Hut34 Wallet |
              Making Ethereum wallets easier for AI, bots, and people | <Link href="https://docsend.com/view/sxfgj6w" target="_blank">Terms and Conditions</Link>
            </p>
          </footer>
        </div>
        <SessionExpiryNotification/>
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
