import React from 'react'
import './Main.css'
import styled from 'styled-components';
import { Button, Grid } from '@material-ui/core';
import logo from '../media/logo.png';
import big from '../media/big.png';
import book from '../media/book.png';
import login from '../media/login.png';
import hairdres from '../media/hairdres.png';
import small from '../media/small.png';
import construction from '../media/undraw_under_construction_46pa-2 1.png';
import {BrowserRouter as Router,Switch,Route,Link} from "react-router-dom";
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
const WhiteButton = styled(Button)`

.MuiButton-label{
  font-family:'Nunito sans';
  color:#fff;
font-style: normal;
font-weight: bold;
font-size: 28px;
line-height: 41px;
letter-spacing: -0.05em;

}
`
const MainWrapper = styled.div`
  background-color:black!important;

`


const main = (props) => {
  return (
    <MainWrapper className='main'>
      <Grid container container direction="row" alignItems="center" justify="space-between" spacing={5} >
        <Grid item xs={12}>
          <Toolbar />
        </Grid>
        <Grid item xs={12}>
          <Grid container container direction="row" alignItems="center" justify="space-between" spacing={5} >
            <Grid item xs={6}>
              <div className='bold'>Find Best Sevices</div>
              <div className='bold'> Near You </div>
              <div className='smaller'> All of our services are done by licenced experts in their fields.</div>
              <div className='smaller'> Our services include, Barbers, Nail technicians,  Dentists, Fitness </div>
              <div className='smaller'>coaches and much more  </div>
            </Grid>
            <Grid item xs={6}>
              <img className="hairdres" src={hairdres} alt="hairdres" />
            </Grid>
          </Grid>
        </Grid>
        <Grid item xs={12}>
          <Grid container container direction="row" alignItems="baseline" justify="flex-start" spacing={5} >
            <Grid item xs={3}>
              <a href="http://localhost:3000/form"> <img  className="big" src={big} alt="big" /> </a>
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


{ }

{/* <Grid container container direction="row" alignItems="center" justify="space-between" >
<Grid item xs={6}>
  <img className="small" src={small} alt="small" href="http://localhost:3000/form" />
</Grid>
<Grid item xs={6}>
  <img className="small" src={small} alt="small" href="http://localhost:3000/form" />
</Grid> */}
export default main; 