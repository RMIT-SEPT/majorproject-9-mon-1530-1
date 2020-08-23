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

export { setBookingSelectedWorker, setBookingStartTime, setBookingEndTime };
