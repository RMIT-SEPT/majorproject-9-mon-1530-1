import React, { useState, useContext } from 'react';
import { Button } from '../../Dashboard/DashboardComponents';
import { days, monthNames, MILLISECONDS_TO_DAYS_FACTOR } from './BookingUtils';
import {
  month,
  prev,
  next,
  weekdays,
  day,
  StyledList,
} from './BookingView.styles';
import { TimeSlotView, DisabledButton } from './BookingComponents';
import { sampleReturn } from './BookingsMockData';
import { BookingContext } from '../../../Contexts/BookingContext';
import { StyledGreenText,SelectedTime } from '../DashboardComponents';

function getWeekEndDate(viewDate) {
  let endDate = new Date(
    viewDate.valueOf() +
      MILLISECONDS_TO_DAYS_FACTOR * (days.length - (viewDate.getDay() + 1))
  );

  return endDate;
}

function getWeekStartDate(viewDate) {
  let startDate = new Date(
    viewDate.valueOf() - MILLISECONDS_TO_DAYS_FACTOR * viewDate.getDay()
  );

  return startDate;
}
function generateDays(startDate, endDate) {
  var indents = [];

  if (startDate > endDate) {
    return <li>start viewDate too great</li>;
  }

  if (startDate + 7 > endDate) {
    return <li>end viewDate to short</li>;
  }

  var needMonth = startDate.getMonth() !== endDate.getMonth();

  //active dates
  for (var i = startDate.getDay(); i <= endDate.getDay(); i++) {
    var currentDate = new Date(
      startDate.valueOf() + MILLISECONDS_TO_DAYS_FACTOR * i
    );

    indents.push(
      <li key={days[i % days.length]} style={day}>{`${
        days[i % days.length]
      } ${currentDate.getDate()} ${
        needMonth ? monthNames[currentDate.getMonth()] : ''
      }`}</li>
    );
  }
  return indents;
}

function generateTimesForDays(startDate, timeSlots) {
  var indents = [];
  var dateKeys = Object.keys(timeSlots);
  var counter = 0;

  var longestDay = 0; //this is used to determine the height of the table
  for (var i = 0; i < dateKeys.length; i++) {
    var currentDayTimeSlot = timeSlots[dateKeys[i]];

    if (currentDayTimeSlot.length > longestDay)
      longestDay = currentDayTimeSlot.length;
  }

  var localTimeSlots = JSON.parse(JSON.stringify(timeSlots));

  //this loop is the height
  for (var k = 0; k < longestDay; k++) {
    //this loop is the row
    for (var j = 0; j < days.length; j++) {
      var currentSlot = localTimeSlots[dateKeys[j]];
      var insidelocalStartDate = new Date(
        startDate.valueOf() + MILLISECONDS_TO_DAYS_FACTOR * j
      );
      if (currentSlot.length > 0) {
        var currentTimeSlotStartDate = new Date(currentSlot[0].startDateTime);
        var currentTimeSlotEndDate = new Date(currentSlot[0].endDateTime);
        if (
          currentTimeSlotStartDate.getDate() ===
            insidelocalStartDate.getDate() &&
          currentTimeSlotStartDate.getMonth() ===
            insidelocalStartDate.getMonth() &&
          currentTimeSlotStartDate.getFullYear() ===
            insidelocalStartDate.getFullYear()
        ) {
          if (currentSlot[0].available) {
            indents.push(
              <li key={currentTimeSlotStartDate} style={day}>
                <TimeSlotView
                  startTime={currentTimeSlotStartDate}
                  endTime={currentTimeSlotEndDate}
                >
                  {`
                  ${currentTimeSlotStartDate.toTimeString().substring(0, 5)} 
                  - 
                  ${currentTimeSlotEndDate.toTimeString().substring(0, 5)}`}
                </TimeSlotView>
              </li>
            );
          } else {
            indents.push(
              <li key={currentTimeSlotStartDate} style={day}>
                <DisabledButton disabled>
                  {`
                  ${currentTimeSlotStartDate.toTimeString().substring(0, 5)} 
                  - 
                  ${currentTimeSlotEndDate.toTimeString().substring(0, 5)}`}
                </DisabledButton>
              </li>
            );
          }
          currentSlot.splice(0, 1);
        }
      } else {
        indents.push(
          <li key={counter} style={day}>
            <br />
          </li>
        );
        counter++;
      }
    }
  }

  return indents;
}

const BookingView = ({ timeSlots }) => {
  const { startTime, endTime } = useContext(BookingContext);

  const [viewDate, setViewDate] = useState(new Date());
  const [weekStartDate, setWeekStartDate] = useState(
    getWeekStartDate(viewDate)
  );
  const [weekEndDate, setWeekEndDate] = useState(getWeekEndDate(viewDate));

  return (
    <>
      <div style={month}>
        <StyledList>
          <li style={prev}>
            <Button
              onClick={() => {
                setViewDate(new Date(viewDate.setDate(viewDate.getDate() - 7)));
                setWeekStartDate(getWeekStartDate(viewDate));
                setWeekEndDate(getWeekEndDate(viewDate));
              }}
            >
              &#10094;
            </Button>
          </li>
          <li style={next}>
            <Button
              onClick={() => {
                const nextWeekDate = new Date(
                  viewDate.valueOf() + MILLISECONDS_TO_DAYS_FACTOR * 7
                );
                setViewDate(nextWeekDate);
                setWeekStartDate(getWeekStartDate(nextWeekDate));
                setWeekEndDate(getWeekEndDate(nextWeekDate));
              }}
            >
              &#10095;
            </Button>
          </li>
          <li>
            {monthNames[viewDate.getMonth()]} {viewDate.getFullYear()}
            <br />
            <span>
              {days[weekStartDate.getDay()]} {weekStartDate.getDate()}{' '}
              {weekStartDate.getMonth() !== weekEndDate.getMonth()
                ? monthNames[weekStartDate.getMonth()]
                : ''}
            </span>
            <span> - </span>
            <span>
              {days[weekEndDate.getDay()]} {weekEndDate.getDate()}{' '}
              {weekStartDate.getMonth() !== weekEndDate.getMonth()
                ? monthNames[weekEndDate.getMonth()]
                : ''}
            </span>
          </li>
        </StyledList>
      </div>
      <ul style={weekdays}>
        {generateDays(weekStartDate, weekEndDate)}
        {generateTimesForDays(weekStartDate, sampleReturn)}
      </ul>
      <div>
        <SelectedTime>
        Selected time:{' '}
        </SelectedTime>
        <StyledGreenText>
          {startTime && startTime.toLocaleString()} -{' '}
          {endTime && endTime.toLocaleString()}
        </StyledGreenText>
      </div>
    </>
  );
};

export { BookingView };
