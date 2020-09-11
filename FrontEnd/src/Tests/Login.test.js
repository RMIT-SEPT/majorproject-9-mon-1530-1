import React from 'react';
import 'mutationobserver-shim';
import { render } from '@testing-library/react';
import Login from '../Components/Login';
import Form from '../Components/Form';
global.MutationObserver = window.MutationObserver;

describe('Log in', () => {
  it('should render a link to log in', () => {
    const { getByText } = render(<Login />);

    expect(getByText('Log In')).toBeTruthy();
  });

  it('should render the log in for member registration', () => {
    const { getByText } = render(<Login />);

    expect(getByText('User Name')).toBeTruthy();
    expect(getByText('Password')).toBeTruthy();
  });
  it('should render a link to form', () => {
    const { getByText } = render(<Login/>);

    expect(getByText('Sign up')).toBeTruthy();
  });
});
