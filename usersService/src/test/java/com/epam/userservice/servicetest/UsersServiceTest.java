package com.epam.userservice.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.epam.userservice.dao.User;
import com.epam.userservice.dto.UserDTO;
import com.epam.userservice.repo.UserRepository;
import com.epam.userservice.service.UserService;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private UserService userService;

	@Test
	void testGetAllUsers() {
		User user1 = User.builder().id(1).username("john").email("john@test.com").name("John").build();
		User user2 = User.builder().id(2).username("jane").email("jane@test.com").name("Jane").build();
		List<User> userList = List.of(user1, user2);
		UserDTO userDTO1 = UserDTO.builder().id(1).username("john").email("john@test.com").name("John").build();
		UserDTO userDTO2 = UserDTO.builder().id(2).username("jane").email("jane@test.com").name("Jane").build();
		List<UserDTO> expectedUserDTOList = List.of(userDTO1, userDTO2);

		Mockito.when(userRepository.findAll()).thenReturn(userList);

		List<UserDTO> actualUserDTOList = userService.getAllUsers();
		assertEquals(expectedUserDTOList, actualUserDTOList);
	}

	@Test
	void testGetUserByUsername() {
		String username = "john";
		User user = User.builder().id(1).username(username).email("john@test.com").name("John").build();
		UserDTO expectedUserDTO = UserDTO.builder().id(1).username(username).email("john@test.com").name("John")
				.build();

		Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

		UserDTO actualUserDTO = userService.getUserByUsername(username);
		assertEquals(expectedUserDTO, actualUserDTO);
	}

	@Test
	void testGetUserByUsernameNotPresent() {
		String username = "john";

		Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
		
		UserDTO result = userService.getUserByUsername(username);
		assertEquals("no user found with given username", result.getDeveloperMessage());
	}

	@Test
	void testAddUser() {
		UserDTO userDTO = UserDTO.builder().id(1).username("john").email("john@test.com").name("John").build();
		User user = User.builder().id(1).username("john").email("john@test.com").name("John").build();

		Mockito.when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
		Mockito.when(modelMapper.map(userDTO, User.class)).thenReturn(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

		UserDTO actualUserDTO = userService.addUser(userDTO);
		assertEquals(userDTO, actualUserDTO);
	}

	@Test
	void testAddUserAlreadyPresent() {
		UserDTO userDTO = UserDTO.builder().id(1).username("john").email("john@test.com").name("John").build();

		Mockito.when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

		UserDTO result = userService.addUser(userDTO);
		assertEquals("username already taken", result.getDeveloperMessage());
	}

	@Test
	void testDeleteUser() {
		String username = "john123";
		userService.deleteUser(username);
		verify(userRepository).deleteByUsername(username);
	}

	@Test
	void testUpdateUser() {
		 String username = "johnDoe";
	        UserDTO userDTO = new UserDTO();
	        userDTO.setUsername(username);
	        userDTO.setEmail("johndoe@example.com");
	        userDTO.setName("John Doe");
	        User user = new User();
	        user.setUsername(username);
	        user.setEmail(userDTO.getEmail());
	        user.setName(userDTO.getName());
	        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
	        UserDTO result = userService.updateUser(username, userDTO);
	        assertEquals(userDTO.getUsername(), result.getUsername());
	        assertEquals(userDTO.getEmail(), result.getEmail());
	        assertEquals(userDTO.getName(), result.getName());

	        verify(userRepository).findByUsername(username);
	}

	@Test
	void testUpdateUserNotPresent() {
		String username = "john123";
		UserDTO userDTO = new UserDTO();
		userDTO.setName("John Smith");
		userDTO.setEmail("john.smith@example.com");

		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		UserDTO result = userService.updateUser(username, userDTO);
		assertEquals("no user found with given username", result.getDeveloperMessage());
	}

}