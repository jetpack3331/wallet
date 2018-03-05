import { isNumber } from 'lodash';
import moment from 'moment';
import * as numeral from 'numeral';

const date = (str, defaultVal) => (str && moment(str).format('DD/MM/YYYY')) || defaultVal;
const currency = num => num && numeral(num).format('$0,0.00');
const percent = num => isNumber(num) && `${num}%`;
const enumSlugify = str => str && str.replace(/_/g, '-').toLowerCase();

export default {
  date,
  currency,
  percent,
  enumSlugify,
};
