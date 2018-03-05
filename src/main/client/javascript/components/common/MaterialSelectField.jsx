import { chain } from 'lodash';
import * as PropTypes from 'prop-types';
import * as React from 'react';

import MaterialSelect from './MaterialSelect';

export const computeOptions = valueKey => (options, filter) => {
  const key = valueKey || 'value';
  const filterLower = filter.toLowerCase();

  // Rank start with higher up
  const startsWith = chain(options)
    .filter(value => value[key].toLowerCase().startsWith(filterLower))
    .value();

  // Rank contains
  const filteredResults = chain(options)
    .filter(value => value[key].toLowerCase().includes(filterLower))
    .sortBy(value => value[valueKey])
    .value();

  return chain([...startsWith, ...filteredResults])
    .uniq()
    .value();
};

const MaterialSelectField = (props) => {
  const {
    input,
    label,
    meta: { touched, error },
    valueKey,
    ...custom
  } = props;

  const onChange = (evt) => {
    if (evt) {
      if (Array.isArray(evt)) {
        input.onChange(evt.map(v => v[valueKey || 'value']));
      } else {
        input.onChange(evt[valueKey || 'value']);
      }
    }
  };

  return (
    <MaterialSelect
      labelText={label}
      errorText={touched ? error : undefined}
      {...input}
      {...custom}
      value={input.value}
      valueKey={valueKey}
      onBlur={() => input.onBlur(input.value)}
      onChange={onChange}
      filterOptions={computeOptions(props.valueKey)}
    />);
};

MaterialSelectField.propTypes = {
  input: PropTypes.object.isRequired,
  label: PropTypes.string,
  valueKey: PropTypes.string,
  options: PropTypes.array,
  meta: PropTypes.shape({
    touched: PropTypes.any,
    error: PropTypes.any,
  }).isRequired,
};

MaterialSelectField.defaultProps = {
  label: undefined,
  valueKey: undefined,
  options: [],
};

export default MaterialSelectField;
