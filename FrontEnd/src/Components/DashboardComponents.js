import React from 'react';
import styled from 'styled-components';
import hairdresserImage from '../media/hairdresser-card.png';

const StyledDashboardModule = styled.div`
  grid-column: span 12;
  ${'' /* background-color: #90ee90; */}
`;

const ClickableSpan = styled.span`
  transition: color 0.15s;

  &:hover {
    cursor: pointer;
    color: #5ac490;
  }
`;

const Title = styled.span`
  font-weight: 550;
  font-size: 24px;
`;

const Children = styled.div`
  margin-top: 8px;
`;

const TitleIcon = styled.img`
  position: relative;
  top: 2px;
  left: 8px;

  ${'' /* &:hover {
    cursor: pointer;
  } */}
`;

const CardImage = styled.img`
  ${'' /* width: 300px;
  height: 100px; */}
  border-radius: 4px 4px 0px 0px;
`;

const StyledAppointmentCard = styled.div`
  flex: none;
  background-color: white;
  width: 280px;
  height: 256px;
  margin-right: 24px;
  border-radius: 4px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.25);
`;

const CardContent = styled.div`
  padding: 0px 12px 12px 12px;
`;

export const DashboardModule = ({ children, title, icon, action }) => {
  return (
    <StyledDashboardModule>
      {action && (
        <ClickableSpan onClick={action}>
          <Title>{title}</Title>
          <TitleIcon src={icon} />
        </ClickableSpan>
      )}
      {!action && (
        <span>
          <Title>{title}</Title>
          <TitleIcon src={icon} />
        </span>
      )}
      <Children>{children}</Children>
    </StyledDashboardModule>
  );
};

export const UpcomingAppointmentCard = ({ children }) => {
  return (
    // TODO: Solve responsive grid layout for these components when there are many elements in a single row
    <StyledAppointmentCard>
      <CardImage src={hairdresserImage} alt="Hairdresser image" />
      <CardContent>{children.employeeUsername}</CardContent>
    </StyledAppointmentCard>
  );
};
