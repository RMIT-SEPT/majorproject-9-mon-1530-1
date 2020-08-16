import React from 'react';
import { render } from '@testing-library/react';
import Form from './Form';

describe('Form', () => {
  it('should render a link to sign in', () => {
    const { getByText } = render(<Form />);

    expect(getByText('Sign in')).toBeTruthy();
  });

  it('should render the form for member registration', () => {
    const { getByText } = render(<Form />);

    expect(getByText('First Name')).toBeTruthy();
    expect(getByText('Surname')).toBeTruthy();
    expect(getByText('Full address')).toBeTruthy();
    expect(getByText('Contact number')).toBeTruthy();
    expect(getByText('Password')).toBeTruthy();
  });
});
