import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { theme } from '../../App';

import Main from '../Main';
global.MutationObserver = window.MutationObserver;

const renderComponent = (component) => {
  return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('Main', () => {
  it('should render Main', () => {
    const { getByText } = renderComponent(<Main></Main>);
    expect(getByText('Barbers')).toBeTruthy();
  });
});
