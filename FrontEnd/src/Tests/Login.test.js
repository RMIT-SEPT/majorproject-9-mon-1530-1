
import React from 'react';
import 'mutationobserver-shim';
import { render } from '@testing-library/react';
import Login from '../Components/Login';

global.MutationObserver = window.MutationObserver;

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

  test('Should call onSubmit prop for valid login submission', () => {
    const expense = {
      username: "liza",
      password: liza,
    }
  
    const onSubmitSpy = jest.fn();
  
    const wrapper = shallow(
      <ExpenseForm expense={expense} onSubmit={onSubmitSpy} />
    );
  
    wrapper.find('form').simulate('submit', { preventDefault: () => {} });
  
    expect(wrapper.state('error')).toBe('');
    expect(onSubmitSpy).toHaveBeenLastCalledWith({
      description: expense.description,
      amount: expense.amount,
      note: expense.note
    });


  });
}
