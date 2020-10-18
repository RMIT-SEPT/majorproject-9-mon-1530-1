import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { theme } from '../../App';
import Contact from './Contact';

global.MutationObserver = window.MutationObserver;

const renderComponent = (component) => {
  return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('Contact', () => {
  it('should render Contact', () => {
    const { getByText } = renderComponent(<Contact></Contact>);
    expect(getByText('Elizabeth Tawaf:')).toBeTruthy();
    expect(getByText('Brodey Yendall:')).toBeTruthy();
    expect(getByText('Richard Wang:')).toBeTruthy();
    expect(getByText('Abrar Alsagheer:')).toBeTruthy();
    expect(getByText('Lawrence Abdelmalek:')).toBeTruthy();
  });
});
