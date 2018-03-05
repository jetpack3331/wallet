import PropTypes from 'prop-types';
import React from 'react';
import { FormattedDate } from 'react-intl';
import Moment from 'react-moment';
import Time from './Time';
import './TimeAgo.less';

class TimeAgo extends React.Component {
  static propTypes = {
    value: PropTypes.instanceOf(Date).isRequired,
    expandable: PropTypes.bool,
  };

  static defaultProps = {
    expandable: false,
  };

  state = { showFull: false };

  toggleShowFull = () => {
    this.setState({ showFull: !this.state.showFull });
  };

  render() {
    const { value, expandable } = this.props;
    const { showFull } = this.state;
    return (
      <span className="time-ago">
        {expandable && !showFull &&
        <Moment date={value} fromNow onClick={this.toggleShowFull} style={{ cursor: 'pointer' }}/>
        }
        {expandable && showFull &&
        <span
          role="button"
          onClick={this.toggleShowFull}
          onKeyPress={this.toggleShowFull}
          tabIndex={0}
          style={{ cursor: 'pointer' }}
        >
          <FormattedDate value={value}/> <Time value={value}/>
        </span>
        }
        {!expandable &&
        <Moment date={value} fromNow/>
        }
      </span>

    );
  }
}

export default TimeAgo;
