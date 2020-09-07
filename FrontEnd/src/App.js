import React from 'react';
import './App.css';
import About from './Components/About';
import Form from './Components/Form';
import Main from './Components/Main';
import Login from './Components/Login';
import Contact from './Components/Contact';
import User from './Components/Dashboard/User/User';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { createGlobalStyle, ThemeProvider } from 'styled-components';
import BrowserContext from './Contexts/BrowserContext';
import BookingContext from './Contexts/BookingContext';
import { ProtectedRoute } from './Components/ProtectedRoute.js';
import Unauthorized from './Components/Unauthorized';
import Authorized from './Components/Authorized';

const GlobalStyle = createGlobalStyle`
  body, html{
    font-family: Nunito Sans;
  }
`;

const theme = {
  colours: {
    greenPrimary: '#5ac490',
    greenSecondary: '#369668',
    greenTertiary: '#1b4b34',
  },
  dashboard: {
    defaultWidth: '1304px',
  },
  transition: {
    short: '0.15s',
  },
  font: {
    primary: 'Nunito Sans',
  },
  fontWeight: {
    semiBold: '550',
  },
  shadow: {
    defaultBoxShadow: '0px 4px 8px rgba(0, 0, 0, 0.25)',
  },
};

function App() {
  return (
    <>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <div className="App">
          <BrowserContext>
            <BookingContext>
              <Router>
                <Route exact path="/" component={Main} />
                <Route exact path="/form" component={Form} />
                <Route exact path="/login" component={Login} />
                <Route exact path="/about" component={About} />
                <Route exact path="/contactus" component={Contact} />
                <Route exact path="/user" component={User} />
                <Route exact path="/unauthorized" component={Unauthorized} />
                <ProtectedRoute
                  exact
                  path="/authorized"
                  component={Authorized}
                />
              </Router>
            </BookingContext>
          </BrowserContext>
        </div>
      </ThemeProvider>
    </>
  );
}

export default App;
export { theme };
