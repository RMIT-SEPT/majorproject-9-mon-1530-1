
import React from 'react';
import 'mutationobserver-shim';
import { render, fireEvent, act } from '@testing-library/react';
import Login from '../Components/Login';


it("should watch the input correctly", () => {

  const { container } = render(<Login />);

  const mockUsername = "John Doe";
  const mockPwd = "John123!";

  const newLogin = container.querySelector(
    "input[name='username']"
  );
  const password = container.querySelector(
    "input[name='password']"
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
  expect(newLogin.value).toEqual(mockUsername);
  expect(password.value).toEqual(mockPwd);
  // expect(mockChangeValue).toBeCalledTimes(1);
});
describe('Form', () => {
  it('should render a link to log in', () => {
    const { getByText } = render(<Login />);

    expect(getByText('Sign up')).toBeTruthy();
  });

  it('should render the login for member login', () => {
    const { getByText } = render(<Login />);

    expect(getByText('User Name')).toBeTruthy();
    expect(getByText('Password')).toBeTruthy();
  });

  // it('should simulate On SUbmit event', () => {
  //   const onSubmitSpy = jest.fn();
  //   const { getByText } = render(
  //     //how do i fix this 
  //    <
  //   );
  //   fireEvent.click(getByText('SUBMIT'));
  //   expect(onSubmitSpy).toHaveBeenCalled();
  // });
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
    expect(container.textContent).toMatch("The username or password is incorrect.");
    // expect(mockChangeValue).toBeCalledTimes(1);
  }});
})

