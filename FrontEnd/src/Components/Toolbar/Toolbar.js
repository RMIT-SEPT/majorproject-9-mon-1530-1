import React, { useState } from 'react';
import {
  StyledNavBlack,
  StyledNavBar,
  StyledLogoLink,
  GreenNavLink,
  RightFlexElements,
} from '../Navigation/Nav';
import logo from '../../media/logo.png';
import login from '../../media/login.png';
import small from '../../media/small.png';
import RightNavElement from '../Navigation/RightNavElemnet';
import axios from 'axios';
import { useQuery } from 'react-query';
// this is the toolbar for the guest user
// items are allocated evenly using a Grid function in material ui library
// we use normal routing in order to move between pages

const Toolbar = ({ id }) => {
  const [main, setMain] = useState(false);
  const [worker, setWorker] = useState(false);
  const token = localStorage.getItem('token');
  const fetchAdminData = async (key, id) => {
    const { data } = await axios
      .get(`http://localhost:8083/users/username?username=${id}`, {
        headers: {
          'Authorization': `${token}`,
          'username': `${id}`
        }
      })
      .then((res) => res)
      .then(setWorker(true))
      .catch((error) => {
        setMain(true);
        setWorker(false);
        throw error;
      });

    return data;
  };
  useQuery(['adminData', id], fetchAdminData, {
    onSuccess: (data) => {
      setUserName(data.name);
      setRole(data.userType);
    },
  });
  const [userName, setUserName] = useState();
  const [role, setRole] = useState();
  return (
    <StyledNavBlack>
      <StyledNavBar>
        <StyledLogoLink href="/">
          <img src={logo} alt="logo" />
        </StyledLogoLink>
        <a href="/contactus" style={{ textDecoration: 'none' }}>
          <GreenNavLink>Contact-us</GreenNavLink>
        </a>
        <a href="/about" style={{ textDecoration: 'none' }}>
          <GreenNavLink>About</GreenNavLink>
        </a>
        {main && (
          <RightFlexElements>
            <GreenNavLink>
              <a href="/login">
                <img className="login" src={login} alt="login" />{' '}
              </a>
            </GreenNavLink>
            <GreenNavLink>
              <a href="/form">
                <img className="small" src={small} alt="small" />{' '}
              </a>
            </GreenNavLink>
          </RightFlexElements>
        )}
        {worker && (
          <StyledNavBlack>
            <StyledNavBar>
              {role === 'user'
                ? <a
                  href="http://localhost:3000/user"
                  style={{ textDecoration: 'none' }}
                >
                  <GreenNavLink>Dashboard</GreenNavLink>
                </a>
                : <a
                  href="http://localhost:3000/admin"
                  style={{ textDecoration: 'none' }}
                >
                  <GreenNavLink>Dashboard</GreenNavLink>
                </a>
              }
              <RightNavElement userName={userName}
                role={role}></RightNavElement>
            </StyledNavBar>
          </StyledNavBlack>
        )}
      </StyledNavBar>
    </StyledNavBlack>
  );
};
Toolbar.defaultProps = {
  id: localStorage.getItem('username'),
};
export default Toolbar;
