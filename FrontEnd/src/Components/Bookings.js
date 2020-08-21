import { Button } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';

const BackButton = withStyles((theme) => ({
  root: {
    margin: '24px 0px 0px 0px',
    color: theme.palette.getContrastText('#000000'),
    backgroundColor: '#5ac490',

    '&:hover': {
      backgroundColor: '#369668',
    },
  },
}))(Button);

export { BackButton };
