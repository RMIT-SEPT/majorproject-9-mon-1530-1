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
        expect(getByText('Elizabeth')).toBeTruthy();
        expect(getByText('Brodey')).toBeTruthy();
        expect(getByText('Richard')).toBeTruthy();
        expect(getByText('Abrar')).toBeTruthy();
        expect(getByText('Lawrence')).toBeTruthy();
    });
});
