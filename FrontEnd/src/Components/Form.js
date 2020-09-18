import React from 'react'
import { Grid } from '@material-ui/core';
import logo from '../media/logo.png';
import construction from '../media/undraw_under_construction_46pa-2 1.png';
import styled from 'styled-components';
import FormDetails from './FormDetails';




const Bold = styled.div`
  font-family: 'Nunito Sans';
  font-style: normal;
  font-weight: 800;
  font-size: 35px;
  line-height: 48px;
  letter-spacing: -0.05em;
  color: #000000;
`
const TopRight = styled.div`
  font-family: 'Nunito Sans';
  font-style: normal;
  font-weight: 600;
  font-size: 20px;
  line-height: 27px;
  letter-spacing: -0.05em;
  position: absolute;
  top: 2%;
  right: 5%;
  padding-top: 2%;
`
const Right = styled.div`
  padding: 10px; 
  margin: 80px;
  flex-grow: 1;
  color : black;
  background-color: white;
`
const Construction = styled.img`
  width: 100%;
  height: auto;
  padding-top: 10%;
  background-repeat: no-repeat;
  background-size: contain;
`
const Left = styled.div`
  height:120vh;
  padding-top: 3%;
  /* flex-grow: 1 ; */
  background-color: black;
  color:white;
`

const Logo = styled.img`
  background-repeat: no-repeat;
  background-size: contain;
  /* padding-left: 10%; */
`


// this is the signup page, 
// items are allocated evenly using a Grid function in material ui library 
// we use normal routing in order to move between pages 

function Form() {
  return (
    <form >
      <Grid container alignItems="center" justify="center" spacing={0}>
        <Grid item xs={7} >
          {/* the logo and the img on the left  */}
          <Left>
            <Grid container direction="column" justify="center" alignItems="center">
              <Grid item xs={12}>
                <a href="http://localhost:3000/"> <Logo src={logo} alt="logo" /> </a>
                <Grid item xs={12}>
                  <Construction src={construction} alt="contact" />
                </Grid>
              </Grid>
            </Grid>
          </Left>
        </Grid>

        <Grid item xs={5} >
          <Right>
            {/* a link to the log in page  */}
            <TopRight>Already  a member? <a href="http://localhost:3000/login"> Log-in</a> </TopRight>
            <Grid container spacing={1}>
              {/* the form to login  */}
              <Grid item xs={12}>
                <Bold> Sign Up to Agem </Bold>
              </Grid>
              <FormDetails></FormDetails>
              </Grid>
          </Right>
        </Grid>
      </Grid>
      </form>
  )
}

export default Form; 