import React from 'react';
import 'mutationobserver-shim';
import { render } from '@testing-library/react';

import Main from '../Components/Main';
global.MutationObserver = window.MutationObserver;

describe('Main', () => {
  it('should render Main', () => {
    const { getByText } = render(<Main />);

    expect(getByText('About')).toBeTruthy();
  });


});
