import React from 'react'
import { Grid, Button } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import { withStyles } from "@material-ui/core/styles";
import styled from 'styled-components';
import { useForm } from 'react-hook-form';




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

const Heading = styled.div`
  font-style: normal;
  font-weight: bold;
  font-size: 20px;
  line-height: 34px;
  letter-spacing: -0.05em;
  color: #000000;
  margin: 10px;
`
const ColorButton = withStyles((theme) => ({
    root: {
        margin: '10px',
        color: theme.palette.getContrastText('#000000'),
        backgroundColor: '#5AC490',
        '&:hover': {
            backgroundColor: '#60BF90',
        },
    },
}))(Button);

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

// this is the signup page, 
// items are allocated evenly using a Grid function in material ui library 
// we use normal routing in order to move between pages 

function FormDetails() {
    const { register, handleSubmit } = useForm()
    return (
        <form onSubmit={handleSubmit((data) => alert(JSON.stringify(data)))}>
            <Grid container direction="row" spacing={3}>
                <Grid item xs={6}>
                    <Heading>Full Name  </Heading>
                    <CssTextFieldGreen required name="name"
                        inputRef={
                            register({
                                required: 'First Name Required',
                                maxLength: {
                                    value: 50,
                                    message: 'fist name must be less than 50 characters '
                                },
                                minLength: {
                                    value: 1,
                                    message: 'fist name must be more than 1 character '
                                },
                                pattern: {
                                    value: /^[A-Za-z ,.'-]+$/,
                                    message: 'fist name must be only alphabet '
                                }

                            })
                        } id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                        InputLabelProps={{ shrink: true, }} variant="outlined" />
                </Grid>
                <Grid item xs={6}>
                    <Heading>Username  </Heading>
                    <CssTextFieldGreen required name="userName"
                        inputRef={
                            register({
                                required: 'userName Required',
                                maxLength: {
                                    value: 10,
                                    message: 'userName must be less than 10 characters '
                                },
                                minLength: {
                                    value: 2,
                                    message: 'fist name must be more than 2 character '
                                }

                            })
                        } id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                        InputLabelProps={{ shrink: true, }} variant="outlined" />
                </Grid>

                <Grid item xs={12}>
                    <Heading>Full Address </Heading>
                    <CssTextFieldGreen required name="address"
                        inputRef={
                            register({
                                required: 'address Required',
                                maxLength: {
                                    value: 250,
                                    message: 'address must be less than 250 characters '
                                },
                                minLength: {
                                    value: 5,
                                    message: 'address must be more than 5 character '
                                },
                                pattern: {
                                    value: /^\d+\s[A-z]+\s[A-z]+/,
                                    message: 'fist name must be only alphabet or numbers '
                                }

                            })
                        } id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                        InputLabelProps={{ shrink: true, }} variant="outlined" />
                </Grid>

                <Grid item xs={12}>
                    <Heading>Contact Number </Heading>
                    <CssTextFieldGreen required name="number"
                        inputRef={
                            register({
                                required: 'number Required',
                                maxLength: {
                                    value: 17,
                                    message: 'number must be less than 16 number '
                                },
                                minLength: {
                                    value: 10,
                                    message: 'number must be more than 10 numbers '
                                },
                                pattern: {
                                    value: /^(?:\+?(61))? ?(?:\((?=.*\)))?(0?[2-57-8])\)? ?(\d\d(?:[- ](?=\d{3})|(?!\d\d[- ]?\d[- ]))\d\d[- ]?\d[- ]?\d{3})$/,
                                    message: 'number must be only alphabet '
                                }

                            })
                        } id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                        InputLabelProps={{ shrink: true, }} variant="outlined" />
                </Grid>

                <Grid item xs={12}>
                    <Heading>Password</Heading>
                    <CssTextField type="password" required name="password"
                        inputRef={
                            register({
                                required: 'password Required',
                                maxLength: {
                                    value: 50,
                                    message: 'password must be less than 60 char '
                                },
                                minLength: {
                                    value: 5,
                                    message: 'number must be more than 5 char '
                                }

                            })
                        } id="outlined-full-width" style={{ margin: 8 }} helperText="Full width!" fullWidth margin="normal"
                        InputLabelProps={{ shrink: true, }} variant="outlined" />
                </Grid>
                {/* submit button  */}
                <Grid item xs={12}>
                    <ColorButton type="submit" variant="contained"> Submit</ColorButton>
                </Grid>
            </Grid>
        </form>
    )
}

export default FormDetails; 