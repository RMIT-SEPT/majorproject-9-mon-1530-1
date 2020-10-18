import React, { createContext, useState } from 'react';
import axios from 'axios';

export const BookingContext = createContext();

const BookingContextProvider = (props) => {
  const [customerId, setCustomerId] = useState();
  const [workerId, setWorkerId] = useState();
  const [startTime, setStartTime] = useState();
  const [endTime, setEndTime] = useState();

  const clearBooking = () => {
    setCustomerId();
    setWorkerId();
    setStartTime();
    setEndTime();
  };

  const submitBooking = async () => {
    const token = localStorage.getItem('token');

    // Removes 'Z' character from end of string to allow it to be passed
    const modifiedStartTime = startTime
      .toISOString()
      .substring(0, startTime.toISOString().length - 1);
    const modifiedEndTime = endTime
      .toISOString()
      .substring(0, endTime.toISOString().length - 1);

    const headers = {
      Authorization: `${token}`,
      username: `${customerId}`,
    };

    await axios.post(
      `${process.env.REACT_APP_BOOKINGS_ENDPOINT}/bookings`,
      {
        workerUsername: workerId,
        customerUsername: customerId,
        startDateTime: modifiedStartTime,
        endDateTime: modifiedEndTime,
      },
      { headers: headers }
    );
  };

  const bookingContext = {
    customerId,
    setCustomerId,
    workerId,
    setWorkerId,
    startTime,
    setStartTime,
    endTime,
    setEndTime,
    clearBooking,
    submitBooking,
  };

  return (
    <BookingContext.Provider value={{ ...bookingContext }}>
      {props.children}
    </BookingContext.Provider>
  );
};

export default BookingContextProvider;
