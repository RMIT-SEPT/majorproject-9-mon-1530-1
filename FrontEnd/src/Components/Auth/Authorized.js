import React from 'react';
import { Link } from 'react-router-dom';
import './Unauthorized.scss';

const Authorized = () => {
  return (
    <div className='container'>
      <div class="gandalf">
        <div class="fireball"></div>
        <div class="skirt"></div>
        <div class="sleeves"></div>
        <div class="shoulders">
          <div class="hand left"></div>
          <div class="hand right"></div>
        </div>
        <div class="head">
          <div class="hair"></div>
          <div class="beard"></div>
        </div>
      </div>
      <div class="message">
        <h1>403 - You Shall Pass</h1>
        <p>Welcome!<br /></p>
      </div>
      <p><Link to='/'>Back to Home</Link></p>
    </div>
  )
}

export default Authorized;