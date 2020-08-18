import React from 'react';
import styled from 'styled-components';
import { useQuery } from 'react-query';
import { DashboardWrapper, MenuBarComponent, MenuIcon } from './Dashboard';
import home from '../media/home-40px.svg';
import book from '../media/book-40px.svg';
import info from '../media/info-40px.svg';
import phone from '../media/phone-40px.svg';

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
    <DashboardWrapper
      userName={data && data.name}
      role={data && data.eye_color}
    >
      <MenuBarComponent>
        <MenuIcon src={home} alt="Home icon" />
        <MenuIcon src={book} alt="Book icon" />
        <MenuIcon src={phone} alt="Phone icon" />
        <MenuIcon src={info} alt="Info icon" />
      </MenuBarComponent>
      {isSuccess && <TempContainer>{JSON.stringify(data)}</TempContainer>}
    </DashboardWrapper>
  );
};

export default Worker;
