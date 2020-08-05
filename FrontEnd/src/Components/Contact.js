import React from 'react'
import { Button, Grid } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import logo from '../media/logo.png';
import construction from '../media/contact.png';
import { withStyles } from "@material-ui/core/styles";
import styled from 'styled-components';

const Heading = styled.div`
  font-family: Nunito Sans;
  font-style: normal;
  font-weight: bold;
  font-size: 20px;
  line-height: 34px;
  letter-spacing: -0.05em;
  color: #000000;
  margin: 10px;
`
const Bold = styled.div`
  font-family: Nunito Sans;
  font-style: normal;
  font-weight: 800;
  font-size: 35px;
  line-height: 48px;
  letter-spacing: -0.05em;
  color: #000000;
`
const TopRight = styled.div`
  font-family: Nunito Sans;
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
  height:100vh;
  padding-top: 3%;
  /* flex-grow: 1 ; */
  background-color: black;
  color:white;
`

const Logo = styled.img`
  width: 40%;
  height: auto;
  background-repeat: no-repeat;
  background-size: contain;
  /* padding-left: 10%; */
`


const ColorButton = withStyles((theme) => ({
  root: {
    margin: '4px',
    color: theme.palette.getContrastText('#000000'),
    backgroundColor: '#5AC490',
    '&:hover': {
      backgroundColor: '#60BF90',
    },
  },
}))(Button);

const CssTextFieldGreen = withStyles({
  root: {
    "& label.Mui-focused": {
      color: "green"
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: "green"
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "green"
      },
      "&:hover fieldset": {
        borderColor: "light green"
      },
      "&.Mui-focused fieldset": {
        borderColor: "green"
      }
    }
  }
})(TextField);

const contact = (props) => {
  return (
    <Grid container alignItems="center" justify="center" spacing={0}>
      <Grid item xs={7} >
        <Left>
          <Grid container direction="column" justify="center" alignItems="center">
            <Grid item xs={12}>
              <a href="http://localhost:3000/main"> <Logo src={logo} alt="logo" /> </a>
              <Grid item xs={12}>
                <Construction src={construction} alt="contact" />
              </Grid>
            </Grid>
          </Grid>
        </Left>
      </Grid>

      <Grid item xs={5} >
        <Right>
          <TopRight>Not a member? <a href="http://localhost:3000/form">Sign up</a> </TopRight>
          <Grid container direction="row" container spacing={1}>
            <Grid item xs={12}>
              <Bold>Send us a message </Bold>
            </Grid>
            <Grid item xs={12}>
              <Heading>Name </Heading>
              <CssTextFieldGreen id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                InputLabelProps={{ shrink: true, }} variant="outlined" />
            </Grid>
            <Grid item xs={12}>
              <Heading>Email </Heading>
              <CssTextFieldGreen id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                InputLabelProps={{ shrink: true, }} variant="outlined" />
            </Grid>
            <Grid item xs={12}>
              <Heading>Phone </Heading>
              <CssTextFieldGreen id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                InputLabelProps={{ shrink: true, }} variant="outlined" />
            </Grid>
            <Grid item xs={12}>
              <Heading>Message</Heading>
              <CssTextFieldGreen id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                InputLabelProps={{ shrink: true, }} variant="outlined" />
            </Grid>
            <Grid item xs={12}>
              <ColorButton variant="contained" color="#ffffff" > Submit</ColorButton>
            </Grid>
          </Grid>
        </Right>
      </Grid>
    </Grid>
  )
}

export default contact; 