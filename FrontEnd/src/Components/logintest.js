import React from 'react'
import auth from '../Components/auth.js'
import { Link } from 'react-router-dom';



export const LandingPage = (props) => {
    return (
        <div>
            <h1>Landing</h1>
            <p><Link to='/out'>View Dashboard</Link></p>
            <p>Logged in status: {props.user}</p>
            <button onClick={props.handleLogin}>Log In</button>
        </div>
    );
};
export default LandingPage; 