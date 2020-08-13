import React, { useState } from 'react'
import styled from 'styled-components';
import { Button, Grid, InputLabel, Box, FormControl, Select } from '@material-ui/core';
import DateTimePicker from 'react-datetime-picker';
import { withStyles } from "@material-ui/core/styles";

const defaultProps = {
    borderColor: '#5AC490',
    m: 5,
    border: 3,
    style: { width: '95%', height: '16rem' },
};

const MidFont = styled.div`
    font-family: Nunito Sans;
    font-style: normal;
    font-weight: bold;
    font-size: 40px;
    line-height: 95px;
    letter-spacing: -0.05em;
    color: #FFFFFF;
    margin: 1%;
    margin-left: 10%;
  
  `
const Input = styled(InputLabel)`
    font-family:'Nunito sans';
    margin-left: 5%;
    color:'#5AC490';
    width: 300%;
    font-size: 40px;
  `
const ElementSelect = styled(Select)`
  
    font-family:'Nunito sans';
    border-radius: 3px;
    background: White;
    width: 300%;
    margin-left: 50%;
  `


const DatePicker = styled(DateTimePicker)`
   .react-datetime-picker__wrapper{
    background-color:white!important;
    width:200%;
    height: 60px;
    border-radius: 8px;
    padding:8%
    
   
  }
  .react-datetime-picker__inputGroup__divider {
      padding: 1px ;
      white-space: pre;
      color: black;
      
  }
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

function Find(props) {
    const [date, setDate] = useState(new Date())

    const onChange = date => setDate(date)
    return (

        <Box display="flex" justifyContent="center">
            <Box borderRadius={70} {...defaultProps}>
                <Grid container direction="row" alignItems="flex-end" justify="space-between" spacing={5} >
                    <Grid item xs={6}>
                        <MidFont>Choose a service</MidFont>
                    </Grid>
                    <Grid item xs={5}>
                        <div>
                            <DatePicker
                                onChange={onChange}
                                value={date}
                            />
                        </div>
                    </Grid>
                    <Grid item xs={6}>
                        <FormControl variant="outlined">
                            <Input htmlFor="grouped-native-select"></Input>
                            <ElementSelect native defaultValue="" id="grouped-native-select">Choose a service<option value={0}>Choose a service</option>
                                <optgroup label="Mark's cleaning">
                                    <option value={1}>Mark</option>
                                    <option value={2}>Sandra</option>
                                </optgroup>
                                <optgroup label="Category 2">
                                    <option value={3}>Option 3</option>
                                    <option value={4}>Option 4</option>
                                </optgroup>
                            </ElementSelect>
                        </FormControl>
                    </Grid>
                    <Grid item xs={5}>
                        <ColorButton variant="contained" color="#ffffff" > Submit</ColorButton>
                    </Grid>
                </Grid>
            </Box>

        </Box>

    )
}
export default Find;


