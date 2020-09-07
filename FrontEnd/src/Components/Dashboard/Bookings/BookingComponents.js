import React from 'react';
import styled from 'styled-components';
import { Title } from '../DashboardComponents';

// Components defined here are specifically used for booking appointments

const StyledServiceCard = styled.div`
  height: 106px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.25);
`;

const StyledWorkerCard = styled.div`
  height: 60px;
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
  width: 24px;
  height: 24px;
  margin: 0;
  outline: 0px;
  border-radius: 50%;
  border: 2px solid #999;
  position: relative;
  top: 2px;

  &:checked {
    border: 12px solid ${(props) => props.theme.colours.greenPrimary};
  }
`;

const StyledLabel = styled.label`
  width: 100%;
  height: 100%;
  font-weight: ${(props) => props.theme.fontWeight.semiBold};
  font-size: 24px;
`;

const StyledDateTimeInput = styled.input`
  display: block;
  border: none;
  outline: none;
  background-color: transparent;
  position: relative;
  right: 2px;
  margin-top: 4px;
  padding-top: 4px;
  font-size: 16px;
  font-family: ${(props) => props.theme.font.primary};
`;

// Used to help find component during unit testing
StyledDateTimeInput.displayName = 'StyledDateTimeInput';

const TimeFlex = styled.div`
  display: flex;
`;

const StyledHr = styled.hr`
  margin-bottom: 0;
`;

const ServiceCard = ({ service, onClick }) => {
  return (
    <StyledServiceCard>
      <CardContents onClick={onClick}>
        <TempServiceIcon></TempServiceIcon>
        <CardContentsText>
          <Title>{service.serviceName}</Title>
          <div>{service.address}</div>
          <div>{service.phoneNumber}</div>
        </CardContentsText>
      </CardContents>
    </StyledServiceCard>
  );
};

const WorkerRadioButton = ({ worker, onChange }) => {
  return (
    <StyledWorkerCard>
      <CardContents>
        <StyledRadioInput
          type="radio"
          id={worker.workerUserName}
          key={worker.workerUserName}
          label={worker.workerFullName}
          value={worker.workerUserName}
          name="selectWorker"
          onChange={() => {
            onChange(worker.workerUserName);
          }}
        />
        <CardContentsText>
          <StyledLabel htmlFor={worker.workerUserName}>
            {worker.workerFullName}
          </StyledLabel>
        </CardContentsText>
      </CardContents>
    </StyledWorkerCard>
  );
};

const DateTimeSelector = ({ label, onChange }) => {
  return (
    <DateTimeSelectorWrapper>
      <label htmlFor={label}>{label}</label>
      <StyledDateTimeInput
        type="datetime-local"
        id={label}
        name={label}
        onChange={(e) => {
          onChange(e.target.value);
        }}
      />
      <StyledHr />
    </DateTimeSelectorWrapper>
  );
};

export { ServiceCard, DateTimeSelector, WorkerRadioButton, TimeFlex };
