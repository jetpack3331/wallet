import { schema } from 'normalizr';

const user = new schema.Entity('users');
const arrayOfUsers = new schema.Array(user);

export default {
  user,
  arrayOfUsers,
};
