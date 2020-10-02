import React, { useState } from "react";
import { useQuery } from 'react-query';
import axios from 'axios';
import { Grid, Button } from "@material-ui/core";
import TextField from "@material-ui/core/TextField";
import { useForm } from "react-hook-form";
import {
    SubHeading
} from '../Components/Dashboard/DashboardComponents';

export const UserSettings = ({ id }) => {

    const fetchUserData = async (key, id) => {
        const { data } = await axios
          .get(`http://localhost:8083/users?username=${id}`)
          .catch((error) => {
            console.log('Error fetching user data: ' + error);
          });
    
        return data;
    };

    const [userName, setUserName] = useState();

    const { isSuccess, isLoading, isError } = useQuery(
        ['userData', id],
        fetchUserData,
        {
          onSuccess: (data) => {
            setUserName(data.name);
          },
        }
      );

    const {
        register: checkName,
        errors: nameErrors,
        handleSubmit: handleSubmitName,
        reset: resetName
    } = useForm();

    const {
        register: checkPassword,
        errors: passwordErrors,
        handleSubmit: handleSubmitPassword,
        reset: resetPassword
    } = useForm();

    const [nameError, setNameErrors] = useState("");
    const [passwordError, setPasswordError] = useState("");


    const onSubmitNameChange = async (values) => {
        //work here required to implement getting user and using patch for user
        console.log(nameErrors);
        setPasswordError("");
        var params = {
            name: values.name
        }
        const { postNameData } = await axios.patch(`http://localhost:8083/users/?username=${id}`, params)
            .catch(function (error) {
                if (error.response)
                {
                    console.log(error.response.data);
                    console.log(error.response.status);
                    console.log(error.response.headers);
                } else if (error.request) {
                    console.log(error.request);
                } else {
                    console.log(error.message);
                }
                console.log(error.config);
        });
        setNameErrors("name change accepted");
        handleSubmitName();
    }

    const onSubmitPasswordChange = async (values) => {
        console.log(passwordErrors);
        if (values.RepeatNewPassword === values.NewPassword)
        {
            setPasswordError("");
            var params = {
                oldPassword: values.OldPassword,
                newPassword: values.NewPassword
            }
            const { postPassowrdData } = await axios.post(`http://localhost:8083/users/password/?username=${id}`, params)
                                    .catch (function (error) {
                                        if (error.response)
                                        {
                                            console.log(error.response.data);
                                            console.log(error.response.status);
                                            console.log(error.response.headers);
                                        } else if (error.request) {
                                            console.log(error.request);
                                        } else {
                                            console.log(error.message);
                                        }
                                        console.log(error.config);
                                    })
            handleSubmitPassword();
        }
        else
        {
            setPasswordError("passwords don't match");
            resetPassword();
        }
    }

    return (
        <>
            {isLoading && <div>Loading...</div>}
            {isError && <div>Error...</div>}
            {isSuccess && (
            <>
                <SubHeading>Profile Infomormation</SubHeading>
                <form key={1} style={{width: '840px'}} onSubmit={handleSubmitName(onSubmitNameChange)}>
                    <Grid container direction="column" spacing={3}>
                        <h2>Full Name</h2>
                        <TextField
                            //value={userName}
                            name="name"
                            inputRef={checkName({
                                required: "Name Required",
                                maxLength: {
                                    value: 50,
                                    message: "Name must be less than 50 characters ",
                                },
                                minLength: {
                                    value: 1,
                                    message: "Name must be more than 1 character ",
                                },
                                pattern: {
                                    value: /^[A-Za-z ,.'-]+$/,
                                    message: "Name must contain letters from the alphabet ",
                                },
                            })}
                            id="outlined-full-width"
                            style={{ margin: 8 }}
                            helperText="Full width!"
                            fullWidth
                            margin="normal"
                            InputLabelProps={{ shrink: true }}
                            variant="outlined"
                        />
                        <Grid item xs={12}>
                            <Button onClick={handleSubmitName(onSubmitNameChange)} variant="contained" color="default">
                                {" "}
                                Update
                            </Button>
                            {nameError}
                        </Grid>
                    </Grid>
                </form>
                <form key={2} style={{width: '840px'}} onSubmit={handleSubmitName(onSubmitPasswordChange)}>
                    <Grid container direction="column" spacing={1}>
                    <h2>Old Password</h2>
                    <TextField
                            name="OldPassword"
                            inputRef={checkPassword({
                                required: "old password Required",
                                maxLength: {
                                    value: 50,
                                    message: "password must be less than 60 char ",
                                },
                                minLength: {
                                    value: 5,
                                    message: "number must be more than 5 char ",
                                },
                            })}
                            type="password"
                            id="outlined-full-width"
                            style={{ margin: 8 }}
                            helperText="Full width!"
                            fullWidth
                            margin="normal"
                            InputLabelProps={{ shrink: true }}
                            variant="outlined"
                        />
                        <h2>New Password</h2>
                        <TextField
                            name="NewPassword"
                            inputRef={checkPassword({
                                required: "new password Required",
                                maxLength: {
                                    value: 50,
                                    message: "password must be less than 60 char ",
                                },
                                minLength: {
                                    value: 5,
                                    message: "number must be more than 5 char ",
                                },
                            })}
                            type="password"
                            id="outlined-full-width"
                            style={{ margin: 8 }}
                            helperText="Full width!"
                            fullWidth
                            margin="normal"
                            InputLabelProps={{ shrink: true }}
                            variant="outlined"
                        />
                        <h2>Repeat New Password</h2>
                        <TextField
                            name="RepeatNewPassword"
                            inputRef={checkPassword({
                                required: "repeat of new password Required",
                                maxLength: {
                                    value: 50,
                                    message: "password must be less than 60 char ",
                                },
                                minLength: {
                                    value: 5,
                                    message: "number must be more than 5 char ",
                                },
                            })}
                            type="password"
                            id="outlined-full-width"
                            style={{ margin: 8 }}
                            helperText="Full width!"
                            fullWidth
                            margin="normal"
                            InputLabelProps={{ shrink: true }}
                            variant="outlined"
                        />
                        <Grid item xs={12}>
                            <Button onClick={handleSubmitPassword(onSubmitPasswordChange)} variant="contained" color="default">
                                {" "}
                                Update
                            </Button>
                            {passwordError}
                        </Grid>
                    </Grid>
                </form>
            </>
            )}
        </>
    )
}

export default UserSettings;
