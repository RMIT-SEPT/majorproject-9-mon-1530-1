import React, { useState, useContext } from 'react';
import { useQuery } from 'react-query';
import axios from 'axios';
import { theme } from '../../../App';
import { Home, Phone, Calendar, PlusCircle } from 'react-feather';
import { DashboardWrapper, MenuBarComponent } from '../Dashboard';
import {
    Heading,
    SubHeading,
    Content
  } from '../DashboardComponents';

  
  const Admin = ({ id }) => {

    const fetchUserData = async (key, id) => {
        const { data } = await axios
        .get(`http://localhost:8083/users?username=${id}`)
        .catch((error) => {
            console.log('Error fetching user data: ' + error);
        });
        
        return data;
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

    const [date] = useState(new Date());
    const [role, setRole] = useState('User');
    const [userName, setUserName] = useState();

    return (
        <>
            {isLoading && <div>Loading...</div>}
            {isError && <div>Error...</div>}
            {isSuccess && (
                <DashboardWrapper
                    userName={userName}
                    role={role}
                    actions=""
                >
                    <MenuBarComponent>
                        <Home
                            //onClick={returnHome}
                            className="menuIcon"
                            color={theme.colours.grey.primary}
                            size={theme.icons.size.medium}
                        />
                        <PlusCircle
                            //onClick={bookAppointment}
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
                    <Content>
                        <Heading>Welcome back, {userName}!</Heading>
                        <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
                        //this would be where the main decor would go under
                    </Content>
                </DashboardWrapper>
            )}
        </>
    );
}

Admin.defaultProps = {
    id: 's1@gmail.com',
};

export default Admin;
