import React, { useState } from 'react';
import { Button } from '../../Dashboard/DashboardComponents';
import { DateTimeSelector } from './BookingComponents';


const days = [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday"
];

const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
];
    
const month = {
    padding: "30px 0px",
    width: "100%",
    background: "#5ac490",
    textAlign: "center"
};
const prev = {
    float: "left",
    paddingLeft: "10px",
    paddingTop: "5px"
}
  
const next = {
    float: "right",
    paddingRight: "10px",
    paddingTop: "5px"
}

const weekdays = {
    margin: "0",
    padding: "10px 0",
    backgroundColor: "#ddd"
}

const day = {
    display: "inline-block",
    width: "13.6%",
    color: "#666",
    textAlign: "center"
}

function changeDateRange(selectedDate, startDate, endDate) {
    var date = new Date(selectedDate.valueOf());

    startDate.setDate(date.getDate());
    endDate.setDate(startDate.getDate() + (days.length - (startDate.getDay() + 1)));
}

function generateDays(startDate, endDate) {
    var indents = [];

    if (startDate > endDate) {
        return (<li>start date too great</li>)
    }

    if (startDate + 7 > endDate) {
        return (<li>end date to short</li>)
    }

    var currentDate = new Date();
    var i = 0;
    //pre dates which would be greyed out
    for (i = 0; i < startDate.getDay(); i++) {
        currentDate.setDate(startDate.getDate() + (i - startDate.getDay()));
        indents.push(<li style = {day} >{`${currentDate.getDate()} ${days[i % days.length]}`}</li>);
    }

    //active dates
    for (i = startDate.getDay(); i <= endDate.getDay(); i++) {
        currentDate.setDate(startDate.getDate() + (i - startDate.getDay()));
        indents.push(<li style = {day} >{`${currentDate.getDate()} ${days[i % days.length]}`}</li>);
    }
    return indents;
}

function generateTimesForDays(startDate, endDate, timeSlots) {
    var indents = [];
    var insidelocalStartDate = new Date();
    var localStartDate = new Date();
    var localTimeSlots = [...timeSlots];
    var j = 0;

    localStartDate.setDate(startDate.getDate() - 1);

    while (localTimeSlots.length > 0)
    {
        for(j = 0; j < days.length; j++) {
            if (localTimeSlots.length == 0)
                break;

            insidelocalStartDate.setDate(localStartDate.getDate() + j);
            
            var currentTimeSlotStartDate = new Date(localTimeSlots[0].startDateTime);
            var currentTimeSlotEndDate = new Date(localTimeSlots[0].endDateTime);
            
            if (currentTimeSlotStartDate.getDate() == insidelocalStartDate.getDate()) {
                localTimeSlots.shift();
                indents.push(
                    <li style = {day} >
                        <Button>{`
                            ${currentTimeSlotStartDate.getHours()}:${currentTimeSlotStartDate.getMinutes()} 
                            - 
                            ${currentTimeSlotEndDate.getHours()}:${currentTimeSlotEndDate.getMinutes()}`}
                        </Button>
                    </li>
                );
            } else {
                indents.push(<li style = {day}><br/></li>);
            }
        }
    }

    return indents;

}

//direction is bool
//true to go forward 1 week
//false to go back 1 week
function moveDate(startDate, endDate, direction) {
    if (direction) {
        startDate.setDate(startDate.getDate() + 7);
        endDate.setDate(endDate.getDate() + 7);
    } else {
        startDate.setDate(startDate.getDate() - 7);
        endDate.setDate(endDate.getDate() - 7);
    }

    console.log("button fired");
}

//https://www.w3schools.com/howto/howto_css_calendar.asp
//https://www.w3schools.com/howto/tryit.asp?filename=tryhow_css_calendar
const BookingView = ({timeSlots}) => {
    const [date] = useState(new Date());
    var currentWeekDate = new Date();
    var currentWeekEnd = new Date();

    changeDateRange(date, currentWeekDate, currentWeekEnd);

    //from today till end of the week
    const daysToEnd = days.slice(date.getDay()-1, days.length);

    return (
        <>
            <div style={ month }>
                <ul style = {{listStyleType: "none"}}>
                    <li style = {prev}><Button onClick={moveDate(currentWeekDate, currentWeekEnd, false)}>&#10094;</Button></li>
                    <li style = {next}><Button onClick={moveDate(currentWeekDate, currentWeekEnd, true)}>&#10095;</Button></li>
                    <li>
                        {monthNames[date.getMonth()]}<br/>
                        <span>{`${currentWeekDate.getDate()} ${days[currentWeekDate.getDay()]}`}</span>
                        <span> - </span>
                        <span>{`${currentWeekEnd.getDate()} ${days[currentWeekEnd.getDay()]}`}</span>
                    </li>
                </ul>
            </div>
            <ul style = { weekdays }>
                {generateDays(currentWeekDate, currentWeekEnd)}
                {generateTimesForDays(currentWeekDate, currentWeekEnd, timeSlots)}
            </ul>
        </>
    );
}

export { BookingView } 