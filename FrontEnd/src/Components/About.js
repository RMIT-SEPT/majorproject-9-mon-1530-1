import React from 'react';
import styled from 'styled-components';
import {Grid}  from '@material-ui/core';
import doctor from '../media/doc.png';
import gym from '../media/gym.svg';
import TextLoop from "react-text-loop";
import Toolbar from '../Components/Toolbar.js';

const Doc = styled.img`
  width: 60%;
  height: auto;
  background-repeat: no-repeat;
  background-size: contain;
  padding-left: 5%;
`
const Gym = styled.img`
  width: 80%;
  height: auto;
  background-repeat: no-repeat;
  background-size: contain;
`
const Bold = styled.div`
  font-family: Nunito Sans;
  font-style: normal;
  font-weight: bold;
  font-size: 35px;
  line-height: 95px;
  letter-spacing: -0.05em;
  color: #FFFFFF;
  margin: 5%;
  white-space: 'pre-wrap';
`

const MainWrapper = styled.div`
  background-color:black!important;
  height: 100vh;
  flex-grow: 1;
  background-color: black;
  color: white;
padding :1%;

`
const about = (props) => {
  document.body.style = 'background:black;'
  return (
    <MainWrapper>
      <Grid container container direction="row" alignItems="center" justify="space-between" spacing={5} >
        <Grid item xs={12}>
          <Toolbar />
        </Grid>
        <Grid item xs={5}>
          <Bold>{"Agem helps your find you best \nLocal services such as\n"}
            <TextLoop interval={350} >
              <span>Barbers </span>
              <span>Nail technicians</span>
              <span>Dentists</span>
              <span>Gyms</span>
              <span>Fitness Coaches</span>
            </TextLoop>{" "} And Much more .
            </Bold>
        </Grid>
        <Grid item xs={5} justify="flex-end" >
          <Gym src={gym} alt="gym" />
        </Grid>
        <Grid item xs={6}>
          <Doc src={doctor} alt="doctor" />
        </Grid>
        <Grid item xs={6}>
          <Bold>{"Agem helps your find you best \nLocal services such as\n"}
            <TextLoop interval={350} >
              <span>Barbers </span>
              <span>Nail technicians</span>
              <span>Dentists</span>
              <span>Gyms</span>
              <span>Fitness Coaches</span>
            </TextLoop>{" "} And Much more .
            </Bold>
        </Grid>
      </Grid>
    </MainWrapper >

  )
}

export default about; 