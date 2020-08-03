import React from 'react';
import './App.css';
import About from './About/About.js';
import Form from './Form/Form.js';
import Main from './Main/Main.js';
import Login from './Login/Login.js';
import Contact from './Contact/Contact.js';
import TextLoop from "react-text-loop";
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import { createGlobalStyle } from 'styled-components'

const GlobalStyle = createGlobalStyle`
  body,html{
    font-family:Nunito Sans;
  }

`
function App() {
  return (
    <Router>
      <div className="App">
        <Route exact path="/main" component={Main} />
        <Route exact path="/form" component={Form} />
        <Route exact path="/login" component={Login} />
        <Route exact path="/about" component={About} />
        <Route exact path="/contactus" component={Contact} />
      </div>
    </Router>
  );
}

export default App;
