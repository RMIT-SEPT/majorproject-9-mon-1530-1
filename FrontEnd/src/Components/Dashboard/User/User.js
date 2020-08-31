import React, { useState, useContext } from 'react';
import { useQuery, useMutation } from 'react-query';
import { DashboardWrapper, MenuBarComponent, MenuIcon } from '../Dashboard';
import {
  DashboardModule,
  UpcomingAppointmentCard,
  Heading,
  SubHeading,
  Content,
  DashboardGrid,
  AppointmentsGrid,
  PanelGrid,
} from '../DashboardComponents';
import {
  BackButton,
  ServiceCard,
  DateTimeSelector,
  SubmitButton,
  WorkerRadioButton,
} from '../Bookings/BookingComponents';
import {
  setBookingSelectedWorker,
  setBookingStartTime,
  setBookingEndTime,
  submitBooking,
} from '../Bookings/Booking';
import home from '../../../media/home-40px.svg';
import book from '../../../media/book-40px.svg';
import calendar from '../../../media/calendar-40px.svg';
import phone from '../../../media/phone-40px.svg';
import circleAdd from '../../../media/plus-circle-20px.svg';
import { BrowserContext } from '../../../Contexts/BrowserContext';

const axios = require('axios');

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

  {
    serviceName: 'Nail Lisa',
    address: 'No address available',
    phoneNumber: '03 9561 2313',
  },
  {
    serviceName: 'Bob the Builder',
    address: 'Channel 3, ABC, Australia',
    phoneNumber: '123 456',
  },
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
    workerUsername: 'jeffOak',
    customerUsername: 'rw22448',
    startDateTime: '2020-08-29T12:00',
    endDateTime: '2020-08-29T13:00',
  },
  {
    workerUsername: 'jeffOak',
    customerUsername: 'anthony',
    startDateTime: '2020-08-31T14:00',
    endDateTime: '2020-08-31T16:00',
  },
];

const User = ({ id }) => {
  const fetchUserData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users?username=${id}`)
      .catch((error) => {
        console.log('Error fetching user data: ' + error);
      });

    return data;
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
    ['userData', id],
    fetchUserData,
    {
      onSuccess: (data) => {
        setUserName(data.name);
        setRole(data.userType);
      },
    }
  );
  const [mutate] = useMutation(
    async () => {
      await submitBooking(userId);
    },
    {
      onSuccess: () => {
        console.log('Booking successful!');
      },
    }
  );

  const browserContext = useContext(BrowserContext);

  const [userId] = useState(id);
  const [userName, setUserName] = useState();
  const [role, setRole] = useState('User');
  const [date] = useState(new Date());

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
                      <UpcomingAppointmentCard key={booking.customerUsername}>
                        {/* TODO: Potentially provide a bookingId to use as key for uniqueness? */}
                        {booking}
                      </UpcomingAppointmentCard>
                    ))}
                  </AppointmentsGrid>
                </DashboardModule>
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
                          service={service}
                        ></ServiceCard>
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
                        <WorkerRadioButton
                          type="radio"
                          worker={worker}
                          key={worker.workerUserName}
                          name="selectWorker"
                          onChange={setBookingSelectedWorker}
                        ></WorkerRadioButton>
                      ))}
                    </PanelGrid>
                  </DashboardModule>
                </DashboardGrid>
                <DashboardModule title="Select times">
                  {browserContext.isChrome && (
                    <>
                      <DateTimeSelector
                        label="Start time"
                        onChange={setBookingStartTime}
                      ></DateTimeSelector>
                      <DateTimeSelector
                        label="End time"
                        onChange={setBookingEndTime}
                      ></DateTimeSelector>
                    </>
                  )}
                  {browserContext.isFirefox && <div>Firefox...</div>}
                </DashboardModule>
                <SubmitButton onClick={mutate}>Submit</SubmitButton>
              </form>
            </Content>
          )}
        </DashboardWrapper>
      )}
    </>
  );
};

User.defaultProps = {
  id: 'jeffOak',
};

export default User;
