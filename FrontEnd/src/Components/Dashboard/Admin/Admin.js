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
  DashboardGrid,
  Button,
} from '../DashboardComponents';
import FormDetails from '../../FormDetails';
// User dashboard component for a logged in user. id of user is passed in a pro-
// ps so that we can reuse the Dashboard component. Here we can handle the logi-
// c of booking a service and such

const Admin = ({ id }) => {
  const fetchUserData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users?username=${id}`)
      .catch((error) => {
        console.log('Error fetching user data: ' + error);
      });

    return data;
  };

  // State changing functions for updating page view
  const returnHome = () => {
    setMain(true);
    setAdd(false);
  };

  const addEmployee = () => {
    setMain(false);
    setAdd(true);
  };

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

  const [userName, setUserName] = useState();
  const [role, setRole] = useState('User');
  const [date] = useState(new Date());

  // Page states for updating current view
  const [main, setMain] = useState(true);
  const [addEmpl, setAdd] = useState(false);

  return (
    <>
      {isLoading && <div>Loading...</div>}
      {isError && <div>Error...</div>}
      {isSuccess && (
        <DashboardWrapper
          userName={userName}
          role={role}
          actions={{ bookingLink: () => {} }}
        >
          <MenuBarComponent>
            <Home
              onClick={returnHome}
              className="menuIcon"
              color={theme.colours.grey.primary}
              size={theme.icons.size.medium}
            />
            <PlusCircle
              onClick={() => {}}
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
              <Button type="button" onClick={addEmployee}>
                Add An Employee
              </Button>
            </Content>
          )}
          {addEmpl && (
            <Content>
              <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
              <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
              <Button type="button" onClick={returnHome}>
                Back
              </Button>
              <DashboardGrid>
                <FormDetails />
              </DashboardGrid>
            </Content>
          )}
        </DashboardWrapper>
      )}
    </>
  );
};

Admin.defaultProps = {
  id: 'lizatawaf',
};

export default Admin;
