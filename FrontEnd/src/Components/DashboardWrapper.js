import React from 'react';
import styled from 'styled-components';
import seclogo from '../media/seclogo.png';
import chevronDown from '../media/chevron-down-28px.svg';

const StyledNavBar = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 38px 12px 38px;
  outline: 2px solid #cccccc;
`;

const StyledLogoLink = styled.a`
  flex: none;
  margin-right: 20px;
`;

const GreenNavLink = styled.div`
  flex: none;
  color: #5ac490;
  font-weight: 550;
  font-size: 28px;
  margin: 0px 30px 4px 30px;
`;

const StyledUserTag = styled.div`
  margin: 0px 0px 4px auto;
  display: flex;
  align-items: center;
`;

const Logo = styled.div`
  flex: none;
  width: 56px;
  height: 56px;
  background-color: #5ac490;
  border-radius: 28px;
  margin: 0px 12px 0px 24px;
`;

const StyledUserTagName = styled.div`
  flex: none;
`;

const UserName = styled.div`
  text-align: right;
  font-size: 20px;
  font-weight: 550;
`;

const Role = styled.div`
  text-align: right;
  color: #5ac490;
  font-size: 16px;
  font-weight: 550;
  text-transform: capitalize;
`;

const StyledDashboardNav = styled.div`
  background-color: white;
`;

const DashboardWrapper = (props) => {
  return (
    <>
      <StyledDashboardNav>
        <StyledNavBar>
          <StyledLogoLink href="/">
            <img src={seclogo} alt="logo" />
          </StyledLogoLink>
          <GreenNavLink>New Booking</GreenNavLink>
          <GreenNavLink>Appointments</GreenNavLink>
          <GreenNavLink>Contact-us</GreenNavLink>
          <GreenNavLink>About</GreenNavLink>
          <StyledUserTag>
            <StyledUserTagName>
              <UserName>{props.userName}</UserName>
              <Role>{props.role}</Role>
            </StyledUserTagName>
            <Logo></Logo>
            <img src={chevronDown} alt="Chevron drop down button" />
          </StyledUserTag>
        </StyledNavBar>
      </StyledDashboardNav>
    </>
  );
};

export default DashboardWrapper;
