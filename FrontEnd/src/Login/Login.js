import React from 'react'
import './Login.css'
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import logo from '../media/logo.png';
import construction from '../media/undraw_under_construction_46pa-2 1.png';
import { withStyles } from "@material-ui/core/styles";

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

const login = (props) => {
  return (
    <Grid container className="mainGrid" alignItems="center" justify="center" spacing={0}>

      <Grid item xs={6} >
        <div className="left">
          <Grid container direction="column" justify="center" alignItems="center">
            <Grid item xs={12}>
              <a href="http://localhost:3000/main"> <img className="logo" src={logo} alt="logo" /> </a>
              <Grid item xs={12}>
                <img className="construction" src={construction} alt="construction" />
              </Grid>
            </Grid>
          </Grid>
        </div>
      </Grid>

      <Grid item xs={6} >
        <div className='right'>
          <div className='topright'>
            Not a member? <a href="http://localhost:3000/form">Sign up</a>
          </div>
          <Grid container direction="row">
            <Grid container spacing={1}>
              <Grid item xs={12}>
                <div className='bold'></div>
              </Grid>
              <Grid item xs={12}>
                <div className='bold'>Sign Up to Agem</div>
              </Grid>
              <Grid item xs={12}>
                <div className='heading'>UserName </div>
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
              <div className='heading'>Password</div>
              <CssTextField
                id="outlined-full-width"
                type="password"
                placeholder="6+ characters  "
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
          </Grid>
        </div>
    </Grid>
    </Grid>
  )
}

export default login; 