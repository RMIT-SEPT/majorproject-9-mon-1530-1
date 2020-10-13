import React, { useState } from 'react';
import { useQuery } from 'react-query';
import axios from 'axios';
import { Home, Phone, Calendar, PlusCircle } from 'react-feather';
import { theme } from '../../../App';
import { DashboardWrapper, MenuBarComponent } from '../Dashboard';
import {
  Heading,
  SubHeading,
  Content,
  Button,
  DashboardModule,
  Loading,
} from '../DashboardComponents';
import { WorkerList, WorkerHours } from '../Workers/WorkerComponents';
import FormDetails from '../../Form/FormDetails';
import Unauthorized from '../../Auth/Unauthorized';
import { Redirect } from 'react-router-dom';
// Admin dashboard component for a logged in admin.
const token = localStorage.getItem('token');
console.log(token);
const Admin = ({ id }) => {
  const fetchAdminData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users/username?username=${id}`, {
        headers: {
          'Authorization': `${token}`,
          'username': `${id}`
        }
      })
      .then((res) => res)
      .catch((error) => {
        console.log('admin error' + error);
        throw error;
      });

    return data;
  };

  // State changing functions for updating page view
  const clear = () => {
    setMain(false);
    setEmployee(false);
    setWorker(false);
  };

  const returnHome = () => {
    clear();
    setMain(true);
  };

  const addEmployee = () => {
    clear();
    setEmployee(true);
  };

  const { isSuccess, isLoading, isError } = useQuery(
    ['adminData', id],
    fetchAdminData,
    {
      onSuccess: (data) => {
        setUserName(data.name);
        setRole(data.userType);
        const localRole = localStorage.setItem('role', data.userType);
      },
    }
  );

  const [userName, setUserName] = useState();
  const [role, setRole] = useState();
  const [date] = useState(new Date());
  const [selectedWorker, setSelectedWorker] = useState();

  // Page states for updating current view
  const [main, setMain] = useState(true);
  const [worker, setWorker] = useState(false);
  const [employee, setEmployee] = useState(false);
  const isLoggedIn = localStorage.getItem('isAuth');

  if (localStorage.getItem('role') === 'user') {
    return <Redirect to="/user" />;
  } else {
    return (
      <>
        {isLoading && <Loading />}
        {isError && <Unauthorized />}
        {isSuccess && (
          <DashboardWrapper
            userName={userName}
            role={role}
            actions={{ bookingLink: () => { } }}
          >
            <MenuBarComponent>
              <Home
                onClick={returnHome}
                className="menuIcon"
                color={theme.colours.grey.primary}
                size={theme.icons.size.medium}
              />
              <PlusCircle
                onClick={() => { }}
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
                <DashboardModule title="Add an employee">
                  <WorkerList
                    setWorker={setWorker}
                    clear={clear}
                    setSelectedWorker={setSelectedWorker}
                  />
                  <Button type="button" onClick={addEmployee}>
                    Add An Employee
                </Button>
                </DashboardModule>
              </Content>
            )}
            {worker && (
              <Content>
                <Heading>Add worker hours</Heading>
                <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
                <Button type="button" onClick={returnHome}>
                  Back
              </Button>
                <DashboardModule title="Add worker hours">
                  <WorkerHours worker={selectedWorker} userName={id} />
                </DashboardModule>
              </Content>
            )}
            {employee && (
              <Content>
                <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
                <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
                <Button type="button" onClick={returnHome}>
                  Back
              </Button>
                <DashboardModule title="Fill employee details">
                  <FormDetails />
                </DashboardModule>
              </Content>
            )}
          </DashboardWrapper>
        )}
      </>
    );
  }
};

Admin.defaultProps = {
  id: localStorage.getItem('username'),
};

export default Admin;
