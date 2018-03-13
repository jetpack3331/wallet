import gasPrices from '../services/api/gasPrices';
import { asyncAction } from './actions';

export const fetchGasPrices = () => asyncAction(
  'GAS_PRICE_FETCH',
  gasPrices.fetchGasPrices(),
);
