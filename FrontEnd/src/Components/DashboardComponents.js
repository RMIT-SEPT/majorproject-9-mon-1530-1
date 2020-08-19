import React from 'react';
import styled from 'styled-components';

const StyledDashboardModule = styled.div`
  grid-column: span 6;
  background-color: #90ee90;
`;

const Title = styled.div`
  font-weight: 550;
  font-size: 24px;
  margin-bottom: 8px;
`;

const StyledAppointmentCard = styled.div``;

export const DashboardModule = ({ children, title }) => {
  return (
    <StyledDashboardModule>
      <Title>{title}</Title>
      {children}
    </StyledDashboardModule>
  );
};

export const UpcomingAppointmentCard = ({ children }) => {
  return <StyledAppointmentCard>{children}</StyledAppointmentCard>;
};
