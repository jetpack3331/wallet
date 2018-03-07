import Color from 'color';
import { createMuiTheme } from 'material-ui/styles';

const spacing = 8;

const hutOrange = '#fd562b';
const white = '#ffffff';
const black = '#000000';

export default createMuiTheme({
  palette: {
    background: hutOrange,
    type: 'dark',
    primary: {
      main: white,
      contrastText: Color(white).darken(0.15).hex(),
    },
    error: {
      main: black,
    },
  },
  spacing: {
    unit: spacing,
  },
  overrides: {
    MuiButton: {
      root: {
        marginLeft: spacing,
        '&:nth-child(1)': {
          marginLeft: 0,
        },
      },
    },
    MuiChip: {
      root: {
        marginLeft: spacing / 2,
        '&:nth-child(1)': {
          marginLeft: 0,
        },
      },
    },
    MuiAppBar: {
      colorPrimary: {
        backgroundColor: hutOrange,
        color: white,
      },
    },
  },
});
