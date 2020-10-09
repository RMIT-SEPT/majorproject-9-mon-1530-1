import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { theme } from '../../App';
import Toolbar from './Toolbar';

global.MutationObserver = window.MutationObserver;

const renderComponent = (component) => {
  return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('Toolbar', () => {
  it('should render Toolbar', () => {
    const { getByText } = renderComponent(<Toolbar></Toolbar>);
    expect(getByText('Contact-us')).toBeTruthy();
    expect(getByText('About')).toBeTruthy();
  });
});
