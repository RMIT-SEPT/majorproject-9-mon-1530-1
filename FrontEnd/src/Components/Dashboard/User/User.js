import React, { useState, useContext } from 'react';
import { useQuery, useMutation } from 'react-query';
import axios from 'axios';
import { Home, Phone, Calendar, PlusCircle } from 'react-feather';
import { theme } from '../../../App';
import { DashboardWrapper, MenuBarComponent } from '../Dashboard';
import {
  DashboardModule,
  Heading,
  SubHeading,
  Content,
  AppointmentsGrid,
  PanelGrid,
  Button,
  Loading,
  Error,
  BookingsList,
} from '../DashboardComponents';
import { WorkerRadioList } from '../Bookings/BookingComponents';
import { BookingContext } from '../../../Contexts/BookingContext';
import { BookingView } from '../Bookings/BookingView';
import Unauthorized from '../../Auth/Unauthorized';
import { Redirect } from 'react-router-dom';
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
  const token = localStorage.getItem('token');
  const fetchUserData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users/username?username=${id}`, {
        headers: {
          'Authorization': `${token}`,
          'username': `${id}`
        }
      })
      .then((response) => response)
      .then((res) => res)
      .catch((error) => {
        console.log('admin error' + error);
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

  if (localStorage.getItem('role') === 'admin') {
    return <Redirect to="/admin" />;
  } else {
    return (
      <>
        {isLoading && <Loading />}
        {isError && <Unauthorized />}
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
                {}
                <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
                <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
                <DashboardModule title="Upcoming appointments">
                  {/* Content of Upcoming appointments DashboardModule will change depending on how many appointments for the user
                  Potentially update to use flex container for wrapping? */}
                  <AppointmentsGrid>
                    <BookingsList id={userId} />
                  </AppointmentsGrid>
                  <Button type="button" onClick={bookAppointment}>
                    Book
                </Button>
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
    )
  };
};

User.defaultProps = {
  id: localStorage.getItem('username'),
};

export default User;
