import React, { useState, useContext } from 'react';
import styled from 'styled-components';
import { useQuery, useMutation } from 'react-query';
import axios from 'axios';
import { User } from 'react-feather';
import { theme } from '../../../App';
import { PanelGrid, StyledGreenText, Button } from '../DashboardComponents';
import {
  DateTimeSelector,
  TimeFlex,
  DateSelector,
  TimeSelector,
} from '../Bookings/BookingComponents';
import { BrowserContext } from '../../../Contexts/BrowserContext';

const StyledWorkerItem = styled.div`
  height: 56px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.25);
  transition: color ${(props) => props.theme.transition.short};

  &:hover {
    cursor: pointer;
    text-decoration: underline;
    color: ${(props) => props.theme.colours.green.primary};
  }
`;

const Contents = styled.div`
  margin: 16px 24px;
`;

const FlexContainer = styled.div`
  display: flex;
`;

const IconContainer = styled.div`
  margin-left: auto;
  margin-right: 20px;
  align-self: center;
`;

const StyledConatiner = styled.div`
  margin-bottom: 20px;
`;

const WorkerList = ({ setWorker, clear, setSelectedWorker, id }) => {
  const userType = 'Worker';
  const token = localStorage.getItem('token');

  const [workerList, setWorkerList] = useState([]);

  const fetchWorkerList = async (key) => {
    const { data } = await axios
      .get(
        `${process.env.REACT_APP_USERS_ENDPOINT}/users/bulk?userType=${userType}`,
        {
          headers: {
            Authorization: `${token}`,
            username: `${id}`,
          },
        }
      )
      .then((res) => res)
      .catch((error) => {
        console.log('Error fetching list of workers: ' + error);
        throw error;
      });

    return data;
  };

  useQuery(['workerList'], fetchWorkerList, {
    onSuccess: (data) => {
      console.log(data);
      setWorkerList(data);
    },
  });

  return (
    <PanelGrid>
      {workerList.map((worker) => {
        return (
          <StyledWorkerItem
            onClick={() => {
              clear();
              setWorker(true);
              setSelectedWorker(worker);
            }}
            key={worker.username}
          >
            <FlexContainer>
              <Contents>
                <StyledGreenText>{worker.name}</StyledGreenText> &middot;{' '}
                {worker.address} &middot; {worker.phone}
              </Contents>
              <IconContainer>
                <User size={theme.icons.size.medium} />
              </IconContainer>
            </FlexContainer>
          </StyledWorkerItem>
        );
      })}
    </PanelGrid>
  );
};

const WorkerHours = ({ worker, userName }) => {
  const { isFirefox, isChrome } = useContext(BrowserContext);

  const token = localStorage.getItem('token');

  const [startDateTime, setStartDateTime] = useState();
  const [endDateTime, setEndDateTime] = useState();
  const [hoursSuccess, setHoursSuccess] = useState(false);

  const [startDate, setStartDate] = useState();
  const [startTime, setStartTime] = useState();
  const [endDate, setEndDate] = useState();
  const [endTime, setEndTime] = useState();

  const submitHours = async () => {
    var localStart;
    var localEnd;

    if (isFirefox) {
      localStart = startDate + 'T' + startTime;
      localEnd = endDate + 'T' + endTime;
    } else if (isChrome) {
      localStart = startDateTime;
      localEnd = endDateTime;
    }

    console.log(localStart);
    console.log(localEnd);

    const headers = {
      Authorization: `${token}`,
      username: `${userName}`,
    };

    await axios.post(
      `${process.env.REACT_APP_HOURS_ENDPOINT}/hours`,
      {
        creatorUsername: userName,
        workerUsername: worker.username,
        startDateTime: localStart,
        endDateTime: localEnd,
      },
      { headers: headers }
    );
  };

  const [mutate] = useMutation(
    async () => {
      await submitHours();
    },
    {
      onSuccess: () => {
        console.log('Hours successful!');
        setHoursSuccess(true);
      },
    }
  );

  return (
    <div>
      <StyledConatiner>
        Add hours for <StyledGreenText>{worker.name}</StyledGreenText>
      </StyledConatiner>
      <TimeFlex>
        {isChrome && (
          <>
            <DateTimeSelector label="Start time" onChange={setStartDateTime} />
            <DateTimeSelector label="End time" onChange={setEndDateTime} />
          </>
        )}
        {isFirefox && (
          <>
            <DateSelector label="Start date" onChange={setStartDate} />
            <TimeSelector label="Start time" onChange={setStartTime} />

            <DateSelector label="End date" onChange={setEndDate} />
            <TimeSelector label="End time" onChange={setEndTime} />
          </>
        )}
      </TimeFlex>
      <Button type="button" onClick={mutate}>
        Submit
      </Button>
      {hoursSuccess && <StyledConatiner>Successful!</StyledConatiner>}
    </div>
  );
};

export { WorkerList, WorkerHours };
