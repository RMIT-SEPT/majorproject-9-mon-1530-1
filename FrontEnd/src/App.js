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
        <h1>
          Working pages 
        </h1>
        <h2>
        /main
        </h2>
        <h2>
        /form
        </h2>
        <h2>
        /Login
        </h2>
        <h2>
        /contactus
        </h2>
        <h2>
        /About
        </h2>
        <Route path="/main" component={Main} />
        <Route path="/form" component={Form} />
        <Route path="/login" component={Login} />
        <Route path="/about" component={About} />
        <Route path="/contactus" component={Contact} />
      </div>
    </Router>
  );
}

export default App;
