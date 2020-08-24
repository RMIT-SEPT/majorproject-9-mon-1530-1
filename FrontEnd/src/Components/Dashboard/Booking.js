const axios = require('axios');

var selectedWorker;
var startTime;
var endTime;

const setBookingSelectedWorker = (worker) => {
  selectedWorker = worker;
  console.log(selectedWorker.workerUserName);
};

const setBookingStartTime = (time) => {
  startTime = time;
  console.log('Start time ' + startTime);
};

const setBookingEndTime = (time) => {
  endTime = time;
  console.log('End time ' + endTime);
};

const submitBooking = async (userId) => {
  console.log('Booking...');
  console.log('User = ' + userId);
  console.log('Worker = ' + selectedWorker.workerUserName);
  console.log('Start = ' + startTime);
  console.log('End = ' + endTime);

  await axios.post('http://localhost:8081/bookings', {
    workerUsername: selectedWorker.workerUserName,
    customerUsername: userId,
    startDateTime: startTime,
    endDateTime: endTime,
  });
};

export {
  setBookingSelectedWorker,
  setBookingStartTime,
  setBookingEndTime,
  submitBooking,
};
