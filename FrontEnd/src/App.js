import React from 'react';
import './App.css';
import About from './Components/About.js';
import Form from './Components/Form.js';
import Main from './Components/Main.js';
import Login from './Components/Login.js';
import Contact from './Components/Contact.js';
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
        </div>
      </Router>
    </>
  );
}

export default App;
