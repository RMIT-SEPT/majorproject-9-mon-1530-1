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
  var dateKeys = Object.keys(timeSlots);
  
  var longestDay = 0; //this is used to determine the height of the table
  for (var i = 0; i < dateKeys.length; i++)
  {
    var currentDayTimeSlot = timeSlots[dateKeys[i]];
    
    if (currentDayTimeSlot.length > longestDay)
      longestDay = currentDayTimeSlot.length;
  }
  
  var localTimeSlots = JSON.parse(JSON.stringify(timeSlots));  

  //this loop is the height
  for (var i = 0; i < longestDay; i++)
  {
    //this loop is the row
    for (var j = 0; j < days.length; j++) {
      var currentDayTimeSlot = localTimeSlots[dateKeys[j]];
      var insidelocalStartDate = new Date(
        startDate.valueOf() + 1000 * 60 * 60 * 24 * j
      );
      if (currentDayTimeSlot.length > 0) {
        var currentTimeSlotStartDate = new Date(
          currentDayTimeSlot[0].startDateTime
        );
        var currentTimeSlotEndDate = new Date(currentDayTimeSlot[0].endDateTime);
        if (
          (currentTimeSlotStartDate.getDate() === insidelocalStartDate.getDate())
          &&
          (currentTimeSlotStartDate.getMonth() === insidelocalStartDate.getMonth())
          &&
          (currentTimeSlotStartDate.getFullYear() === insidelocalStartDate.getFullYear())
        ) {
          if (currentDayTimeSlot[0].available)
          {
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
          } else {
            indents.push(
              <li style={day}>
                <Button style={{backgroundColor : "#ffffff0a", color : "#0000003a"}}>
                  {`
                  ${currentTimeSlotStartDate.toTimeString().substring(0,5)} 
                  - 
                  ${currentTimeSlotEndDate.toTimeString().substring(0,5)}`}
                </Button>
              </li>
            );}
          currentDayTimeSlot.splice(0, 1);
        }
      } else {
        indents.push(
          <li style={day}>
            <br />
          </li>
        );
      }
    }
  }

  return indents;
}

const BookingView = ({ timeSlots }) => {
  const [date, setTheDate] = useState(new Date());
  const [weekStartDate, setWeekStartDate] = useState(getWeekStartDate(date));
  const [weekEndDate, setWeekEndDate] = useState(getWeekEndDate(date));

  //sample return of what should come from the get endpoint
  const sampleReturn = {
    "27/09/2020": {},
    "28/09/2020": {},
    "29/09/2020": {},
    "30/09/2020": [
        {
            "startDateTime": "2020-09-30T09:00",
            "endDateTime": "2020-09-30T10:00",
            "available": false
        },
        {
            "startDateTime": "2020-09-30T10:00",
            "endDateTime": "2020-09-30T11:00",
            "available": false
        },
        {
            "startDateTime": "2020-09-30T11:00",
            "endDateTime": "2020-09-30T12:00",
            "available": false
        },
        {
            "startDateTime": "2020-09-30T12:00",
            "endDateTime": "2020-09-30T13:00",
            "available": true
        },
        {
            "startDateTime": "2020-09-30T13:00",
            "endDateTime": "2020-09-30T14:00",
            "available": true
        },
        {
            "startDateTime": "2020-09-30T14:00",
            "endDateTime": "2020-09-30T15:00",
            "available": false
        },
        {
            "startDateTime": "2020-09-30T15:00",
            "endDateTime": "2020-09-30T16:00",
            "available": false
        },
        {
            "startDateTime": "2020-09-30T16:00",
            "endDateTime": "2020-09-30T17:00",
            "available": false
        },
        {
            "startDateTime": "2020-09-30T17:00",
            "endDateTime": "2020-09-30T18:00",
            "available": false
        }
    ],
    "01/10/2020": [
        {
            "startDateTime": "2020-10-01T17:00",
            "endDateTime": "2020-10-01T18:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-01T18:00",
            "endDateTime": "2020-10-01T19:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-01T19:00",
            "endDateTime": "2020-10-01T20:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-01T20:00",
            "endDateTime": "2020-10-01T21:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-01T21:00",
            "endDateTime": "2020-10-01T22:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-01T22:00",
            "endDateTime": "2020-10-01T23:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-01T23:00",
            "endDateTime": "2020-10-01T24:00",
            "available": false
        }
    ],
    "02/10/2020": [
        {
            "startDateTime": "2020-10-02T09:00",
            "endDateTime": "2020-10-02T10:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T10:00",
            "endDateTime": "2020-10-02T11:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T11:00",
            "endDateTime": "2020-10-02T12:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T12:00",
            "endDateTime": "2020-10-02T13:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T13:00",
            "endDateTime": "2020-10-02T14:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T14:00",
            "endDateTime": "2020-10-02T15:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T15:00",
            "endDateTime": "2020-10-02T16:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T16:00",
            "endDateTime": "2020-10-02T17:00",
            "available": false
        },
        {
            "startDateTime": "2020-10-02T17:00",
            "endDateTime": "2020-10-02T18:00",
            "available": false
        }
    ],
    "03/10/2020": [
        {
            "startDateTime": "2020-10-03T00:00",
            "endDateTime": "2020-10-03T01:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T01:00",
            "endDateTime": "2020-10-03T02:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T02:00",
            "endDateTime": "2020-10-03T02:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T02:00",
            "endDateTime": "2020-10-03T03:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T03:00",
            "endDateTime": "2020-10-03T04:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T04:00",
            "endDateTime": "2020-10-03T05:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T05:00",
            "endDateTime": "2020-10-03T06:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T06:00",
            "endDateTime": "2020-10-03T07:00",
            "available": true
        },
        {
            "startDateTime": "2020-10-03T07:00",
            "endDateTime": "2020-10-03T08:00",
            "available": true
        }
    ]
  };

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
               {days[weekStartDate.getDay()]} {weekStartDate.getDate()} { (weekStartDate.getMonth() !== weekEndDate.getMonth()) ? monthNames[weekStartDate.getMonth()] : "" }
            </span>
            <span> - </span>
            <span>
               {days[weekEndDate.getDay()]} {weekEndDate.getDate()} { (weekStartDate.getMonth() !== weekEndDate.getMonth()) ? monthNames[weekEndDate.getMonth()] : "" }
            </span>
          </li>
        </ul>
      </div>
      <ul style={weekdays}>
        {generateDays(weekStartDate, weekEndDate)}
        {generateTimesForDays(weekStartDate, weekEndDate, sampleReturn)}
      </ul>
    </>
  );
};

export { BookingView };
