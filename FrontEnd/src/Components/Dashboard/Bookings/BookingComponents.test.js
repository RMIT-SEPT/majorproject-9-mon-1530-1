import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import {
  ServiceCard,
  WorkerRadioButton,
  TimeSelector,
} from './BookingComponents';
import { theme } from '../../../App';

const testService = {
  serviceName: "Bambi's Restaurant",
  address: '123 Test Avenue, Melbourne, VIC, Australia',
  phoneNumber: '04 4151 3232',
};

const testWorker = {
  workerUserName: 'kath123',
  workerFullName: 'Kathreen McDonald',
};

const renderComponent = (component) => {
  return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('BookingComponents', () => {
  describe('ServiceCard', () => {
    it('should render component when provided a service as child', () => {
      const {} = renderComponent(<ServiceCard>{testService}</ServiceCard>);
    });
  });

  describe('WorkerRadioButton', () => {
    it('should render component when provided a worker as prop', () => {
      const {} = renderComponent(
        <WorkerRadioButton worker={testWorker}></WorkerRadioButton>
      );
    });
  });

  describe('TimeSelector', () => {
    const label = 'Time';

    it('should render component with a given label', () => {
      const { getByText } = renderComponent(
        <TimeSelector label={label}></TimeSelector>
      );

      expect(getByText(label)).toBeTruthy();
    });

    it('should activate onChange function when interacting with component', () => {
      const onChangeMock = jest.fn();

      const { debug } = renderComponent(
        <TimeSelector
          label={label}
          onChange={() => {
            console.log('Click!');
          }}
        ></TimeSelector>
      );

      debug();
    });
  });
});
