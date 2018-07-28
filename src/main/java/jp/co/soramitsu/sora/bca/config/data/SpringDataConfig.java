package jp.co.soramitsu.sora.bca.config.data;

import javax.persistence.EntityManagerFactory;
import jp.co.soramitsu.sora.bca.domain.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableTransactionManagement
@Configuration
public class SpringDataConfig {

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
