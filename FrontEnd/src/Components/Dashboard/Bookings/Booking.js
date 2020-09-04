const axios = require('axios');

// Handles the logic for booking an appointment
// TODO: Possible refactor logic into User as state

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
  // TODO: Handle errors
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
