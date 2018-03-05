package hut34.wallet.framework.usermanagement.repository;

import hut34.wallet.framework.usermanagement.model.User;
import org.springframework.contrib.gae.objectify.repository.ObjectifyStringRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ObjectifyStringRepository<User> {

}
