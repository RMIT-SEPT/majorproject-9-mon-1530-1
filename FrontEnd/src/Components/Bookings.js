import React from 'react';
import { DashboardWrapper, MenuBarComponent, MenuIcon } from './Dashboard';
import home from '../media/home-40px.svg';
import book from '../media/book-40px.svg';
import calendar from '../media/calendar-40px.svg';
import phone from '../media/phone-40px.svg';

const Bookings = () => {
  return (
    <DashboardWrapper userName="temp" role="temp">
      <MenuBarComponent>
        <MenuIcon src={home} alt="Home icon" />
        <MenuIcon src={book} alt="Book icon" />
        <MenuIcon src={phone} alt="Phone icon" />
        <MenuIcon src={calendar} alt="Calendar icon" />
      </MenuBarComponent>
      <div>test</div>
    </DashboardWrapper>
  );
};

export default Bookings;
