const axios = require('axios');

// Handles the logic for booking an appointment

var selectedWorker;
var startTime;
var endTime;

const setBookingSelectedWorker = (worker) => {
  selectedWorker = worker;
};

const setBookingStartTime = (time) => {
  startTime = time;
};

const setBookingEndTime = (time) => {
  endTime = time;
};

const submitBooking = async (userId) => {
  await axios.post('http://localhost:8081/bookings', {
    workerUsername: selectedWorker.workerUserName,
    customerUsername: userId,
    startDateTime: startTime,
    endDateTime: endTime,
  });
};

const clearBooking = () => {
  setBookingSelectedWorker(undefined);
  setBookingStartTime(undefined);
  setBookingEndTime(undefined);
};

export {
  setBookingSelectedWorker,
  setBookingStartTime,
  setBookingEndTime,
  submitBooking,
  clearBooking,
};
