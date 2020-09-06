import React from 'react'
import auth from './auth.js'

export const Dash = (props) => {
    return (<div>
        <h1> logout page </h1>
        <button onClick={props.handleLogout}>Log Out</button>
    </div>
    );
};
export default Dash; 