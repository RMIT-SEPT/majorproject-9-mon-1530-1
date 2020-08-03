import React from 'react'
import styled from 'styled-components';
import { Button, Grid } from '@material-ui/core';
import logo from '../media/logo.png';
import login from '../media/login.png';
import small from '../media/small.png';


const GreenButton = styled(Button)`
.MuiButton-label{
  font-family:'Nunito sans';
  color:#5AC490;
font-style: normal;
 font-weight: bold;
font-size: 30px;
line-height: 41px;
letter-spacing: -0.05em;
}
`
const Toolbar = styled.div`
    height: 10vh;
    flex-grow: 1;
    background-color: black;
    margin-top:1%;
  `
  
const toolbar = (props) => {
    return (
        <Toolbar>
            <Grid container container direction="row" alignItems="flex-start" justify="space-between" >
                <Grid item xs={2}>
                    <a href="http://localhost:3000/main"> <img className="logo" src={logo} alt="logo" /> </a>
                </Grid>
                <Grid item xs={2}>
                    <a href="http://localhost:3000/contactus" style={{ textDecoration: 'none' }}>
                        <GreenButton variant="text" >Contact-us</GreenButton>
                    </a>
                </Grid>
                <Grid item xs={2}>
                <a href="http://localhost:3000/about" style={{ textDecoration: 'none' }}>
                        <GreenButton variant="text" >About-us</GreenButton>
                    </a>
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
            </Toolbar>
    )
}
export default toolbar; 