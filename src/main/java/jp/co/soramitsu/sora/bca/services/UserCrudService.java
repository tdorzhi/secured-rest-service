package jp.co.soramitsu.sora.bca.services;

import jp.co.soramitsu.sora.bca.exceptions.UserExistsException;

public interface UserCrudService {

  void createUser(String username, String password) throws UserExistsException;

}
