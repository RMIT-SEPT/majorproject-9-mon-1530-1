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
  BookingGrid,
  PanelGrid,
  Button,
} from '../DashboardComponents';
import {
  ServiceCard,
  DateTimeSelector,
  WorkerRadioButton,
  TimeFlex,
  AvailabilityView,
} from '../Bookings/BookingComponents';
import { BrowserContext } from '../../../Contexts/BrowserContext';
import { BookingContext } from '../../../Contexts/BookingContext';
import { tempServices, tempWorkers, tempBookings } from './UserMockData';
import { BookingView } from '../Bookings/BookingView';
// User dashboard component for a logged in user. id of user is passed in a pro-
// ps so that we can reuse the Dashboard component. Here we can handle the logi-
// c of booking a service and such

//function which is used to add 0 infront of numbers if they're < 10
function addZero(number) {
  if (number < 10) {
    return ("0" + number);
  }
  return number;
}

const User = ({ id }) => {
  const { isFirefox, isChrome } = useContext(BrowserContext);
  const {
    setCustomerId,
    setWorkerId,
    setStartTime,
    setEndTime,
    clearBooking,
    submitBooking,
    workerId,
  } = useContext(BookingContext);

  const fetchUserData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users?username=${id}`)
      .catch((error) => {
        console.log('Error fetching user data: ' + error);
      });

    return data;
  };

  const clear = () => {
    setBooking(false);
    setService(false);
    setWorker(false);
    setMain(false);
  };

  // State changing functions for updating page view
  const bookAppointment = () => {
    clear();
    setCustomerId(userId);
    setService(true);
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

  const selectWorker = () => {
    clear();
    setWorker(true);
  };

  const cancelWorker = () => {
    setWorker(false);
    setService(true);
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
      },
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
  const [service, setService] = useState(false);
  const [booking, setBooking] = useState(false);
  const [worker, setWorker] = useState(false);

  return (
    <>
      {isLoading && <div>Loading...</div>}
      {isError && <div>Error...</div>}
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
          {service && (
            <Content>
              <Heading>New booking</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <BookingView
                timeSlots={[
                  {
                    "startDateTime": (`${date.getFullYear()}-${addZero(date.getMonth()+1)}-${addZero(date.getDate())}T${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`),
                    "endDateTime": (`${date.getFullYear()}-${addZero(date.getMonth()+1)}-${addZero(date.getDate())}T${date.getHours()+1}:${date.getMinutes()}:${date.getSeconds()}`)
                  },
                  {
                    "startDateTime": (`${date.getFullYear()}-${addZero(date.getMonth()+1)}-${addZero(date.getDate())}T${date.getHours()+2}:${date.getMinutes()}:${date.getSeconds()}`),
                    "endDateTime": (`${date.getFullYear()}-${addZero(date.getMonth()+1)}-${addZero(date.getDate())}T${date.getHours()+3}:${date.getMinutes()}:${date.getSeconds()}`)
                  }
                ]}
              />
              <Button type="button" onClick={returnHome}>
                Back
              </Button>
              <DashboardModule title="Choose a service">
                <PanelGrid>
                  {tempServices.map((service) => (
                    <ServiceCard
                      key={service.serviceName}
                      onClick={selectWorker}
                      service={service}
                    ></ServiceCard>
                  ))}
                </PanelGrid>
              </DashboardModule>
            </Content>
          )}
          {worker && (
            <Content>
              <Heading>New booking</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <Button type="button" onClick={cancelWorker}>
                Back
              </Button>
              <DashboardModule title="Choose a worker">
                <PanelGrid>
                  {tempWorkers.map((worker) => (
                    <WorkerRadioButton
                      type="radio"
                      worker={worker}
                      key={worker.workerUserName}
                      name="selectWorker"
                      onChange={setWorkerId}
                      onClick={selectBooking}
                    ></WorkerRadioButton>
                  ))}
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
              <BookingGrid>
                <DashboardModule title="Availability">
                  <AvailabilityView workerId={workerId} />
                </DashboardModule>

                <DashboardModule title="Select times">
                  <TimeFlex>
                    {isChrome && (
                      <>
                        <DateTimeSelector
                          label="Start time"
                          onChange={setStartTime}
                        ></DateTimeSelector>
                        <DateTimeSelector
                          label="End time"
                          onChange={setEndTime}
                        ></DateTimeSelector>
                      </>
                    )}
                    {isFirefox && <div>Firefox...</div>}
                  </TimeFlex>
                </DashboardModule>
              </BookingGrid>
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
  id: 's1@gmail.com',
};

export default User;
