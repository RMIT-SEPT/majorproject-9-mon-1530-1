import React, { useState, useContext } from 'react';
import { useQuery, useMutation } from 'react-query';
import axios from 'axios';
import { Home, Phone, Calendar, PlusCircle } from 'react-feather';
import { theme } from '../../../App';
import { DashboardWrapper, MenuBarComponent } from '../Dashboard';
import {
  DashboardModule,
  UpcomingAppointmentCard,
  Heading,
  SubHeading,
  Content,
  AppointmentsGrid,
  PanelGrid,
  Button,
  Loading,
  Error,
} from '../DashboardComponents';
import { WorkerRadioList } from '../Bookings/BookingComponents';
import { BookingContext } from '../../../Contexts/BookingContext';
import { tempBookings } from './UserMockData';
import { BookingView } from '../Bookings/BookingView';

// User dashboard component for a logged in user. id of user is passed in a pro-
// ps so that we can reuse the Dashboard component. Here we can handle the logi-
// c of booking a service and such

const User = ({ id }) => {
  const {
    setCustomerId,
    setWorkerId,
    clearBooking,
    submitBooking,
  } = useContext(BookingContext);

  const fetchUserData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users?username=${id}`)
      .then((response) => response)
      .then((res) => res)
      .catch((error) => {
        console.log('Error fetching user data: ' + error);
        throw error;
      });

    return data;
  };
  const clear = () => {
    setBooking(false);
    setWorker(false);
    setMain(false);
  };

  // State changing functions for updating page view
  const bookAppointment = () => {
    clear();
    setCustomerId(userId);
    setWorker(true);
  };

  const returnHome = () => {
    clear();
    clearBooking();
    setMain(true);
  };

  const selectBooking = () => {
    clear();
    setBooking(true);
  };

  const cancelBooking = () => {
    setBooking(false);
    setWorker(true);
  };

  // Fetch user info from back-end
  const { isSuccess, isLoading, isError } = useQuery(
    ['userData', id],
    fetchUserData,
    {
      onSuccess: (data) => {
        setUserName(data.name);
        setRole(data.userType);
        localStorage.setItem('role', data.userType);
      },
      retry: 3,
    }
  );

  // Send booking request on submit
  const [mutate] = useMutation(
    async () => {
      await submitBooking();
    },
    {
      onSuccess: () => {
        console.log('Booking successful!');
      },
    }
  );

  const [userId] = useState(id);
  const [userName, setUserName] = useState();
  const [role, setRole] = useState('User');
  const [date] = useState(new Date());

  // Page states for updating current view
  const [main, setMain] = useState(true);
  const [worker, setWorker] = useState(false);
  const [booking, setBooking] = useState(false);

  return (
    <>
      {isLoading && <Loading />}
      {isError && <Error />}
      {isSuccess && (
        <DashboardWrapper
          userName={userName}
          role={role}
          actions={{ bookingLink: bookAppointment }}
        >
          <MenuBarComponent>
            <Home
              onClick={returnHome}
              className="menuIcon"
              color={theme.colours.grey.primary}
              size={theme.icons.size.medium}
            />
            <PlusCircle
              onClick={bookAppointment}
              className="menuIcon"
              color={theme.colours.grey.primary}
              size={theme.icons.size.medium}
            />
            <Phone
              className="menuIcon"
              color={theme.colours.grey.primary}
              size={theme.icons.size.medium}
            />
            <Calendar
              className="menuIcon"
              color={theme.colours.grey.primary}
              size={theme.icons.size.medium}
            />
          </MenuBarComponent>
          {main && (
            <Content>
              <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <DashboardModule title="Upcoming appointments">
                {/* Content of Upcoming appointments DashboardModule will change depending on how many appointments for the user
                  Potentially update to use flex container for wrapping? */}
                <AppointmentsGrid>
                  {tempBookings.map((booking) => (
                    <UpcomingAppointmentCard
                      key={booking.bookingId}
                      booking={booking}
                    />
                  ))}
                </AppointmentsGrid>
              </DashboardModule>
            </Content>
          )}
          {worker && (
            <Content>
              <Heading>New booking</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <Button type="button" onClick={returnHome}>
                Back
              </Button>
              <DashboardModule title="Choose a worker">
                <PanelGrid>
                  <WorkerRadioList
                    selectBooking={selectBooking}
                    setWorkerId={setWorkerId}
                  />
                </PanelGrid>
              </DashboardModule>
            </Content>
          )}
          {booking && (
            <Content>
              <Heading>New booking</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <Button type="button" onClick={cancelBooking}>
                Back
              </Button>
              <DashboardModule title="Availability">
                <BookingView />
              </DashboardModule>

              <Button type="button" onClick={mutate}>
                Submit
              </Button>
            </Content>
          )}
        </DashboardWrapper>
      )}
    </>
  );
};

User.defaultProps = {
  id: localStorage.getItem('username'),
};

export default User;
