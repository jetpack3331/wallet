import { arrayOf, shape, string } from 'prop-types';

export default shape({
  username: string,
  email: string,
  name: string,
  roles: arrayOf(string),
  status: string,
});
