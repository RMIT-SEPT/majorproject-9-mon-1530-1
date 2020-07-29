import React from 'react'
import './Form.css'
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import logo from '../media/logo.png';
import construction from '../media/undraw_under_construction_46pa-2 1.png';


const form = (props) => {
  return (
    // <Grid conclassName='root'>
    <Grid container className="mainGrid" alignItems="center" justify="center" spacing={0}>
     
      <Grid item xs={6} >
        <div className="left">
        <Grid container direction="column" justify="center" alignItems="center">
        <Grid item xs={12}>
            <img className="logo" src={logo} alt="Logo" />
            <Grid item xs={12}>
            <img className="construction" src={construction} alt="construction" />
            </Grid>
            </Grid>
        </Grid>
        </div>
      </Grid>

      <Grid item xs={6} >
        <div className='right'>
        <div className = 'topright'>
          Already a memeber? <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ">Sign in</a> 
        </div>

        <Grid container direction="row">
        <Grid container spacing={1}>
       <Grid item xs={12}>
          <div className='bold'></div>
        </Grid>
        <Grid item xs={12}>
          <div className='bold'>Sign Up to Agem</div>
        </Grid>
        <Grid item xs={6}>
        <div className='heading'>First Name </div>
        <TextField
          id="outlined-full-width"
          // label="First name"
          style={{ margin: 8 }}
          // placeholder="Placeholder"
          helperText="Full width!"
          fullWidth
          margin="normal"
          InputLabelProps={{
            shrink: true,
          }}
          variant="outlined"
        />
        </Grid>
        <Grid item xs={6}>
        <div className='heading'>Surname </div>
        <TextField
          id="outlined-full-width"
          // label="Surname"
          style={{ margin:8 }}
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
        <div className='heading'>Full address </div>
        <TextField
          id="outlined-full-width"
          // label="Address"
          style={{ margin:8 }}
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
        <div className='heading'>Contact number</div>
        <TextField
          id="outlined-full-width"
          // label="Contact numbers"
          style={{ margin:8 }}
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
        <div className='heading'>Password</div>
        <TextField
          id="outlined-full-width"
          // label="Password"
          type="password"
          placeholder="6+ characters  "
          style={{ margin:8 }}
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
           </Grid>
        </div>
      </Grid>
    </Grid>
    // </div>
  )
}

export default form; 