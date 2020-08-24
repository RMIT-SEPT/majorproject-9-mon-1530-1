import React, { useState } from 'react';
import styled from 'styled-components';
import hairdresserImage from '../../media/hairdresser-card.png';
import watch from '../../media/watch-16px.svg';
import clock from '../../media/clock-16px.svg';
import user from '../../media/user-16px.svg';

const Heading = styled.div`
  font-weight: bold;
  font-size: 56px;
  margin-bottom: -8px;
`;

const SubHeading = styled.div`
  font-size: 24px;
  color: #707070;
`;

const Content = styled.div`
  width: 100%;
  margin: 40px;
`;

const DashboardGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  column-gap: 24px;
  row-gap: 24px;
  margin: 24px 0px;
`;

const AppointmentsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 280px);
  gap: 24px;
`;

const PanelGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(1, 840px);
  gap: 24px;
`;

const StyledDashboardModule = styled.div`
  grid-column: span 2;
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
  padding: 4px 16px 12px 16px;
`;

const Strong = styled.span`
  font-weight: 550;
`;

const AppointmentCardLogo = styled.img`
  position: relative;
  right: 2px;
  top: 3px;
  margin-right: 4px;
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
  const [startTime] = useState(new Date(children.startDateTime));
  const [endTime] = useState(new Date(children.endDateTime));

  return (
    // TODO: Solve responsive grid layout for these components when there are many elements in a single row
    <StyledAppointmentCard>
      <CardImage src={hairdresserImage} alt="Hairdresser image" />
      <CardContent>
        <div>
          <AppointmentCardLogo src={user} alt="User logo" />
          <Strong>{children.workerUsername}</Strong>
        </div>
        <br />
        <div>
          <AppointmentCardLogo src={watch} alt="Watch logo" />
          {startTime.toLocaleDateString()}
        </div>
        <div>
          <AppointmentCardLogo src={clock} alt="Clock logo" />
          {startTime.toLocaleTimeString()} to {endTime.toLocaleTimeString()}
        </div>
      </CardContent>
    </StyledAppointmentCard>
  );
};

export {
  DashboardModule,
  UpcomingAppointmentCard,
  Heading,
  SubHeading,
  Content,
  DashboardGrid,
  AppointmentsGrid,
  PanelGrid,
  Title,
};
