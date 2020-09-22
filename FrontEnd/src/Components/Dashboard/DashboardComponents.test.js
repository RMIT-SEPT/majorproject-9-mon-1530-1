import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import {
  DashboardModule,
  Button,
  UpcomingAppointmentCard,
} from './DashboardComponents';
import { theme } from '../../App';

// Added this render method to wrap components rendered in Theme tag to prevent
// test failing

const renderComponent = (component) => {
  return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('DashboardComponents', () => {
  describe('Button', () => {
    it('should render a Button', () => {
      const { getByRole } = renderComponent(<Button>Test button</Button>);

      expect(getByRole('button')).toBeTruthy();
    });
  });

  describe('DashboardModule', () => {
    it('should render a DashboardModule with title', () => {
      const { getByText } = renderComponent(
        <DashboardModule title="Test module"></DashboardModule>
      );

      expect(getByText('Test module')).toBeTruthy();
    });

    it('should render children under the component', () => {
      const { getAllByRole } = renderComponent(
        <DashboardModule title="Test module">
          <button>Button 1</button>
          <button>Button 1</button>
          <button>Button 1</button>
        </DashboardModule>
      );

      expect(getAllByRole('button')).toHaveLength(3);
    });
  });

  describe('UpcomingAppointmentCard', () => {
    const testBooking = {
      bookingId: '1',
      workerUsername: 'testWorker',
      customerUsername: 'mockCustomer',
      startDateTime: '2022-01-13T14:00',
      endDateTime: '2022-01-13T15:00',
    };

    it('should render an UpcomingAppointmentCard with booking information passed from props', () => {
      const { getByText } = renderComponent(
        <UpcomingAppointmentCard booking={testBooking} />
      );

      expect(getByText('testWorker')).toBeTruthy();
      expect(getByText('13/01/2022')).toBeTruthy();
      expect(getByText('2:00:00 pm')).toBeTruthy();
      expect(getByText('3:00:00 pm')).toBeTruthy();
    });
  });
});
