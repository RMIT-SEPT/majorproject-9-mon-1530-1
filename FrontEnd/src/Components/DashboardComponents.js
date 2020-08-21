import React from 'react';
import styled from 'styled-components';
import hairdresserImage from '../media/hairdresser-card.png';

const StyledDashboardModule = styled.div`
  grid-column: span 12;
`;

const ClickableSpan = styled.span`
  transition: color ${(props) => props.theme.transition.short};

  &:hover {
    cursor: pointer;
    text-decoration: underline;
    color: ${(props) => props.theme.colours.greenPrimary};
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
`;

const CardImage = styled.img`
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

const DashboardModule = ({ children, title, icon, action }) => {
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

const UpcomingAppointmentCard = ({ children }) => {
  return (
    // TODO: Solve responsive grid layout for these components when there are many elements in a single row
    <StyledAppointmentCard>
      <CardImage src={hairdresserImage} alt="Hairdresser image" />
      <CardContent>{children.employeeUsername}</CardContent>
    </StyledAppointmentCard>
  );
};

export { DashboardModule, UpcomingAppointmentCard };
