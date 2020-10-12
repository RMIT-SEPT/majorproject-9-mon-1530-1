import React from 'react';
import styled from 'styled-components';
import { ChevronDown } from 'react-feather';
import {
  RightFlexElements,
  StyledUserTagName,
} from '../Navigation/Nav';
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
const Logo = styled.div`
  flex: none;
  width: 56px;
  height: 56px;
  background-color: ${(props) => props.theme.colours.green.primary};
  border-radius: 28px;
  margin: 0px 12px 0px 24px;
`;
const rightNavElement = ({ userName, role }) => {
  function clear() {
    localStorage.clear('');
  }
  return (
    <RightFlexElements>
      <StyledUserTagName>
        <UserName>{userName || 'empty'}</UserName>
        <Role>{role || 'empty'}</Role>
      </StyledUserTagName>
      <Logo></Logo>

      <a href="/"><ChevronDown
        size={28}
        onClick={() => {
          clear();
        }} /></a>
    </RightFlexElements>
  );
};

export default rightNavElement;