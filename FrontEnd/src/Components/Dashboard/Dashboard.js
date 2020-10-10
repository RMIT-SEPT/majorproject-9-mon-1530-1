import React from 'react';
import styled from 'styled-components';
import { LogOut } from 'react-feather';
import { theme } from '../../App';
import { Link } from 'react-router-dom';
import logoAlt from '../../media/logo-alt.png';
import {
  StyledNavWhite,
  StyledNavBarBorder,
  StyledLogoLink,
  GreenNavLink,
} from '../Navigation/Nav';
import RightNavElement from '../Navigation/RightNavElemnet';

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
          {/* TODO: Allow parent to control what navigation links show (booking not always needed) */}
          <GreenNavLink onClick={actions.bookingLink}>New Booking</GreenNavLink>
          <GreenNavLink>Appointments</GreenNavLink>
          <a href="/contactus" style={{ textDecoration: 'none' }}>
            <GreenNavLink>Contact-us</GreenNavLink>
          </a>
          <a href="/about" style={{ textDecoration: 'none' }}>
          <GreenNavLink>About</GreenNavLink>
          </a>
          <RightNavElement userName={userName}
            role={role}></RightNavElement>
        </StyledNavBarBorder>
      </StyledNavWhite>
      <FlexContainer>{children}</FlexContainer>
    </StyledPageWrapper>
  );
};

// MenuBarComponent renders the side bar for quick functionality at hand

const MenuBarComponent = ({ children }) => {
  function clear() {
    localStorage.clear('');
  }

  return (
    <MenuBar>
      <MenuContainer>
        {children}
        <Link to="/">
          <LogOut
            className="menuIcon"
            data-testid="logOutIcon"
            color={theme.colours.grey.primary}
            size={theme.icons.size.medium}
            onClick={() => {
              clear();
            }} />

        </Link>

      </MenuContainer>
    </MenuBar>
  );
};

export { MenuBar, MenuIcon, DashboardWrapper, MenuBarComponent };
