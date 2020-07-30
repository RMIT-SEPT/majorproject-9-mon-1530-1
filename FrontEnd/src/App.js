import React from 'react';
import './App.css';
import Form from './Form/Form.js';
import Main from './Main/Main.js';
import Login from './Login/Login.js';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import {createGlobalStyle} from 'styled-components'

const GlobalStyle =createGlobalStyle`
  body,html{
    font-family:Nunito Sans;
  }

`
function App() {
  return (
    <Router>
      <div className="App">
      <Route path= "/main" component={Main}/>
        <Route path= "/form" component={Form}/>
        <Route path= "/login" component={Login}/>
      </div>
    </Router>
  );
}

export default App;
