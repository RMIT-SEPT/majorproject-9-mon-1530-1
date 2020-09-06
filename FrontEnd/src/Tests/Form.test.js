import React from 'react';
import 'mutationobserver-shim';
import { render } from '@testing-library/react';
import Form from '../Components/Form';

global.MutationObserver = window.MutationObserver;

describe('Form', () => {
  it('should render a link to log in', () => {
    const { getByText } = render(<Form />);

    expect(getByText('Log-in')).toBeTruthy();
  });

  it('should render the form for member registration', () => {
    const { getByText } = render(<Form />);

    expect(getByText('Full Name')).toBeTruthy();
    expect(getByText('Username')).toBeTruthy();
    expect(getByText('Full Address')).toBeTruthy();
    expect(getByText('Contact Number')).toBeTruthy();
    expect(getByText('Password')).toBeTruthy();
  });
});
