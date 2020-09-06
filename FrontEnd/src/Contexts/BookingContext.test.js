import React, { useContext } from 'react';
import axios from 'axios';
import { mount } from 'enzyme';
import BookingProvider, { BookingContext } from './BookingContext';

const defaultData = {
  customerId: 'rw22448',
  workerId: 'jeffOak',
  startTime: '2020-08-29T12:00:00',
  endTime: '2020-08-29T13:00:00',
};

const TestComponent = () => {
  const {
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
  } = useContext(BookingContext);

  return (
    <>
      <div>
        <span data-testid="customerId">{customerId}</span>
        <span data-testid="workerId">{workerId}</span>
        <span data-testid="startTime">{startTime}</span>
        <span data-testid="endTime">{endTime}</span>
      </div>
      <button data-testid="clearButton" type="button" onClick={clearBooking}>
        Clear
      </button>
      <button
        data-testid="fillButton"
        type="button"
        onClick={() => {
          setCustomerId(defaultData.customerId);
          setWorkerId(defaultData.workerId);
          setStartTime(defaultData.startTime);
          setEndTime(defaultData.endTime);
        }}
      >
        Fill
      </button>
      <button data-testid="submitButton" type="button" onClick={submitBooking}>
        Submit
      </button>
    </>
  );
};

describe('BookingContext', () => {
  describe('clearBooking', () => {
    it('should clear state of all booking attributes', () => {
      const wrapper = mount(
        <BookingProvider>
          <TestComponent />
        </BookingProvider>
      );

      wrapper.find('[data-testid="fillButton"]').simulate('click');

      expect(wrapper.find('[data-testid="customerId"]').text()).toEqual(
        'rw22448'
      );

      wrapper.find('[data-testid="clearButton"]').simulate('click');

      expect(wrapper.find('[data-testid="customerId"]').text()).toEqual('');
    });
  });

  describe('submitBooking', () => {
    let spy;

    beforeEach(() => {
      spy = jest.spyOn(axios, 'post');
    });

    afterEach(() => {
      spy.mockRestore();
    });

    it('should submit a booking with all details required', () => {
      const wrapper = mount(
        <BookingProvider>
          <TestComponent />
        </BookingProvider>
      );

      wrapper.find('[data-testid="fillButton"]').simulate('click');
      wrapper.find('[data-testid="submitButton"]').simulate('click');

      expect(spy).toHaveBeenCalled();
    });
  });
});
