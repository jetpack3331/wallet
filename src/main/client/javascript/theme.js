import Color from 'color';
import { createMuiTheme } from 'material-ui/styles';

const spacing = 8;

export default createMuiTheme({
  palette: {
    primary: {
      main: Color('#8bd1cc').darken(0.15).hex(),
      contrastText: '#ffffff',
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
        backgroundColor: 'rgba(0, 0, 0, 0.87)',
        color: '#FFFFFF',
      },
    },
  },
});
