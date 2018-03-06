import React from 'react';
import { Link } from 'react-router';
import './HomePage.less';

const HomePage = () => (
  <div className="home-page">
    <div>
      <Link to="/login/google" target="_self">Login</Link>
    </div>
  </div>
);

export default HomePage;
