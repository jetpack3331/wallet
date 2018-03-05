import { Chip, MenuItem, Typography } from 'material-ui';
import ArrowDropDownIcon from 'material-ui-icons/ArrowDropDown';
import ArrowDropUpIcon from 'material-ui-icons/ArrowDropUp';
import CancelIcon from 'material-ui-icons/Cancel';
import ClearIcon from 'material-ui-icons/Clear';
import * as PropTypes from 'prop-types';
/* eslint-disable jsx-a11y/no-static-element-interactions, jsx-a11y/click-events-have-key-events */
import React from 'react';
import ReactDOM from 'react-dom';
import Select from 'react-select';

class Option extends React.Component {
    propTypes = {
      children: PropTypes.array.isRequired,
      isFocused: PropTypes.array.isRequired,
      isSelected: PropTypes.array.isRequired,
      onFocus: PropTypes.array.isRequired,
      onSelect: PropTypes.func.isRequired,
      option: PropTypes.object.isRequired,
    };

    handleClick = (event) => {
      this.props.onSelect(this.props.option, event);
    };

    render() {
      const {
        children,
        isFocused,
        isSelected,
        onFocus,
      } = this.props;

      return (
        <MenuItem
          onFocus={onFocus}
          selected={isFocused}
          onClick={this.handleClick}
          component="div"
          style={{
            fontWeight: isSelected ? 500 : 400,
          }}
        >
          {children}
        </MenuItem>
      );
    }
}

// https://github.com/JedWatson/react-select/issues/810
class SelectWithPortal extends Select {
  renderOuter(options, valueArray, focusedOption) {
    const dimensions = this.wrapper ? this.wrapper.getBoundingClientRect() : null;
    const menu = super.renderMenu(options, valueArray, focusedOption);

    if (!menu || !dimensions) return null;

    const maxHeight = document.body.offsetHeight - (dimensions.top + dimensions.height);

    // eslint-disable-next-line no-underscore-dangle
    const instancePrefix = this._instancePrefix;
    return ReactDOM.createPortal(
      <div
        ref={(ref) => { this.menuContainer = ref; }}
        className="Select-menu-outer"
        onClick={(e) => { e.stopPropagation(); }}
        style={{
            ...this.props.menuContainerStyle,
            zIndex: 9999,
            position: 'absolute',
            width: dimensions.width,
            top: dimensions.top + dimensions.height,
            left: dimensions.left,
            maxHeight: Math.min(maxHeight, 200),
            overflow: 'hidden',
          }}
      >
        <div
          ref={(ref) => { this.menu = ref; }}
          role="listbox"
          tabIndex={-1}
          className="Select-menu"
          id={`${instancePrefix}-list`}
          style={{
              ...this.props.menuStyle,
              maxHeight: Math.min(maxHeight, 200),
            }}
          onScroll={this.handleMenuScroll}
          onMouseDown={this.handleMouseDownOnMenu}
        >
          {menu}
        </div>
      </div>,
      document.body,
    );
  }
}

const SelectWrapped = (props) => {
  const {
    classes,
    ...other
  } = props;

  const arrowRenderer = arrowProps => (
    arrowProps.isOpen
      ? <ArrowDropUpIcon />
      : <ArrowDropDownIcon />);

  return (
    <SelectWithPortal
      optionComponent={Option}
      noResultsText={<Typography>No results found</Typography>}
      arrowRenderer={arrowRenderer}
      clearRenderer={() => <ClearIcon />}
      valueComponent={(valueProps) => {
          const { value, children, onRemove } = valueProps;

          const onDelete = (event) => {
            event.preventDefault();
            event.stopPropagation();
            onRemove(value);
          };

          if (onRemove) {
            return (
              <Chip
                tabIndex={-1}
                label={children}
                className={classes.chip}
                deleteIcon={<CancelIcon onTouchEnd={onDelete} />}
                onDelete={onDelete}
              />
            );
          }

          return <div className="Select-value">{children}</div>;
        }}
      {...other}
    />
  );
};

SelectWrapped.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default SelectWrapped;
