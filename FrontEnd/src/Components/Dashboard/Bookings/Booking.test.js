import axios from 'axios';

const booking = require('./Booking');

jest.mock('axios');

const testWorker = {
  workerUserName: 'kath123',
  workerFullName: 'Kathreen McDonald',
};

describe('Booking', () => {
  describe('setBookingSelectedWorker', () => {
    it('should take a worker as parameter', () => {
      const spy = jest.spyOn(booking, 'setBookingSelectedWorker');

      booking.setBookingSelectedWorker(testWorker);

      expect(spy).toHaveBeenCalledWith(testWorker);
    });
  });

  describe('setBookingStartTime', () => {
    it('should take a DateTime as parameter', () => {
      const spy = jest.spyOn(booking, 'setBookingStartTime');
      const date = new Date(1);

      booking.setBookingStartTime(date);

      expect(spy).toHaveBeenCalledWith(date);
    });
  });

  describe('setBookingEndTime', () => {
    it('should take a DateTime as parameter', () => {
      const spy = jest.spyOn(booking, 'setBookingEndTime');
      const date = new Date(1);

      booking.setBookingEndTime(date);

      expect(spy).toHaveBeenCalledWith(date);
    });
  });

  describe('submitBooking', () => {
    beforeAll(() => {});

    it.only('should successfully submit POST request for a booking for a user', async () => {
      axios.post.mockImplementationOnce(() =>
        Promise.resolve({
          bookingId: 5,
          workerUsername: 'jeffOak',
          customerUsername: 'rw22448',
          startDateTime: '2019-08-29T12:00:00',
          endDateTime: '2019-08-29T13:00:00',
        })
      );

      // const spy = jest.spyOn(axios, 'post');

      // const res = await booking.submitBooking('testUser');

      // expect(res).toEqual({
      //   bookingId: 5,
      //   workerUsername: 'jeffOak',
      //   customerUsername: 'rw22448',
      //   startDateTime: '2019-08-29T12:00:00',
      //   endDateTime: '2019-08-29T13:00:00',
      // });
      await expect(booking.submitBooking('testUser')).resolves.toEqual({
        bookingId: 5,
        workerUsername: 'jeffOak',
        customerUsername: 'rw22448',
        startDateTime: '2019-08-29T12:00:00',
        endDateTime: '2019-08-29T13:00:00',
      });

      // expect(spy).toHaveBeenCalled();
    });
  });
});
