
import React from 'react';
import 'mutationobserver-shim';
import { render, fireEvent, act } from '@testing-library/react';
import Login from '../Components/Login';

describe('Form', () => {
  it("Should watch the input correctly", () => {

    //creat the mock data
    const { container } = render(<Login />);
    const mockUsername = "John Doe";
    const mockPwd = "John123!";

    //mock login steps
    const newLogin = container.querySelector(
      "input[name='username']"
    );
    const password = container.querySelector(
      "input[name='password']"
    );
    //fire event the username and password
    fireEvent.input(newLogin, {
      target: {
        value: mockUsername
      }
    });
    fireEvent.input(password, {
      target: {
        value: mockPwd
      }
    });
    //expected to match the user input
    expect(newLogin.value).toEqual(mockUsername);
    expect(password.value).toEqual(mockPwd);
    // expect(mockChangeValue).toBeCalledTimes(1);
  });



  it('should render a link to log in and should render the login for member login', () => {
    const { getByText } = render(<Login />);
    expect(getByText('Sign up')).toBeTruthy();
    expect(getByText('User Name')).toBeTruthy();
    expect(getByText('Password')).toBeTruthy();
  });

  it("should submit the wrong login details, and the error message should appear ", () => {
    async () => {
      const { container } = render(<Login />);

      const mockUsername = "John Doe";
      const mockPwd = "John123!";

      const newLogin = container.querySelector(
        "input[name='username']"
      );
      const password = container.querySelector(
        "input[name='password']"
      );
      const submitButton = container.querySelector(
        "input[type='submit']"
      );

      fireEvent.input(newLogin, {
        target: {
          value: mockUsername
        }
      });

      fireEvent.input(password, {
        target: {
          value: mockPwd
        }
      });
      await act(async () => {
        fireEvent.submit(submitButton);
      });
      expect(newLogin.value).toEqual(mockUsername);
      expect(password.value).toEqual(mockPwd);
      expect(getByText("The username or password is incorrect.")).toBeTruthy();
    }
  });

  it("should submit the wrong login details, and the error message should appear ", () => {
    async () => {
      const { container } = render(<Login />);

      const mockUsername = "Liza";
      const mockPwd = "Liza";

      const newLogin = container.querySelector(
        "input[name='username']"
      );
      const password = container.querySelector(
        "input[name='password']"
      );
      const submitButton = container.querySelector(
        "input[type='submit']"
      );

      fireEvent.input(newLogin, {
        target: {
          value: mockUsername
        }
      });

      fireEvent.input(password, {
        target: {
          value: mockPwd
        }
      });
      await act(async () => {
        fireEvent.submit(submitButton);
      });
      expect(newLogin.value).toEqual(mockUsername);
      expect(password.value).toEqual(mockPwd);
      expect(getByText("Welcome")).toBeTruthy();
    }
  });
})

