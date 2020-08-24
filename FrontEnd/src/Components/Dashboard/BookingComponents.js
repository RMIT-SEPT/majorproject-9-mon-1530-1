import React from 'react';
import styled from 'styled-components';
import { Button, TextField } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import { Title } from './DashboardComponents';
import { setBookingSelectedWorker } from './Booking';

const StyledCard = styled.div`
  height: 106px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.25);
`;

const CardContents = styled.div`
  display: flex;
  margin: 16px 24px;
  transition: color ${(props) => props.theme.transition.short};

  &:hover {
    cursor: pointer;
    text-decoration: underline;
    color: ${(props) => props.theme.colours.greenPrimary};
  }
`;

const TempServiceIcon = styled.div`
  width: 74px;
  height: 74px;
  background-color: ${(props) => props.theme.colours.greenPrimary};
  border-radius: 37px;
`;

const CardContentsText = styled.div`
  display: flex;
  flex-direction: column;
  position: relative;
  bottom: 2px;
  flex: none;
  margin-left: 20px;
  width: calc(100% - 74px - 24px);
`;

const TimeSelectorWrapper = styled.span`
  margin-right: 20px;
`;

const StyledRadioInput = styled.input`
  width: 74px;
  height: 74px;
  margin: 0;
`;

const StyledLabel = styled.label`
  width: 100%;
  height: 100%;
  font-weight: 550;
  font-size: 24px;
`;

const BackButton = withStyles((theme) => ({
  root: {
    margin: '24px 0px 0px 0px',
    color: theme.palette.getContrastText('#000000'),
    backgroundColor: '#5ac490',

    '&:hover': {
      backgroundColor: '#369668',
    },
  },
}))(Button);

const SubmitButton = withStyles((theme) => ({
  root: {
    margin: '24px 0px 0px 0px',
    color: theme.palette.getContrastText('#000000'),
    backgroundColor: '#5ac490',

    '&:hover': {
      backgroundColor: '#369668',
    },
  },
}))(Button);

const StyledDateTimePicker = withStyles((theme) => ({
  root: {
    color: theme.palette.getContrastText('#000000'),
  },
}))(TextField);

const ServiceCard = ({ children, onClick }) => {
  return (
    <StyledCard>
      <CardContents onClick={onClick}>
        <TempServiceIcon></TempServiceIcon>
        <CardContentsText>
          <Title>{children.serviceName}</Title>
          <div>{children.address}</div>
          <div>{children.phoneNumber}</div>
        </CardContentsText>
      </CardContents>
    </StyledCard>
  );
};

const WorkerRadioButton = ({ worker }) => {
  return (
    <StyledCard>
      <CardContents>
        <StyledRadioInput
          type="radio"
          id={worker.workerUserName}
          key={worker.workerUserName}
          label={worker.workerFullName}
          value={worker.workerUser}
          name="selectWorker"
          onChange={() => {
            setBookingSelectedWorker(worker);
          }}
        />
        <CardContentsText>
          <StyledLabel htmlFor={worker.workerUserName}>
            {worker.workerFullName}
          </StyledLabel>
        </CardContentsText>
      </CardContents>
    </StyledCard>
  );
};

const TimeSelector = ({ label, onChange }) => {
  return (
    <TimeSelectorWrapper>
      <StyledDateTimePicker
        id="datetime-local"
        label={label}
        type="datetime-local"
        InputLabelProps={{
          shrink: true,
        }}
        onChange={(e) => {
          onChange(e.target.value);
        }}
      />
    </TimeSelectorWrapper>
  );
};

export {
  BackButton,
  ServiceCard,
  TimeSelector,
  SubmitButton,
  WorkerRadioButton,
};
