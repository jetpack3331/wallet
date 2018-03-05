import * as _ from 'lodash';
import { FormControl, FormHelperText, Input, InputLabel, withStyles } from 'material-ui';
import * as PropTypes from 'prop-types';
import React from 'react';
import SelectWrapped from './ReactSelect';

const ITEM_HEIGHT = 48;

const styles = theme => ({
  chip: {
    margin: theme.spacing.unit / 4,
  },
  // We had to use a lot of global selectors in order to style react-select.
  // We are waiting on https://github.com/JedWatson/react-select/issues/1679
  // to provide a better implementation.
  // Also, we had to reset the default style injected by the library.
  '@global': {
    '.Select-control': {
      display: 'flex',
      alignItems: 'center',
      border: 0,
      height: 'auto',
      background: 'transparent',
      '&:hover': {
        boxShadow: 'none',
      },
    },
    '.Select-multi-value-wrapper': {
      flexGrow: 1,
      display: 'flex',
      flexWrap: 'wrap',
    },
    '.Select--multi .Select-input': {
      margin: 0,
    },
    '.Select.has-value.is-clearable.Select--single > .Select-control .Select-value': {
      padding: 0,
    },
    '.Select-noresults': {
      padding: theme.spacing.unit * 2,
    },
    '.Select-input': {
      display: 'inline-flex !important',
      padding: 0,
      height: 'auto',
    },
    '.Select-input input': {
      background: 'transparent',
      border: 0,
      padding: 0,
      cursor: 'default',
      display: 'inline-block',
      fontFamily: 'inherit',
      fontSize: 'inherit',
      margin: 0,
      outline: 0,
    },
    '.Select-placeholder, .Select--single .Select-value': {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      display: 'flex',
      alignItems: 'center',
      fontFamily: theme.typography.fontFamily,
      fontSize: theme.typography.pxToRem(16),
      padding: 0,
    },
    '.Select-placeholder': {
      opacity: 0.54,
      color: theme.palette.common.black,
    },
    '.Select-menu-outer': {
      backgroundColor: theme.palette.background.paper,
      boxShadow: theme.shadows[2],
      position: 'absolute',
      left: 0,
      top: `calc(100% + ${theme.spacing.unit}px)`,
      width: '100%',
      zIndex: 2,
      maxHeight: ITEM_HEIGHT * 4.5,
    },
    '.Select.is-focused:not(.is-open) > .Select-control': {
      boxShadow: 'none',
    },
    '.Select:not(.is-focused) .Select-placeholder': {
      display: 'none',
    },
    '.Select-menu': {
      maxHeight: ITEM_HEIGHT * 4.5,
      overflowY: 'auto',
    },
    '.Select-menu div': {
      boxSizing: 'content-box',
    },
    '.Select-arrow-zone, .Select-clear-zone': {
      color: theme.palette.action.active,
      cursor: 'pointer',
      height: 21,
      width: 21,
      zIndex: 1,
    },
    // Only for screen readers. We can't use display none.
    '.Select-aria-only': {
      position: 'absolute',
      overflow: 'hidden',
      clip: 'rect(0 0 0 0)',
      height: 1,
      width: 1,
      margin: -1,
    },
  },
  label: {},
  input: {},
  helper: {},
  root: {},
});

// eslint-disable-next-line react/prefer-stateless-function
class IntegrationReactSelect extends React.Component {
  static propTypes = {
    ...Input.props,
    classes: PropTypes.object.isRequired,
    labelText: PropTypes.string,
    fullWidth: PropTypes.bool,
    margin: PropTypes.string,
  };

  static defaultProps = {
    labelText: '',
    fullWidth: false,
    margin: 'normal',
  };

  render() {
    const {
      classes,
      labelText,
      fullWidth,
      margin,
      ...props
    } = this.props;

    return (
      <FormControl fullWidth={fullWidth} margin={margin} className={classes.root}>
        <InputLabel
          className={classes.label}
          shrink={!_.isEmpty(props.value) || props.focused}
          error={props.errorText}
        >
          {labelText}
        </InputLabel>
        <Input
          fullWidth={fullWidth}
          className={classes.input}
          inputComponent={SelectWrapped}
          error={props.errorText}
          inputProps={{
            classes,
            ...props,
          }}
        />
        <FormHelperText error={props.errorText} className={classes.helper}>
          {props.errorText}
        </FormHelperText>
      </FormControl>
    );
  }
}

export default withStyles(styles)(IntegrationReactSelect);
