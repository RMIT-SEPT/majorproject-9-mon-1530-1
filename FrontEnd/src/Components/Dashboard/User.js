import React, { useState } from 'react';
import { useQuery } from 'react-query';
import { DashboardWrapper, MenuBarComponent, MenuIcon } from './Dashboard';
import {
  DashboardModule,
  UpcomingAppointmentCard,
  Heading,
  SubHeading,
  Content,
  DashboardGrid,
  AppointmentsGrid,
  PanelGrid,
} from './DashboardComponents';
import {
  BackButton,
  ServiceCard,
  WorkerCard,
  TimeSelector,
  SubmitButton,
} from './BookingComponents';
import {
  setBookingSelectedWorker,
  setBookingStartTime,
  setBookingEndTime,
} from './Booking';
import home from '../../media/home-40px.svg';
import book from '../../media/book-40px.svg';
import calendar from '../../media/calendar-40px.svg';
import phone from '../../media/phone-40px.svg';
import circleAdd from '../../media/plus-circle-20px.svg';

const services = [
  {
    serviceName: "Jim's Cleaning",
    address: '48 Edinburgh Road, Mooroolbark, Victoria 3138',
    phoneNumber: '131 546',
  },
  {
    serviceName: 'Man Oh Man',
    address: '137 Chapel Street Windsor, VIC 3181 Melbourne',
    phoneNumber: '03 9530 2393',
  },

  { serviceName: 'Nail Lisa' },
  { serviceName: 'Bob the Builder' },
];

const workers = [
  {
    workerUserName: 'kath123',
    workerFullName: 'Kathreen McDonald',
  },
  {
    workerUserName: 'markTheMark',
    workerFullName: 'Mark Falley',
  },
  {
    workerUserName: 'realSarahX',
    workerFullName: 'Sarah Mickey',
  },
  {
    workerUserName: 'JohnLeLemon',
    workerFullName: 'John Lim Le',
  },
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

const handleSelectWorker = (worker) => {
  setBookingSelectedWorker(worker.workerUserName);
};

const User = ({ userId }) => {
  const fetchUserData = async (key, userId) => {
    const result = await fetch(
      `http://localhost:8080/users?username=${userId}`
    ).catch((e) => {
      console.log('Error: ', e);
    });

    return result.json();
  };

  const fetchOfflineData = (key, userId) => {
    return {
      name: 'Richard Offline',
      userType: 'Offline',
    };
  };

  // State changing functions for updating page view
  const bookAppointment = () => {
    setMain(false);
    setService(false);
    setBooking(true);
  };

  const cancelBooking = () => {
    setBooking(false);
    setMain(true);
  };

  const selectService = () => {
    setBooking(false);
    setService(true);
  };

  const cancelService = () => {
    setService(false);
    setMain(true);
  };

  const { data, isSuccess, isLoading, isError } = useQuery(
    ['userData', userId],
    fetchOfflineData,
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
  const [booking, setBooking] = useState(false);
  const [service, setService] = useState(true);

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
              <form>
                <DashboardGrid>
                  <DashboardModule title="Choose a service">
                    <PanelGrid>
                      {services.map((service) => (
                        <ServiceCard
                          key={service.serviceName}
                          onClick={selectService}
                        >
                          {service}
                        </ServiceCard>
                      ))}
                    </PanelGrid>
                  </DashboardModule>
                </DashboardGrid>
              </form>
            </Content>
          )}
          {service && (
            <Content>
              <Heading>New booking</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <form>
                <BackButton onClick={cancelService}>Back</BackButton>
                <DashboardGrid>
                  <DashboardModule title="Choose a worker">
                    <PanelGrid>
                      {workers.map((worker) => (
                        <WorkerCard
                          key={worker.workerUserName}
                          worker={worker}
                          onClick={() => {
                            handleSelectWorker(worker);
                          }}
                        >
                          {worker}
                        </WorkerCard>
                      ))}
                    </PanelGrid>
                  </DashboardModule>
                </DashboardGrid>
                <DashboardModule title="Select times">
                  <TimeSelector
                    label="Start time"
                    onChange={setBookingStartTime}
                  ></TimeSelector>
                  <TimeSelector
                    label="End time"
                    onChange={setBookingEndTime}
                  ></TimeSelector>
                </DashboardModule>
                <SubmitButton>Submit</SubmitButton>
              </form>
            </Content>
          )}
        </DashboardWrapper>
      )}
    </>
  );
};

User.defaultProps = {
  userId: 'rw22448',
};

export default User;
