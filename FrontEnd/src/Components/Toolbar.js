import React from 'react';
import {
  StyledNavBlack,
  StyledNavBar,
  StyledLogoLink,
  GreenNavLink,
  RightFlexElements,
  StyledFormButton,
} from './Navigation/Nav';
import logo from '../media/logo.png';
import login from '../media/login.png';
import small from '../media/small.png';

// this is the toolbar for the guest user
// items are allocated evenly using a Grid function in material ui library
// we use normal routing in order to move between pages

const toolbar = () => {
  return (
    <StyledNavBlack>
      <StyledNavBar>
        <StyledLogoLink href="/">
          <img src={logo} alt="logo" />
        </StyledLogoLink>
        <a
          href="http://localhost:3000/contactus"
          style={{ textDecoration: 'none' }}
        >
          <GreenNavLink>New Booking</GreenNavLink>
        </a>
        <a
          href="http://localhost:3000/contactus"
          style={{ textDecoration: 'none' }}
        >
          <GreenNavLink>Contact-us</GreenNavLink>
        </a>
        <GreenNavLink>Appointments </GreenNavLink>
        <a
          href="http://localhost:3000/about"
          style={{ textDecoration: 'none' }}
        >
          <GreenNavLink>About</GreenNavLink>
        </a>
        <RightFlexElements>
          <GreenNavLink>
            <a href="http://localhost:3000/login">
              <img className="login" src={login} alt="login" />{' '}
            </a>
          </GreenNavLink>
          <GreenNavLink>
            <a href="http://localhost:3000/form">
              <img className="small" src={small} alt="small" />{' '}
            </a>
          </GreenNavLink>
        </RightFlexElements>
      </StyledNavBar>
    </StyledNavBlack>
  );
};
export default toolbar;
