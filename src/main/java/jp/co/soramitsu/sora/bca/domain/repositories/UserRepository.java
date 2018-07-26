package jp.co.soramitsu.sora.bca.domain.repositories;

import java.util.Optional;
import java.util.UUID;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.domain.projections.OnlyPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

//  @Query("select u from User u where u.enabled = true and u.username = :username")
  Optional<OnlyPassword> passwordByUsername(@Param("username") String username);

  Optional<User> findByUsername(String username);

}
