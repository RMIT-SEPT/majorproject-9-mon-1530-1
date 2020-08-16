import React from 'react';
import styled from 'styled-components';
import { useQuery } from 'react-query';
import DashboardWrapper from './DashboardWrapper';

const StyledPageWrapper = styled.div`
  background-color: #f5f5f5;
  height: 100vh;
`;

const fetchUserName = async () => {
  const result = await fetch('https://swapi.dev/api/people/1/');
  return result.json();
};

const Worker = () => {
  const { data, isError, isLoading, isSuccess } = useQuery(
    'name',
    fetchUserName
  );

  return (
    <StyledPageWrapper>
      {isError && <div>Error</div>}
      {isLoading && <div>Loading...</div>}
      {isSuccess && (
        <DashboardWrapper userName={data.name} role={data.eye_color} />
      )}
      {isSuccess && <div>{JSON.stringify(data)}</div>}
    </StyledPageWrapper>
  );
};

export default Worker;
