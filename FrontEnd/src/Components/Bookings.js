import React, { useState } from 'react';
import { useQuery } from 'react-query';
import styled from 'styled-components';
import { DashboardWrapper, MenuBarComponent, MenuIcon } from './Dashboard';
import {
  DashboardModule,
  UpcomingAppointmentCard,
} from './DashboardComponents';
import home from '../media/home-40px.svg';
import book from '../media/book-40px.svg';
import calendar from '../media/calendar-40px.svg';
import phone from '../media/phone-40px.svg';

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
  grid-template-columns: repeat(12, 1fr);
  column-gap: 24px;
  row-gap: 24px;
  margin: 24px 0px;
`;

const GridItem = styled.div`
  grid-column: span 6;
  background-color: #add8e6;
`;

const Grid = styled.div`
  display: grid;
`;

const tempBookings = [
  {
    bookingId: '00003',
    customerUsername: 'rw22448',
    employeeUsername: 'anthony',
    date: '24/07/2020',
    time: 900,
    duration: 1.5,
  },
  {
    bookingId: '00004',
    customerUsername: 'rw22448',
    employeeUsername: 'jeffOak',
    date: '24/07/2020',
    time: 1000,
    duration: 1.5,
  },
];

const Bookings = ({ userId }) => {
  const fetchUserData = async (key, userId) => {
    const result = await fetch(
      `http://localhost:8080/users?username=${userId}`
    ).catch((e) => {
      console.log('Error: ', e);
    });

    return result.json();
  };

  const { data, isSuccess, isLoading, isError } = useQuery(
    ['userData', userId],
    fetchUserData,
    {
      onSuccess: (data) => {
        setUserName(data.name);
        setRole(data.userType);
      },
    }
  );
  const [userName, setUserName] = useState(userId);
  const [role, setRole] = useState('User');
  const [date, setDate] = useState(new Date());

  return (
    <>
      {isLoading && <div>Loading...</div>}
      {isError && <div>Error...</div>}
      {isSuccess && (
        <DashboardWrapper userName={data.name} role={role}>
          <MenuBarComponent>
            <MenuIcon src={home} alt="Home icon" />
            <MenuIcon src={book} alt="Book icon" />
            <MenuIcon src={phone} alt="Phone icon" />
            <MenuIcon src={calendar} alt="Calendar icon" />
          </MenuBarComponent>
          <Content>
            <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
            <SubHeading>Today is {date.toLocaleDateString()}.</SubHeading>
            <DashboardGrid>
              <DashboardModule title="Upcoming appointments">
                {/* content of Upcoming appointments DashboardModule will change depending on how many appointments for the user */}
                <Grid>
                  {tempBookings.map((booking) => (
                    <UpcomingAppointmentCard key={booking.bookingId}>
                      {booking.employeeUsername}
                    </UpcomingAppointmentCard>
                  ))}
                </Grid>
              </DashboardModule>
              <GridItem>Service</GridItem>
              <GridItem>Service</GridItem>
            </DashboardGrid>
          </Content>
        </DashboardWrapper>
      )}
    </>
  );
};

Bookings.defaultProps = {
  userId: 'rw22448',
};

export default Bookings;
