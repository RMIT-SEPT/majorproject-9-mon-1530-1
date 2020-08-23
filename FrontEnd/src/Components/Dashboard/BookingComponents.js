import React from 'react';
import styled from 'styled-components';
import { Button, TextField } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import { Title } from './DashboardComponents';
import { setBookingSelectedWorker } from './Booking';

const StyledServiceCard = styled.div`
  width: 100%;
  height: 106px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.25);
`;

const ServiceCardContents = styled.div`
  display: flex;
  margin: 16px 24px;
`;

const TempServiceIcon = styled.div`
  width: 74px;
  height: 74px;
  background-color: ${(props) => props.theme.colours.greenPrimary};
  border-radius: 37px;
`;

const ServiceCardContentsText = styled.div`
  flex: none;
  margin-left: 20px;
`;

const TimeSelectorWrapper = styled.span`
  margin-right: 20px;
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
    <StyledServiceCard onClick={onClick}>
      <ServiceCardContents>
        <TempServiceIcon></TempServiceIcon>
        <ServiceCardContentsText>
          <Title>{children.serviceName}</Title>
          <div>{children.address}</div>
          <div>{children.phoneNumber}</div>
        </ServiceCardContentsText>
      </ServiceCardContents>
    </StyledServiceCard>
  );
};

const WorkerCard = ({ children, worker }) => {
  return (
    <StyledServiceCard
      onClick={() => {
        setBookingSelectedWorker(worker);
      }}
    >
      <ServiceCardContents>
        <TempServiceIcon></TempServiceIcon>
        <ServiceCardContentsText>
          <Title>{children.workerFullName}</Title>
        </ServiceCardContentsText>
      </ServiceCardContents>
    </StyledServiceCard>
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

export { BackButton, ServiceCard, WorkerCard, TimeSelector, SubmitButton };
