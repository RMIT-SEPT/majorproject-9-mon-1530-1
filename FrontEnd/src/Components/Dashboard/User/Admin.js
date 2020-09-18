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
    DashboardGrid,
    PanelGrid,
    Button,
} from '../DashboardComponents';
import {
    ServiceCard,
    DateTimeSelector,
    WorkerRadioButton,
    TimeFlex,
} from '../Bookings/BookingComponents';
import { BrowserContext } from '../../../Contexts/BrowserContext';
import { BookingContext } from '../../../Contexts/BookingContext';
import FormDetails from '../../FormDetails';
import { Grid } from '@material-ui/core';
// User dashboard component for a logged in user. id of user is passed in a pro-
// ps so that we can reuse the Dashboard component. Here we can handle the logi-
// c of booking a service and such

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
        workerUserName: 'lizaLeLemon',
        workerFullName: 'liza Lim Le',
    },
];


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
    const bookAppointment = () => {
        clearBooking();
        setCustomerId(userId);
        setMain(false);
        setService(false);
        setBooking(true);
    };

    const returnHome = () => {
        clearBooking();
        setBooking(false);
        setService(false);
        setMain(true);
    };

    const cancelBooking = () => {
        clearBooking();
        setBooking(false);
        setMain(true);
    };

    const selectService = () => {
        setBooking(false);
        setService(true);
    };

    const cancelService = () => {
        clearBooking();
        setService(false);
        setMain(true);
    };
    const addEmployee = () => {
        clearBooking();
        setCustomerId(userId);
        setMain(false);
        setAdd(true);
        setService(false);
        setBooking(false);
    };
    const goBack = () => {
        setBooking(false);
        setMain(true);
        setAdd(false);
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

    const { isFirefox, isChrome } = useContext(BrowserContext);
    const {
        setCustomerId,
        setWorkerId,
        setStartTime,
        setEndTime,
        clearBooking,
        submitBooking,
    } = useContext(BookingContext);

    const [userId] = useState(id);
    const [userName, setUserName] = useState();
    const [role, setRole] = useState('User');
    const [date] = useState(new Date());

    // Page states for updating current view
    const [main, setMain] = useState(true);
    const [booking, setBooking] = useState(false);
    const [addEmpl, setAdd] = useState(false);
    const [service, setService] = useState(false);

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
                            <DashboardGrid>
                                <Button type="button" onClick={addEmployee}>
                                    Add An Employee
              </Button>
                            </DashboardGrid>
                        </Content>
                    )}
                    {addEmpl && (
                        <Content>
                            <Heading>Welcome back, {userName.split(' ')[0]}!</Heading>
                            <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
                            <DashboardGrid>
                                <Grid>
                                    <Grid item xs={12}>
                                        <Button type="button" onClick={goBack}>
                                            Back
              </Button>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <FormDetails />
                                    </Grid>
                                </Grid>

                            </DashboardGrid>
                        </Content>
                    )}
                    {booking && (
                        <Content>
                            <Heading>New booking</Heading>
                            <SubHeading>Today is {date.toLocaleDateString()}</SubHeading>
                            <Button type="button" onClick={cancelBooking}>
                                Back
              </Button>
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
                                <Button type="button" onClick={cancelService}>
                                    Back
                </Button>
                                <DashboardGrid>
                                    <DashboardModule title="Choose a worker">
                                        <PanelGrid>
                                            {workers.map((worker) => (
                                                <WorkerRadioButton
                                                    type="radio"
                                                    worker={worker}
                                                    key={worker.workerUserName}
                                                    name="selectWorker"
                                                    onChange={setWorkerId}
                                                ></WorkerRadioButton>
                                            ))}
                                        </PanelGrid>
                                    </DashboardModule>
                                </DashboardGrid>
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
                                <Button type="button" onClick={mutate}>
                                    Submit
                </Button>
                            </form>
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
