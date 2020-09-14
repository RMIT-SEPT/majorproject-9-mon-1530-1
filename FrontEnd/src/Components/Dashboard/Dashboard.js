import React from 'react';
import styled from 'styled-components';
import { LogOut, ChevronDown } from 'react-feather';
import { theme } from '../../App';
import logoAlt from '../../media/logo-alt.png';
import {
  StyledNavWhite,
  StyledNavBarBorder,
  StyledLogoLink,
  GreenNavLink,
  RightFlexElements,
  StyledUserTagName,
} from '../Navigation/Nav';

// This is a wrapper component to use for dashboard views for the front-end. The
// idea is to allow for cohesive re-use of the same component, simply by provid-
// ing the values for the navigation bar through React props. As well, it will
// render any child element to the screen inside a flex container, for responsi-
// ve view. Note that a minimum screen width of 1304px is used to align the nav-
// igation bar and body element

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
  align-items: center;
`;

const Logo = styled.div`
  flex: none;
  width: 56px;
  height: 56px;
  background-color: ${(props) => props.theme.colours.green.primary};
  border-radius: 28px;
  margin: 0px 12px 0px 24px;
`;

const UserName = styled.div`
  text-align: right;
  font-size: 20px;
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
`;

const Role = styled.div`
  text-align: right;
  color: ${(props) => props.theme.colours.green.primary};
  font-size: 16px;
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
  text-transform: capitalize;
`;

// props gets passed in to the DashboardWrapper component to access dynamic ele-
// ments such as the userName and role

const DashboardWrapper = ({ children, userName, role, actions }) => {
  return (
    <StyledPageWrapper>
      <StyledNavWhite>
        <StyledNavBarBorder>
          <StyledLogoLink href="/">
            <img src={logoAlt} alt="AGEM black logo" />
          </StyledLogoLink>
          <GreenNavLink onClick={actions.bookingLink}>New Booking</GreenNavLink>
          <GreenNavLink>Appointments</GreenNavLink>
          <GreenNavLink>Contact-us</GreenNavLink>
          <GreenNavLink>About</GreenNavLink>
          <RightFlexElements>
            <StyledUserTagName>
              <UserName>{userName || 'empty'}</UserName>
              <Role>{role || 'empty'}</Role>
            </StyledUserTagName>
            <Logo></Logo>
            <ChevronDown size={28} />
          </RightFlexElements>
        </StyledNavBarBorder>
      </StyledNavWhite>
      <FlexContainer>{children}</FlexContainer>
    </StyledPageWrapper>
  );
};

// MenuBarComponent renders the side bar for quick functionality at hand

const MenuBarComponent = ({ children }) => {
  return (
    <MenuBar>
      <MenuContainer>
        {children}
        <LogOut
          className="menuIcon"
          data-testid="logOutIcon"
          color={theme.colours.grey.primary}
          size={theme.icons.size.medium}
        />
      </MenuContainer>
    </MenuBar>
  );
};

export { MenuBar, MenuIcon, DashboardWrapper, MenuBarComponent };
