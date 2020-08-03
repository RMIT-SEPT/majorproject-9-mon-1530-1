import React from 'react';
import './About.css';
import styled from 'styled-components';
import { Button, Grid } from '@material-ui/core';
import logo from '../media/logo.png';
import login from '../media/login.png';
import doctor from '../media/doc.png';
import gym from '../media/gym.svg';
import small from '../media/small.png';
import TextLoop from "react-text-loop";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import Toolbar from '../Toolbar/Toolbar.js';

const GreenButton = styled(Button)`
.MuiButton-label{
  font-family:'Nunito sans';
  color:#5AC490;
font-style: normal;
// font-weight: bold;
font-size: 30px;
line-height: 41px;
letter-spacing: -0.05em;
}
`
const MainWrapper = styled.div`
  background-color:black!important;

`
const about = (props) => {
  document.body.style = 'background:black;'
  return (
    <MainWrapper className='main'>
      <Grid container container direction="row" alignItems="center" justify="space-between" spacing={5} >
        <Grid item xs={12}>
          <Toolbar />
        </Grid>
        <Grid item xs={5}>
          <div className="Bold" style={{
            'white-space': 'pre-wrap'
          }}>{"Agem helps your find you best \nLocal services such as\n"}
            <TextLoop interval={350} >
              <span>Barbers </span>
              <span>Nail technicians</span>
              <span>Dentists</span>
              <span>Gyms</span>
              <span>Fitness Coaches</span>
            </TextLoop>{" "} And Much more .
            </div>
        </Grid>
        <Grid item xs={5} justify="flex-end" >
          <img className="gym" src={gym} alt="gym" />
        </Grid>
        <Grid item xs={6}>
          <img className="doctor" src={doctor} alt="doctor" />
        </Grid>
        <Grid item xs={6}>
          <div className="Bold" style={{
            'white-space': 'pre-wrap'
          }}>{"Agem helps your find you best \nLocal services such as\n"}
            <TextLoop interval={350} >
              <span>Barbers </span>
              <span>Nail technicians</span>
              <span>Dentists</span>
              <span>Gyms</span>
              <span>Fitness Coaches</span>
            </TextLoop>{" "} And Much more .
            </div>
        </Grid>
        <Grid item xs={6}>
          <img className="doctor" src={doctor} alt="doctor" />
        </Grid>
        <Grid item xs={6}>
          <div className="Bold" style={{
            'white-space': 'pre-wrap'
          }}>{"Agem helps your find you best \nLocal services such as\n"}
            <TextLoop interval={350} >
              <span>Barbers </span>
              <span>Nail technicians</span>
              <span>Dentists</span>
              <span>Gyms</span>
              <span>Fitness Coaches</span>
            </TextLoop>{" "} And Much more .
            </div>
        </Grid>
      </Grid>
    </MainWrapper >

  )
}


{ }

{/* */ }
export default about; 