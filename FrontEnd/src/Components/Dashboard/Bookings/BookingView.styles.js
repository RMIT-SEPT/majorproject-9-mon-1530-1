import styled from 'styled-components';

const month = {
  padding: '30px 0px',
  width: '100%',
  background: '#5ac490',
  textAlign: 'center',
};
const prev = {
  float: 'left',
  paddingLeft: '10px',
  paddingTop: '5px',
};

const next = {
  float: 'right',
  paddingRight: '10px',
  paddingTop: '5px',
};

const weekdays = {
  margin: '0',
  padding: '10px 0',
  backgroundColor: '#ddd',
};

const day = {
  display: 'inline-block',
  width: '13.6%',
  color: '#666',
  textAlign: 'center',
};

const StyledList = styled.ul`
  margin: 0px;
  padding: 0px 20px;
  list-style-type: none;
`;

export { month, prev, next, weekdays, day, StyledList };
