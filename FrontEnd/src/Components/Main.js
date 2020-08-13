import React, { useState } from 'react'
import styled from 'styled-components';
import { Button, Grid, InputLabel, Box, FormControl, Select } from '@material-ui/core';
import big from '../media/big.png';
import book from '../media/book.png';
import hairdresser from '../media/hairdres.png';
import Toolbar from '../Components/Toolbar.js';
import TextLoop from "react-text-loop";
import DateTimePicker from 'react-datetime-picker';
import { withStyles } from "@material-ui/core/styles";
import Find from '../Components/Find.js'

const defaultProps = {
  borderColor: '#5AC490',
  m: 5,
  border: 3,
  style: { width: '95%', height: '14rem' },
};
const MainWrapper = styled.div`
  background-color:black!important;
  height: 100vh;
  flex-grow: 1;
  background-color: black;
  color: white;
padding :1%;

`
const BigFont = styled.div`
  font-family: Nunito Sans;
  font-style: normal;
  font-weight: bold;
  font-size: 70px;
  line-height: 95px;
  letter-spacing: -0.05em;
  color: #FFFFFF;
  margin: 1%;
  margin-left: 5%;

`
const SmallerFont = styled.div`
  font-family: Nunito Sans;
  font-style: normal;
  font-weight: bold;
  font-size: 26px;
  line-height: 35px;
  letter-spacing: -0.05em;
  color: white;
  margin: 1%;
  margin-left: 5%;

`
const Hairdresser = styled.img`
  width: 80%;
  height: auto;
  background-repeat: no-repeat;
  background-size: contain;

`


//this is the main page, it contains a tool bar 
//and it contains the search for a service feature
// items are allocated evenly using a Grid function in material ui library 
// we use normal routing in order to move between pages 

function Main(props) {
  document.body.style = 'background:black;'
  const [date, setDate] = useState(new Date())
  const onChange = date => setDate(date)
  return (
    <MainWrapper>
      <Grid container direction="row" alignItems="center" justify="space-between" spacing={5} >
        <Grid item xs={12}>
          <Toolbar />
        </Grid>
        <Grid item xs={12}>
         <Find></Find>
        </Grid>
        <Grid item xs={12}>
          <Grid container container direction="row" alignItems="center" justify="space-between" spacing={5} >
            <Grid item xs={6}>
              <BigFont>Find Best Sevices </BigFont>
              <BigFont>Near You</BigFont>
              <SmallerFont> All of our services are done by licenced experts in their fields.</SmallerFont>
              <SmallerFont> Our services{" "}
                <TextLoop interval={350} >
                  <span> Barbers </span>
                  <span> Nail technicians </span>
                  <span> Dentists </span>
                  <span> Gyms </span>
                  <span> Fitness Coaches </span>
                </TextLoop>{" "} And Much more .
              </SmallerFont>
            </Grid>
            <Grid item xs={6}>
              <Hairdresser src={hairdresser} alt="hairdresser" />
            </Grid>
          </Grid>
        </Grid>
        <Grid item xs={12}>
          <Grid container container direction="row" alignItems="baseline" justify="flex-start" spacing={5} >
            <Grid item xs={3}>
              <a href="http://localhost:3000/form"> <img style={{ marginLeft: "10%" }} src={big} alt="big" /> </a>
            </Grid>
            <Grid item xs={3}>
              <img src={book} alt="big" href="http://localhost:3000/form" />
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </MainWrapper>
  )
}
export default Main; 