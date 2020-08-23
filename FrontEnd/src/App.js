import React from 'react';
import './App.css';
import About from './Components/About';
import Form from './Components/Form';
import Main from './Components/Main';
import Login from './Components/Login';
import Contact from './Components/Contact';
import User from './Components/Dashboard/User';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { createGlobalStyle, ThemeProvider } from 'styled-components';

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
};

function App() {
  return (
    <>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <Router>
          <div className="App">
            <Route exact path="/" component={Main} />
            <Route exact path="/form" component={Form} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/about" component={About} />
            <Route exact path="/contactus" component={Contact} />
            <Route exact path="/user" component={User} />
          </div>
        </Router>
      </ThemeProvider>
    </>
  );
}

export default App;
