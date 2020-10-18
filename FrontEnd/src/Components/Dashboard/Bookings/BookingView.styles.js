import styled from 'styled-components';

const month = {
  padding: '30px 0px',
  width: '100%',
  background: 'white',
  textAlign: 'center',
  border: 'solid',
  borderWidth: 'thin',
  borderRadius: '5px',
  borderColor: '#5ac490',
  fontSize:'20px',
  fontWight:'Bold',
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
  margin: '10',
  padding: '30px 0',
  backgroundColor: 'white',
  border: 'solid',
  borderWidth: 'thin',
  borderRadius: '5px',
  borderColor: '#5ac490',
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
