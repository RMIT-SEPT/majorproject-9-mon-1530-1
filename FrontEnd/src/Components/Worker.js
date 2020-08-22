import React, { useState } from 'react';
import { useQuery } from 'react-query';
import styled from 'styled-components';
import { DashboardWrapper, MenuBarComponent, MenuIcon } from './Dashboard';
import {
  DashboardModule,
  UpcomingAppointmentCard,
  ServiceCard,
} from './DashboardComponents';
import { BackButton } from './Bookings';
import home from '../media/home-40px.svg';
import book from '../media/book-40px.svg';
import calendar from '../media/calendar-40px.svg';
import phone from '../media/phone-40px.svg';
import circleAdd from '../media/plus-circle-20px.svg';

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

const AppointmentsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 280px);
  gap: 24px;
`;

const ServicesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(1, 840px);
  gap: 24px;
`;

const services = [
  {
    serviceName: "Jim's Cleaning",
    address: '48 Edinburgh Road, Mooroolbark, Victoria 3138',
    phoneNumber: '131 546',
  },
  { serviceName: 'Man Oh Man' },
  { serviceName: 'Nail Lisa' },
  { serviceName: 'Bob the Builder' },
];

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
  // {
  //   bookingId: '00005',
  //   customerUsername: 'rw22448',
  //   employeeUsername: 'jeffOak',
  //   date: '24/07/2020',
  //   time: 1000,
  //   duration: 1.5,
  // },
  // {
  //   bookingId: '00006',
  //   customerUsername: 'rw22448',
  //   employeeUsername: 'jeffOak',
  //   date: '24/07/2020',
  //   time: 1000,
  //   duration: 1.5,
  // },
  // {
  //   bookingId: '00007',
  //   customerUsername: 'rw22448',
  //   employeeUsername: 'jeffOak',
  //   date: '24/07/2020',
  //   time: 1000,
  //   duration: 1.5,
  // },
  // {
  //   bookingId: '00008',
  //   customerUsername: 'rw22448',
  //   employeeUsername: 'jeffOak',
  //   date: '24/07/2020',
  //   time: 1000,
  //   duration: 1.5,
  // },
];

const Worker = ({ userId }) => {
  const fetchUserData = async (key, userId) => {
    const result = await fetch(
      `http://localhost:8080/users?username=${userId}`
    ).catch((e) => {
      console.log('Error: ', e);
    });

    return result.json();
  };

  const bookAppointment = () => {
    console.log('Booking appointment...');
    setMain(false);
    setBooking(true);
  };

  const cancelBooking = () => {
    console.log('Cancelling booking...');
    setBooking(false);
    setMain(true);
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

  // Page states for updating current view
  const [main, setMain] = useState(false);
  const [booking, setBooking] = useState(true);

  return (
    <>
      {isLoading && <div>Loading...</div>}
      {isError && <div>Error...</div>}
      {isSuccess && (
        <DashboardWrapper
          userName={data.name}
          role={role}
          actions={{ bookingLink: bookAppointment }}
        >
          <MenuBarComponent>
            <MenuIcon src={home} alt="Home icon" />
            <MenuIcon src={book} alt="Book icon" />
            <MenuIcon src={phone} alt="Phone icon" />
            <MenuIcon src={calendar} alt="Calendar icon" />
          </MenuBarComponent>
          {main && (
            <Content>
              <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <DashboardGrid>
                <DashboardModule
                  title="Upcoming appointments"
                  icon={circleAdd}
                  action={bookAppointment}
                >
                  {/* Content of Upcoming appointments DashboardModule will change depending on how many appointments for the user
                  Potentially update to use flex container for wrapping? */}
                  <AppointmentsGrid>
                    {tempBookings.map((booking) => (
                      <UpcomingAppointmentCard key={booking.bookingId}>
                        {booking}
                      </UpcomingAppointmentCard>
                    ))}
                  </AppointmentsGrid>
                </DashboardModule>
                <DashboardModule title="Service">Service</DashboardModule>
                <DashboardModule title="Service">Service</DashboardModule>
              </DashboardGrid>
            </Content>
          )}
          {booking && (
            <Content>
              <Heading>New booking</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <BackButton onClick={cancelBooking}>Back</BackButton>
              <DashboardGrid>
                <DashboardModule title="Choose a service">
                  <ServicesGrid>
                    {services.map((service) => (
                      <ServiceCard key={service.serviceName}>
                        {service}
                      </ServiceCard>
                    ))}
                  </ServicesGrid>
                </DashboardModule>
              </DashboardGrid>
            </Content>
          )}
        </DashboardWrapper>
      )}
    </>
  );
};

Worker.defaultProps = {
  userId: 'rw22448',
};

export default Worker;
