import React from 'react'
import styled from 'styled-components';
import logo from '../media/logo.png';
import login from '../media/login.png';
import small from '../media/small.png';


const StyledDashboardNav = styled.div`
  background-color: black;
  min-width: ${(props) => props.theme.dashboard.defaultWidth};
`;
const StyledNavBar = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 38px 12px 38px;

`;
const StyledLogoLink = styled.a`
  flex: none;
  margin-right: 20px;
`;
const GreenNavLink = styled.div`
  flex: none;
  color: ${(props) => props.theme.colours.greenPrimary};
  font-weight: 550;
  font-size: 28px;
  margin: 0px 30px 4px 30px;
  transition: color ${(props) => props.theme.transition.short};

  &:hover {
    cursor: pointer;
    text-decoration: underline;
    color: ${(props) => props.theme.colours.greenSecondary};
  }

  &:active {
    cursor: pointer;
    text-decoration: underline;
    color: ${(props) => props.theme.colours.greenTertiary};
  }
  
`;
const StyledUserTag = styled.div`
  margin: 0px 0px 4px auto;
  padding-left: 30px;
  display: flex;
  align-items: center;
`;
// this is the toolbar for the guest user 
// items are allocated evenly using a Grid function in material ui library 
// we use normal routing in order to move between pages 

const toolbar = () => {
  return (
    <StyledDashboardNav>
      <StyledNavBar>
        <StyledLogoLink href="/">
          <img src={logo} alt="logo" />
        </StyledLogoLink>
        <a href="http://localhost:3000/contactus" style={{ textDecoration: 'none' }}>
          <GreenNavLink>New Booking</GreenNavLink>
        </a>
        <a href="http://localhost:3000/contactus" style={{ textDecoration: 'none' }}>
          <GreenNavLink>Contact-us</GreenNavLink>
        </a>
        <GreenNavLink>Appointments </GreenNavLink>
        <a href="http://localhost:3000/about" style={{ textDecoration: 'none' }}>
          <GreenNavLink>About</GreenNavLink>
        </a>
        <StyledUserTag>
          <GreenNavLink>
            <a href="http://localhost:3000/login"> <img className="login" src={login} alt="login" /> </a>
          </GreenNavLink>
          <GreenNavLink>
            <a href="http://localhost:3000/form"> <img className="small" src={small} alt="small" /> </a>
          </GreenNavLink>
        </StyledUserTag>
      </StyledNavBar>
    </StyledDashboardNav>
  )
}
export default toolbar; 