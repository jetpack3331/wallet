import { shape, string, number } from 'prop-types';

export const tokenLogo = shape({
  src: string.isRequired,
  width: string,
  height: string,
});

export const token = shape({
  symbol: string.isRequired,
  address: string.isRequired,
  name: string.isRequired,
  decimals: number.isRequired,
  logo: tokenLogo,
});
