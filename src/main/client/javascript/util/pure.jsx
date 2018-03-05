import * as React from 'react';

export function pure(Component) {
  class PureFunctionalComponent extends React.PureComponent {
    static displayName = `pure(${Component.displayName || Component.name})`;

    render() {
      return <Component {...this.props} />;
    }
  }

  return PureFunctionalComponent;
}
