import React from 'react';
import styled from 'styled-components';
import { useQuery } from 'react-query';
import DashboardWrapper from './DashboardWrapper';
import home from '../media/home-40px.svg';
import book from '../media/book-40px.svg';
import info from '../media/info-40px.svg';
import phone from '../media/phone-40px.svg';
import logout from '../media/log-out-40px.svg';

const StyledPageWrapper = styled.div`
  background-color: #f5f5f5;
  height: 100vh;
  min-width: 1304px;
`;

const FlexContainer = styled.div`
  display: flex;
`;

const MenuBar = styled.div`
  flex: none;
  width: 106px;
  height: calc(100vh - 100px);
  background-color: white;
  box-shadow: 2px 0px #cccccc;
  margin-right: 2px;
`;

const MenuContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin: 20px 0px;
  height: calc(100% - 40px);
`;

const MenuIcon = styled.img`
  margin: 20px 0px;
`;

const TempContainer = styled.div`
  flex: 0 1 800px;
`;

const fetchUser = async () => {
  const result = await fetch('https://swapi.dev/api/people/1/');
  return result.json();
};

const Worker = () => {
  const { data, isSuccess } = useQuery('name', fetchUser);

  return (
    <StyledPageWrapper>
      <DashboardWrapper
        userName={data && data.name}
        role={data && data.eye_color}
      />
      <FlexContainer>
        <MenuBar>
          <MenuContainer>
            <MenuIcon src={home} alt="Home icon" />
            <MenuIcon src={book} alt="Book icon" />
            <MenuIcon src={phone} alt="Phone icon" />
            <MenuIcon src={info} alt="Info icon" />
            <MenuIcon
              style={{ margin: 'auto 0px 20px 0px', paddingTop: '20px' }}
              src={logout}
              alt="Log out icon"
            />
          </MenuContainer>
        </MenuBar>
        {isSuccess && <TempContainer>{JSON.stringify(data)}</TempContainer>}
      </FlexContainer>
    </StyledPageWrapper>
  );
};

export default Worker;
