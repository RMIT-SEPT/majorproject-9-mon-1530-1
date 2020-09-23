import React, { useState } from 'react';
import { Button } from '../../Dashboard/DashboardComponents';
// import { DateTimeSelector } from './BookingComponents';

const days = [
  'Sunday',
  'Monday',
  'Tuesday',
  'Wednesday',
  'Thursday',
  'Friday',
  'Saturday',
];

const monthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

const month = {
  padding: '30px 0px',
  width: '100%',
  background: '#5ac490',
  textAlign: 'center',
};
const prev = {
  float: 'left',
  paddingLeft: '10px',
  paddingTop: '5px',
};

const next = {
  float: 'right',
  paddingRight: '10px',
  paddingTop: '5px',
};

const weekdays = {
  margin: '0',
  padding: '10px 0',
  backgroundColor: '#ddd',
};

const day = {
  display: 'inline-block',
  width: '13.6%',
  color: '#666',
  textAlign: 'center',
};

function getWeekEndDate(date) {
  let endDate = new Date(
    date.valueOf() + 1000 * 60 * 60 * 24 * (days.length - (date.getDay() + 1))
  );

  return endDate;
}

function getWeekStartDate(date) {
  let startDate = new Date(
    date.valueOf() - 1000 * 60 * 60 * 24 * date.getDay()
  );

  return startDate;
}

function generateDays(startDate, endDate) {
  var indents = [];

  if (startDate > endDate) {
    return <li>start date too great</li>;
  }

  if (startDate + 7 > endDate) {
    return <li>end date to short</li>;
  }

  var i = 0;
  //pre dates which would be greyed out
  /*for (i = 0; i < startDate.getDay(); i++) {
    currentDate.setDate(startDate.getDate() + (i - startDate.getDay()));
    indents.push(
      <li style={day}>{`${currentDate.getDate()} ${days[i % days.length]}`}</li>
    );
  }*/

  var needMonth = (startDate.getMonth() !== endDate.getMonth());

  //active dates
  for (i = startDate.getDay(); i <= endDate.getDay(); i++) {
    var currentDate = new Date(startDate.valueOf() + 1000 * 60 * 60 * 24 * i);

    indents.push(
      <li style={day}>{`${days[i % days.length]} ${currentDate.getDate()} ${ needMonth ? monthNames[currentDate.getMonth()] : "" }`}</li>
    );
  }
  return indents;
}

function generateTimesForDays(startDate, endDate, timeSlots) {
  var indents = [];
  var localTimeSlots = [...timeSlots];
  var j = 0;

  //this is going from left to right row by row
  //current logic goes like this
  //we loop through one row no matter what but as said row by row
  //if there has been no change (i.e. no time slot added)
  //then we break
  //if a timeslot has been added then we need to ensure not to
  //add an empty timeslot in its position otherwise it will be unaligned
  //empty timeslots are used to make sure that the time slots align
  //under their corresponding dates
  //there is a local copy of the timeSlots as we 
  //go through and pop (splice) the selected element
  while (localTimeSlots.length > 0) {
    var timeLength = localTimeSlots.length;
    var changed = false;
    for (j = 0; j < days.length; j++) {
      var insidelocalStartDate = new Date(
        startDate.valueOf() + 1000 * 60 * 60 * 24 * j
      );

      for (var i = 0; i < localTimeSlots.length; i++) {
        if (localTimeSlots.length === 0) break;

        var currentTimeSlotStartDate = new Date(
          localTimeSlots[i].startDateTime
        );
        var currentTimeSlotEndDate = new Date(localTimeSlots[i].endDateTime);

        if (
          (currentTimeSlotStartDate.getDate() === insidelocalStartDate.getDate())
          &&
          (currentTimeSlotStartDate.getMonth() === insidelocalStartDate.getMonth())
          &&
          (currentTimeSlotStartDate.getFullYear() === insidelocalStartDate.getFullYear())
        ) {
          localTimeSlots.splice(i, 1);
          changed = true;
          indents.push(
            <li style={day}>
              <Button>
                {`
                ${currentTimeSlotStartDate.toTimeString().substring(0,5)} 
                - 
                ${currentTimeSlotEndDate.toTimeString().substring(0,5)}`}
              </Button>
            </li>
          );
          break;
        }
      }
      if (timeLength === localTimeSlots.length) {
        indents.push(
          <li style={day}>
            <br />
          </li>
        );
      } else {
        timeLength = localTimeSlots.length;
      }
    }
    //if nothing has happened (no time slots added through this row, then break)
    if (!changed && timeLength === localTimeSlots.length) break;
  }

  return indents;
}

//direction is bool
//true to go forward 1 week
//false to go back 1 week

//https://www.w3schools.com/howto/howto_css_calendar.asp
//https://www.w3schools.com/howto/tryit.asp?filename=tryhow_css_calendar
const BookingView = ({ timeSlots }) => {
  const [date, setTheDate] = useState(new Date());
  const [weekStartDate, setWeekStartDate] = useState(getWeekStartDate(date));
  const [weekEndDate, setWeekEndDate] = useState(getWeekEndDate(date));

  // changeDateRange(date, currentWeekDate, currentWeekEnd);

  // function moveDate(startDate, endDate, direction) {
  //     if (direction) {
  //         startDate.setDate(startDate.getDate() + 7);
  //         endDate.setDate(endDate.getDate() + 7);
  //     } else {
  //         startDate.setDate(startDate.getDate() - 7);
  //         endDate.setDate(endDate.getDate() - 7);
  //     }

  //     console.log("button fired");
  // }

  //from today till end of the week
  // const daysToEnd = days.slice(date.getDay() - 1, days.length);

  return (
    <>
      <div style={month}>
        <ul style={{ listStyleType: 'none' }}>
          <li style={prev}>
            <Button
              onClick={() => {
                setTheDate(new Date(date.setDate(date.getDate() - 7)));
                setWeekStartDate(getWeekStartDate(date));
                setWeekEndDate(getWeekEndDate(date));
              }}
            >
              &#10094;
            </Button>
          </li>
          <li style={next}>
            <Button
              onClick={() => {
                const nextWeekDate = new Date(
                  date.valueOf() + 1000 * 60 * 60 * 24 * 7
                );
                // if (date.getDate() > nextWeekDate.getDate()) {
                //   //addd month
                //   nextWeekDate.setMonth(nextWeekDate.getMonth() + 1);
                // }
                setTheDate(nextWeekDate);
                setWeekStartDate(getWeekStartDate(nextWeekDate));
                setWeekEndDate(getWeekEndDate(nextWeekDate));
              }}
            >
              &#10095;
            </Button>
          </li>
          <li>
            {monthNames[date.getMonth()]} {date.getFullYear()}
            <br />
            <span>
               {days[weekStartDate.getDay()]} {weekStartDate.getDate()} { (weekStartDate.getMonth() != weekEndDate.getMonth()) ? monthNames[weekStartDate.getMonth()] : "" }
            </span>
            <span> - </span>
            <span>
               {days[weekEndDate.getDay()]} {weekEndDate.getDate()} { (weekStartDate.getMonth() != weekEndDate.getMonth()) ? monthNames[weekEndDate.getMonth()] : "" }
            </span>
          </li>
        </ul>
      </div>
      <ul style={weekdays}>
        {generateDays(weekStartDate, weekEndDate)}
        {generateTimesForDays(weekStartDate, weekEndDate, timeSlots)}
      </ul>
    </>
  );
};

export { BookingView };
