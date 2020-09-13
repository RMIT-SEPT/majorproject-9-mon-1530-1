
import React from 'react';
import 'mutationobserver-shim';
import { render,fireEvent,act } from '@testing-library/react';
import Login from '../Components/Login';

global.MutationObserver = window.MutationObserver;
const testService = {
  username: "liza",
  password: 'liza',

};
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
 })

