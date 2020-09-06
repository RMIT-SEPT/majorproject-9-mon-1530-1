import React from 'react';
import styled from 'styled-components';
import seclogo from '../../media/seclogo.png';
import logout from '../../media/log-out-40px.svg';
import chevronDown from '../../media/chevron-down-28px.svg';

// This is a wrapper component to use for dashboard views for the front-end. The
// idea is to allow for cohesive re-use of the same component, simply by provid-
// ing the values for the navigation bar through React props. Note that a minim-
// um screen width of 1304px is used to align the navigation bar and body eleme-
// nt

const MenuBar = styled.div`
  flex: none;
  width: 106px;
  min-height: calc(100vh - 100px);
  background-color: white;
  box-shadow: 2px 0px #cccccc;
  margin-right: 2px;
`;

const MenuIcon = styled.img`
  margin: 20px 0px;
`;

const StyledPageWrapper = styled.div`
  background-color: #f5f5f5;
  min-width: ${(props) => props.theme.dashboard.defaultWidth};
`;

const FlexContainer = styled.div`
  display: flex;
`;

const MenuContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin: 20px 0px;
  height: calc(100% - 40px);
`;

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

const Logo = styled.div`
  flex: none;
  width: 56px;
  height: 56px;
  background-color: ${(props) => props.theme.colours.greenPrimary};
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
  color: ${(props) => props.theme.colours.greenPrimary};
  font-size: 16px;
  font-weight: 550;
  text-transform: capitalize;
`;

const StyledDashboardNav = styled.div`
  background-color: white;
  min-width: ${(props) => props.theme.dashboard.defaultWidth};
`;

// props gets passed in to the DashboardWrapper component to access dynaic elem-
// ents such as the userName and role

const DashboardWrapper = ({ children, userName, role, actions }) => {
  return (
    <StyledPageWrapper>
      <StyledDashboardNav>
        <StyledNavBar>
          <StyledLogoLink href="/">
            <img src={seclogo} alt="logo" />
          </StyledLogoLink>
          <GreenNavLink onClick={actions.bookingLink}>New Booking</GreenNavLink>
          <GreenNavLink>Appointments</GreenNavLink>
          <GreenNavLink>Contact-us</GreenNavLink>
          <GreenNavLink>About</GreenNavLink>
          <StyledUserTag>
            <StyledUserTagName>
              <UserName>{userName || 'empty'}</UserName>
              <Role>{role || 'empty'}</Role>
            </StyledUserTagName>
            <Logo></Logo>
            <img src={chevronDown} alt="Chevron drop down button" />
          </StyledUserTag>
        </StyledNavBar>
      </StyledDashboardNav>
      <FlexContainer>{children}</FlexContainer>
    </StyledPageWrapper>
  );
};

const MenuBarComponent = ({ children }) => {
  return (
    <MenuBar>
      <MenuContainer>
        {children}
        <MenuIcon src={logout} alt="Log out icon" />
      </MenuContainer>
    </MenuBar>
  );
};

export { MenuBar, MenuIcon, DashboardWrapper, MenuBarComponent };
