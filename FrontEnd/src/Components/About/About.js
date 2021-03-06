import React from 'react';
import styled from 'styled-components';
import { Grid } from '@material-ui/core';
import doctor from '../../media/doc.png';
import gym from '../../media/gym.svg';
import TextLoop from 'react-text-loop';
import Toolbar from '../Toolbar/Toolbar';

const Doc = styled.img`
  width: 60%;
  height: auto;
  background-repeat: no-repeat;
  background-size: contain;
  padding-left: 5%;
`;
const Gym = styled.img`
  width: 80%;
  height: auto;
  background-repeat: no-repeat;
  background-size: contain;
`;
const Bold = styled.div`
  font-style: normal;
  font-weight: bold;
  font-size: 35px;
  line-height: 95px;
  letter-spacing: -0.05em;
  color: white;
  margin: 5%;
  white-space: 'pre-wrap';
`;

const MainWrapper = styled.div`
  background-color: black !important;
  height: 100vh;
  flex-grow: 1;
  color: white;
`;
const BigFont = styled.div`
  font-style: normal;
  font-weight: bold;
  font-size: 70px;
  line-height: 95px;
  letter-spacing: -0.05em;
  color: white;
  margin: 1%;
  margin-left: 5%;
`;
const SmallerFont = styled.div`
  font-style: normal;
  font-weight: bold;
  font-size: 26px;
  line-height: 35px;
  letter-spacing: -0.05em;
  color: white;
  margin: 1%;
  margin-left: 5%;
`;
//this is the about us  page, it contains a tool bar
// items are allocated evenly using a Grid function in material ui library
// we use normal routing in order to move between pages

const about = (props) => {
  document.body.style = 'background:black;';
  return (
    <MainWrapper>
      {/* this is the main grid that holds every element */}
      <Grid
        container
        direction="row"
        alignItems="center"
        justify="space-between"
        spacing={5}
      >
        <Grid item xs={12}>
          {/* made a toolbar component that i use throughout the project*/}
          <Toolbar />
        </Grid>
        <Grid item xs={5}>
          <BigFont>Find Best Sevices </BigFont>
          <BigFont>Near You</BigFont>
          <SmallerFont>
            {' '}
            All of our services are done by licenced experts in their fields.
          </SmallerFont>
          <SmallerFont>
            {' '}
            Our services{' '}
            <TextLoop interval={350}>
              <span> Barbers </span>
              <span> Nail technicians </span>
              <span> Dentists </span>
              <span> Gyms </span>
              <span> Fitness Coaches </span>
            </TextLoop>{' '}
            And Much more .
          </SmallerFont>
        </Grid>
        <Grid item xs={5} justify="flex-end">
          {/*this is the img of the gym*/}
          <Gym src={gym} alt="gym" />
        </Grid>
        <Grid item xs={6}>
          <Doc src={doctor} alt="doctor" />
        </Grid>
        <Grid item xs={6}>
          <Bold>Signup or login to discover more services</Bold>
        </Grid>
      </Grid>
    </MainWrapper>
  );
};

export default about;
