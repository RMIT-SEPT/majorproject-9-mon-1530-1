import React from 'react'
import './Toolbar.css'
import styled from 'styled-components';
import { Button, Grid } from '@material-ui/core';
import logo from '../media/logo.png';
import big from '../media/big.png';
import book from '../media/book.png';
import login from '../media/login.png';
import hairdres from '../media/hairdres.png';
import small from '../media/small.png';
import construction from '../media/undraw_under_construction_46pa-2 1.png';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

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
const toolbar = (props) => {
    return (
        <div className='toolBar'>
            <Grid container container direction="row" alignItems="flex-start" justify="space-between" >
                <Grid item xs={2}>
                    <a href="http://localhost:3000/main"> <img className="logo" src={logo} alt="logo" /> </a>
                </Grid>
                <Grid item xs={2}>
                    <Link to="about" style={{ textDecoration: 'none' }}>
                        <GreenButton variant="text" >About Agem</GreenButton>
                    </Link>
                </Grid>
                <Grid item xs={2}>
                <Link to="contactus" style={{ textDecoration: 'none' }}>
                <GreenButton variant="text" >Contact-us</GreenButton>
                    </Link>
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
    )
}
export default toolbar; 