import React from 'react'
import './Contact.css'
import { Button, Grid } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import logo from '../media/logo.png';
import construction from '../media/contact.png';
import { withStyles } from "@material-ui/core/styles";

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

const CssTextField = withStyles({
  root: {
    "& label.Mui-focused": {
      color: "green"
    },
    "& .MuiInput-underline:after": {
      borderBottomColor: "green"
    },
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "red"
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
    <Grid container className="mainGrid" alignItems="center" justify="center" spacing={0}>

      <Grid item xs={7} >
        <div className="left">
          <Grid container direction="column" justify="center" alignItems="center">
            <Grid item xs={12}>
              <a href="http://localhost:3000/main"> <img className="logo" src={logo} alt="logo" /> </a>
              <Grid item xs={12}>
                <img className="construction" src={construction} alt="contact" />
              </Grid>
            </Grid>
          </Grid>
        </div>
      </Grid>

      <Grid item xs={5} >
        <div className='right'>
          <div className='topright'>
            Not a member? <a href="http://localhost:3000/form">Sign up</a>
          </div>
          <Grid container direction="row" container spacing={1}>
              <Grid item xs={12}>
                <div className='bold'></div>
              </Grid>
              <Grid item xs={12}>
                <div className='bold'>Send us a message </div>
              </Grid>
              <Grid item xs={12}>
                <div className='heading'>Name </div>
                <CssTextFieldGreen
                  id="outlined-full-width"
                  // label="Surname"
                  style={{ margin: 8 }}
                  helperText="Full width!"
                  fullWidth
                  margin="normal"
                  InputLabelProps={{
                    shrink: true,
                  }}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12}>
                <div className='heading'>Email </div>
                <CssTextFieldGreen
                  id="outlined-full-width"
                  // label="Surname"
                  style={{ margin: 8 }}
                  helperText="Full width!"
                  fullWidth
                  margin="normal"
                  InputLabelProps={{
                    shrink: true,
                  }}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12}>
                <div className='heading'>Phone </div>
                <CssTextFieldGreen
                  id="outlined-full-width"
                  // label="Surname"
                  style={{ margin: 8 }}
                  helperText="Full width!"
                  fullWidth
                  margin="normal"
                  InputLabelProps={{
                    shrink: true,
                  }}
                  variant="outlined"
                />
              </Grid>
              <div className='heading'>Message</div>
                <CssTextFieldGreen
                id="outlined-full-width"
                // label="Surname"
                style={{ margin: 8 }}
                helperText="Full width!"
                fullWidth
                margin="normal"
                InputLabelProps={{
                  shrink: true,
                }}
                variant="outlined"
              />
            </Grid>
            <Grid item xs={12}>
              <ColorButton variant="contained" color="#ffffff" > Submit</ColorButton>
            </Grid>
        </div>
      </Grid>
    </Grid>
  )
}

export default contact; 