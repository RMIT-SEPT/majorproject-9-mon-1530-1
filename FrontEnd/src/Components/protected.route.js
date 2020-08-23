import React, { Component } from 'react'
import { Route } from 'react-router-dom'

export const ProtectedRoute = ({ component: Component, ...rest }) => {
    return (
        <Route
            {...rest}
            render={props => {
                return <Component {...props} />
            }} />
    );
};