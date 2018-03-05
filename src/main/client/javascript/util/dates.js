import moment from 'moment';

const LOCAL_DATE_FORMAT = 'YYYY-MM-DD';

const parseDate = str => (str && moment(str, LOCAL_DATE_FORMAT).toDate()) || str;
const serializeDate = date => (date && moment(date).format(LOCAL_DATE_FORMAT)) || date;

const parseTimestamp = str => (str && new Date(Date.parse(str))) || str;
const serializeTimestamp = str => (str && new Date(Date.parse(str))) || str;

export default {
  parseDate,
  serializeDate,
  parseTimestamp,
  serializeTimestamp,
};
