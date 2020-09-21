import React, { useState } from 'react';
import styled from 'styled-components';
import { useQuery, useMutation } from 'react-query';
import axios from 'axios';
import { User } from 'react-feather';
import { theme } from '../../../App';
import { PanelGrid, StyledGreenText, Button } from '../DashboardComponents';
import { DateTimeSelector, TimeFlex } from '../Bookings/BookingComponents';

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

const WorkerList = ({ setWorker, clear, setSelectedWorker }) => {
  const userType = 'Worker';

  const [workerList, setWorkerList] = useState([]);

  const fetchWorkerList = async (key) => {
    const { data } = await axios
      .get(`http://localhost:8083/users/bulk?userType=${userType}`)
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
  const [startDateTime, setStartDateTime] = useState();
  const [endDateTime, setEndDateTime] = useState();
  const [hoursSuccess, setHoursSuccess] = useState(false);

  const submitHours = async () => {
    await axios.post('http://localhost:8082/hours', {
      creatorUsername: userName,
      workerUsername: worker.name,
      startDateTime,
      endDateTime,
    });
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
        <>
          <DateTimeSelector
            label="Start time"
            onChange={setStartDateTime}
          ></DateTimeSelector>
          <DateTimeSelector
            label="End time"
            onChange={setEndDateTime}
          ></DateTimeSelector>
        </>
      </TimeFlex>
      <Button type="button" onClick={mutate}>
        Submit
      </Button>
      {hoursSuccess && <StyledConatiner>Successful!</StyledConatiner>}
    </div>
  );
};

export { WorkerList, WorkerHours };
