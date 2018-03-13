import { shape, string } from 'prop-types';

export default shape({
  averagePrice: string,
  safeLowPrice: string,
  fastPrice: string,
});
