import React from 'react';
import styled from 'styled-components';
import { Button, TextField } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import { Title } from '../DashboardComponents';

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

const DateTimeSelectorWrapper = styled.span`
  margin-right: 20px;
`;

const StyledRadioInput = styled.input`
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  width: 74px;
  height: 74px;
  margin: 0;
  outline: 0px;
  border-radius: 50%;
  border: 2px solid #999;

  &:checked {
    border: 37px solid ${(props) => props.theme.colours.greenPrimary};
  }
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

const ServiceCard = ({ service, onClick }) => {
  return (
    <StyledCard>
      <CardContents onClick={onClick}>
        <TempServiceIcon></TempServiceIcon>
        <CardContentsText>
          <Title>{service.serviceName}</Title>
          <div>{service.address}</div>
          <div>{service.phoneNumber}</div>
        </CardContentsText>
      </CardContents>
    </StyledCard>
  );
};

const WorkerRadioButton = ({ worker, onChange }) => {
  return (
    <StyledCard>
      <CardContents>
        <StyledRadioInput
          type="radio"
          id={worker.workerUserName}
          key={worker.workerUserName}
          label={worker.workerFullName}
          value={worker.workerUserName}
          name="selectWorker"
          onChange={() => {
            onChange(worker);
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

const DateTimeSelector = ({ label, onChange }) => {
  return (
    <DateTimeSelectorWrapper>
      <label htmlFor={label}>{label}</label>
      <input
        type="datetime-local"
        id={label}
        name={label}
        onChange={(e) => {
          onChange(e.target.value);
        }}
      />
    </DateTimeSelectorWrapper>
  );
};

export {
  BackButton,
  ServiceCard,
  DateTimeSelector,
  SubmitButton,
  WorkerRadioButton,
};
