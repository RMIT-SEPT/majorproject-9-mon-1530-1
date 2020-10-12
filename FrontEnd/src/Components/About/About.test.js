import React from 'react';
import { render } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { theme } from '../../App';
import About from './About';

global.MutationObserver = window.MutationObserver;

const renderComponent = (component) => {
    return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('About', () => {
    it('should render About', () => {
        const { getByText } = renderComponent(<About></About>);
        expect(getByText('Barbers')).toBeTruthy();
        expect(getByText('Signup or login')).toBeTruthy();
    });
});
