import { requestJSON } from './http';

const fetchGasPrices = () => requestJSON('/gas', 'GET');

export default {
  fetchGasPrices,
};
