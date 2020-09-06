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
    // TODO: Handle errors
    await axios.post('http://localhost:8081/bookings', {
      workerUsername: workerId,
      customerUsername: customerId,
      startDateTime: startTime,
      endDateTime: endTime,
    });
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
