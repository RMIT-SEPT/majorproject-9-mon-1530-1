import React from 'react';
import './App.css';
import About from './Components/About';
import Form from './Components/Form';
import Main from './Components/Main';
import Login from './Components/Login';
import Contact from './Components/Contact';
import Worker from './Components/Worker';
import Bookings from './Components/Bookings';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  body, html{
    font-family: Nunito Sans;
  }
`;

function App() {
  return (
    <>
      <GlobalStyle />
      <Router>
        <div className="App">
          <Route exact path="/" component={Main} />
          <Route exact path="/form" component={Form} />
          <Route exact path="/login" component={Login} />
          <Route exact path="/about" component={About} />
          <Route exact path="/contactus" component={Contact} />
          <Route exact path="/worker" component={Worker} />
          <Route exact path="/bookings" component={Bookings} />
        </div>
      </Router>
    </>
  );
}

export default App;
