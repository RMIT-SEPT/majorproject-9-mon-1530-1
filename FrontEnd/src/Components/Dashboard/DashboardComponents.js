import React, { useState } from 'react';
import styled from 'styled-components';
import { Watch, Clock, User } from 'react-feather';
import BarLoader from 'react-spinners/BarLoader';
import { theme } from '../../App';
import hairdresserImage from '../../media/hairdresser-card.png';

// Components defined here are specifically used for dashboard appointments

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

const AppointmentsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 280px);
  gap: 20px;
`;

const PanelGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(1, 840px);
  gap: 20px;
`;

const AvailabilityGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(1, 400px);
  gap: 20px;
`;

const BookingGrid = styled.div`
  display: grid;
  grid-template-columns: 400px 600px;
  gap: 40px;
`;

const StyledDashboardModule = styled.div`
  margin-top: 24px;
`;

const Title = styled.span`
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
  font-size: 24px;
`;

const Children = styled.div`
  margin-top: 8px;
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
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
`;

const Button = styled.button`
  margin-top: 24px;
  padding: 4px 24px;
  font-family: ${(props) => props.theme.font.primary};
  font-size: 16px;
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
  color: white;
  border: 2px solid transparent;
  border-radius: 4px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.25);
  background-color: ${(props) => props.theme.colours.green.primary};
  outline: 0px;
  transition: background-color ${(props) => props.theme.transition.short};

  &:hover {
    background-color: ${(props) => props.theme.colours.green.secondary};
  }

  &:active {
    background-color: ${(props) => props.theme.colours.green.tertiary};
    border: 2px solid black;
    position: relative;
    top: 4px;
  }
`;

const StyledGreenText = styled.span`
  color: ${(props) => props.theme.colours.green.primary};
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
`;
const SelectedTime = styled.span`
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
  font-size:25px;
`;

const StyledStateContainer = styled.div`
  display: flex;
  margin: auto;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const DashboardModule = ({ children, title }) => {
  return (
    <StyledDashboardModule>
      <span>
        <Title>{title}</Title>
      </span>
      <Children>{children}</Children>
    </StyledDashboardModule>
  );
};

const UpcomingAppointmentCard = ({ booking }) => {
  const [startTime] = useState(new Date(booking.startDateTime));
  const [endTime] = useState(new Date(booking.endDateTime));

  return (
    // TODO: Solve responsive flex layout for these components when there are many elements in a single row
    <StyledAppointmentCard>
      <CardImage src={hairdresserImage} alt="Hairdresser image" />
      <CardContent>
        <div>
          <User
            className="upcomingAppointmentsCardIcon"
            size={theme.icons.size.small}
          />
          <Strong>{booking.workerUsername}</Strong>
        </div>
        <br />
        <div>
          <Watch
            className="upcomingAppointmentsCardIcon"
            size={theme.icons.size.small}
          />
          {startTime.toLocaleDateString()}
        </div>
        <div>
          <Clock
            className="upcomingAppointmentsCardIcon"
            size={theme.icons.size.small}
          />
          <span>
            <span>{startTime.toLocaleTimeString()}</span> to{' '}
            <span>{endTime.toLocaleTimeString()}</span>
          </span>
        </div>
      </CardContent>
    </StyledAppointmentCard>
  );
};

const Loading = () => {
  return (
    <StyledStateContainer>
      <BarLoader height={8} width={200} color={theme.colours.green.primary} />
    </StyledStateContainer>
  );
};

const Error = () => {
  return <StyledStateContainer>Error...</StyledStateContainer>;
};

export {
  DashboardModule,
  UpcomingAppointmentCard,
  Heading,
  SubHeading,
  Content,
  AppointmentsGrid,
  AvailabilityGrid,
  BookingGrid,
  PanelGrid,
  Title,
  Button,
  StyledGreenText,
  SelectedTime,
  StyledStateContainer,
  Loading,
  Error,
};
