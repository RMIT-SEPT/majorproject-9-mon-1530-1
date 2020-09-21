import React from 'react';
import { Redirect, Route } from 'react-router-dom';

export const ProtectedRoute = ({ component: Component, ...rest }) => {
  const isAuth = localStorage.getItem('isAuth');
  return (
    <Route
      {...rest}
      render={(props) =>
        isAuth ? (
          <Redirect
            to={{
              pathname: '/authorized',
              state: {
                from: props.location,
              },
            }}
          />
        ) : (
            <Redirect
              to={{
                pathname: '/unauthorized',
                state: {
                  from: props.location,
                },
              }}
            />
          )
      }
    />
  );
};
