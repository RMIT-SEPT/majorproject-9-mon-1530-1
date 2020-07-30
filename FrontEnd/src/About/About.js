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

const about = (props) => {
  return (
    <div className='main'>
      <div className='toolBar'>
        <Grid container container direction="row" alignItems="flex-start" justify="space-between" >
          <Grid item xs={2}>
            <a href="http://localhost:3000/main"> <img className="logo" src={logo} alt="logo" /> </a>
          </Grid>
          <Grid item xs={2}>
            <GreenButton variant="text" >About Agem</GreenButton>
          </Grid>
          <Grid item xs={2}>
            <GreenButton variant="text" >Contact-us</GreenButton>
          </Grid>
          <Grid item xs={2}>
            <GreenButton variant="text"> Lolols</GreenButton>
          </Grid>
          <Grid item xs={1}>
            <a href="http://localhost:3000/login"> <img className="login" src={login} alt="login" /> </a>
          </Grid>
          <Grid item xs={1}>
            <a href="http://localhost:3000/form"> <img className="small" src={small} alt="small" /> </a>
          </Grid>
        </Grid>
      </div>
      <Grid container container direction="row" alignItems="center"  justify="space-between"spacing={5} >
        
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
    </div >

  )
}


{ }

{/* */ }
export default about; 