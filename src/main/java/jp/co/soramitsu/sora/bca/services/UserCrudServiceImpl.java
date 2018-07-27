package jp.co.soramitsu.sora.bca.services;

import java.util.UUID;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.domain.repositories.UserRepository;
import jp.co.soramitsu.sora.bca.exceptions.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserCrudServiceImpl implements UserCrudService{

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserCrudServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void createUser(String username, String password) throws UserExistsException {
    if (!userRepository.findByUsername(username).isPresent()) {
      User user = new User();
      user.setUid(UUID.randomUUID());
      user.setUsername(username);
      user.setPassword(passwordEncoder.encode(password));
      user.setActive(true);
      userRepository.save(user);
    } else {
      throw new UserExistsException(username);
    }
  }
}
