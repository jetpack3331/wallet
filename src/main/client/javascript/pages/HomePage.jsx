import React from 'react';
import { Link } from 'react-router';
import './HomePage.less';


const HomePage = () => (
  <div className="home-page">
    <h1 className="display-2">Hut34 Wallet</h1>
    <div className="widgets">
      <div>
        <Link to="/login/google" target="_self">Login</Link>
      </div>
    </div>
  </div>
);

export default HomePage;
