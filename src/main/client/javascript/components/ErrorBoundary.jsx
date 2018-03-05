import * as PropTypes from 'prop-types';
import React, { Component } from 'react';
import { logComponentError } from '../util/errors';

class ErrorBoundary extends Component {
  static propTypes = {
    children: PropTypes.node.isRequired,
    renderError: PropTypes.oneOf([PropTypes.func, PropTypes.object]),
  };

  static defaultProps = {
    renderError: undefined,
  };

  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidUpdate(prevProps) {
    if (prevProps.children !== this.props.children) {
      // eslint-disable-next-line react/no-did-update-set-state
      this.setState({
        hasError: false,
      });
    }
  }

  componentDidCatch(error, { componentStack }) {
    logComponentError(error, componentStack);
    console.error(componentStack, error);

    // eslint-disable-next-line react/no-unused-state
    this.setState({ hasError: true, error, componentStack });
  }

  render() {
    const { hasError, error, info } = this.state;
    const { children, renderError: Error } = this.props;

    if (hasError) {
      if (Error) {
        return <Error error={error} info={info} />;
      }
      return (
        <div>
          An error has occured and been logged.
        </div>);
    }

    return children;
  }
}

export default ErrorBoundary;
