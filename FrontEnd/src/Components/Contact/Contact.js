import React from 'react';
import { Grid } from '@material-ui/core';
import logo from '../../media/logo.png';
import construction from '../../media/contact.png';
import styled from 'styled-components';

const Heading = styled.div`
  font-style: normal;
  font-weight: bold;
  font-size: 20px;
  line-height: 34px;
  letter-spacing: -0.05em;
  color: #000000;
  margin: 10px;
`;
const Bold = styled.div`
  font-style: normal;
  font-weight: 800;
  font-size: 35px;
  line-height: 48px;
  letter-spacing: -0.05em;
  color: #000000;
`;
const TopRight = styled.div`
  font-style: normal;
  font-weight: 600;
  font-size: 20px;
  line-height: 27px;
  letter-spacing: -0.05em;
  position: absolute;
  top: 2%;
  right: 5%;
  padding-top: 2%;
`;
const Right = styled.div`
  padding: 10px;
  margin: 80px;
  flex-grow: 1;
  color: black;
  background-color: white;
`;

const Construction = styled.img`
  width: 100%;
  height: auto;

  padding-top: 10%;
  background-repeat: no-repeat;
  background-size: contain;
`;
const Left = styled.div`
  height: 120vh;
  padding-top: 3%;
  /* flex-grow: 1 ; */
  background-color: black;
  color: white;
`;

const Logo = styled.img`
  background-repeat: no-repeat;
  background-size: contain;
  padding-top: 3%;
  /* padding-left: 10%; */
`;

// this is a conrtact us page where a user can send contact us and leaves a message
// items are allocated evenly using a Grid function in material ui library
// we use normal routing in order to move between pages

const contact = () => {
  return (
    // this is the main grid that holds every element
    <Grid container alignItems="flex-start" justify="center" spacing={0}>
      {/* the logo and the img on the left  */}
      <Grid item xs={7}>
        <Left>
          <Grid
            container
            direction="column"
            justify="center"
            alignItems="center"
          >
            <Grid item xs={12}>
              <a href="/">
                {' '}
                <Logo src={logo} alt="logo" />{' '}
              </a>
              <Grid item xs={12}>
                <Construction src={construction} alt="contact" />
              </Grid>
            </Grid>
          </Grid>
        </Left>
      </Grid>
      {/* a link to the signup page   */}
      <Grid item xs={5}>
        <Right>
          <TopRight>
            Not a member? <a href="/form">Sign up</a>{' '}
          </TopRight>

          <Grid container spacing={1}>
            <Grid item xs={12}>
              <Bold>Contact us </Bold>
            </Grid>
            {/* form to sign up   */}
            <Grid item xs={12}>
              <div>
                <Heading>Elizabeth Tawaf:</Heading>{' '}
                <span>s3723812@student.rmit.edu.au</span>
                <Heading>Brodey Yendall:</Heading>{' '}
                <span>s3718834@student.rmit.edu.au</span>
                <Heading>Richard Wang:</Heading>
                <span>s3720241@student.rmit.edu.au</span>
                <Heading>Abrar Alsagheer:</Heading>
                <span>s3707180@student.rmit.edu.au</span>
                <Heading>Lawrence Abdelmalek:</Heading>
                <span>s3656022@student.rmit.edu.au</span>
              </div>
            </Grid>
          </Grid>
        </Right>
      </Grid>
    </Grid>
  );
};

export default contact;
