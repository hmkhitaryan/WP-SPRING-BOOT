package com.egs.account.service.user;

import com.egs.account.model.User;

import java.util.List;

/**
 * UserService interface with required methods declarations
 *
 * @author Hayk_Mkhitaryan
 */
public interface UserService {

	/**
	 * Find user by id.
	 *
	 * @param id by which user will be found
	 * @return user found
	 */
	User findById(Long id);

	/**
	 * Save user.
	 *
	 * @param user to be saved
	 */
	void saveUser(User user);

	/**
	 * Update user.
	 *
	 * @param user to be updated
	 */
	void updateUser(User user);

	/**
	 * Find user by username.
	 *
	 * @param username by which user will be found
	 * @return user found
	 */
	User findByUsername(String username);

	/**
	 * Delete user by id.
	 *
	 * @param id by which user will be deleted
	 */
	void deleteUserById(Long id);

	/**
	 * Find all users.
	 *
	 * @return users found
	 */
	List<User> findAllUsers();
}
