import styled from 'styled-components';

const StyledNav = styled.div`
  background-color: white;
  min-width: ${(props) => props.theme.dashboard.defaultWidth};
`;

const StyledNavWhite = styled(StyledNav)`
  background-color: white;
  min-width: ${(props) => props.theme.dashboard.defaultWidth};
`;

const StyledNavBlack = styled.div`
  background-color: black;
  min-width: ${(props) => props.theme.dashboard.defaultWidth};
`;

const StyledNavBar = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 38px 12px 38px;
`;

const StyledNavBarBorder = styled(StyledNavBar)`
  outline: 2px solid #cccccc;
`;

const StyledLogoLink = styled.a`
  flex: none;
  margin-right: 20px;
`;

const GreenNavLink = styled.div`
  flex: none;
  color: ${(props) => props.theme.colours.greenPrimary};
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
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

const RightFlexElements = styled.div`
  margin: 0px 0px 4px auto;
  padding-left: 30px;
  display: flex;
  align-items: center;
`;

const StyledFormButton = styled.div`
  flex: none;
  padding-left: 30px;
`;

const StyledUserTagName = styled.div`
  flex: none;
`;

export {
  StyledNavWhite,
  StyledNavBlack,
  StyledNavBar,
  StyledLogoLink,
  GreenNavLink,
  RightFlexElements,
  StyledUserTagName,
  StyledNavBarBorder,
  StyledFormButton,
};
