import React from 'react';
import { fireEvent, render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { DashboardWrapper, MenuBarComponent } from './Dashboard';
import { theme } from '../../App';

// Added this render method to wrap components rendered in Theme tag to prevent
// test failing

const renderComponent = (component) => {
  return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('Dashboard', () => {
  describe('DashboardWrapper', () => {
    const bookingMock = jest.fn();

    const userName = 'testId123';
    const role = 'User';
    const actions = { bookingLink: bookingMock };

    afterEach(() => {
      jest.clearAllMocks();
    });

    it('should render a new Dashboard successfully when passing in required props', () => {
      const { getByText } = renderComponent(
        <DashboardWrapper
          userName={userName}
          role={role}
          actions={actions}
        ></DashboardWrapper>
      );

      expect(getByText(userName)).toBeTruthy();
      expect(getByText(role)).toBeTruthy();
    });

    it('should render children components inside Dashboard when provided', () => {
      const { getByText } = renderComponent(
        <DashboardWrapper userName={userName} role={role} actions={actions}>
          <div>Hello World!</div>
        </DashboardWrapper>
      );

      expect(getByText('Hello World!')).toBeTruthy();
    });

    it('should display placeholder info when props not provided', () => {
      const { getAllByText } = renderComponent(
        <DashboardWrapper actions={actions}></DashboardWrapper>
      );

      expect(getAllByText('empty')).toHaveLength(2);
    });

    describe('actions', () => {
      it('should fire a callback when clicking on New Booking', () => {
        const { getByText } = renderComponent(
          <DashboardWrapper
            userName={userName}
            role={role}
            actions={actions}
          ></DashboardWrapper>
        );

        fireEvent.click(getByText('New Booking'));

        expect(bookingMock).toHaveBeenCalled();
      });
    });
  });

  describe('MenuBarComponent', () => {
    it('should render the MenuBarComponent with default LogOut icon', () => {
      const { getByTestId } = renderComponent(
        <MenuBarComponent></MenuBarComponent>
      );

      expect(getByTestId('logOutIcon')).toBeTruthy();
    });

    it('should render all children components passed under MenuBarComponent', () => {
      const { getAllByRole } = renderComponent(
        <MenuBarComponent>
          <button>Button 1</button>
          <button>Button 2</button>
          <button>Button 3</button>
        </MenuBarComponent>
      );

      expect(getAllByRole('button')).toHaveLength(3);
    });
  });
});
