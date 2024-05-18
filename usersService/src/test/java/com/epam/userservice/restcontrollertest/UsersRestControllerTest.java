package com.epam.userservice.restcontrollertest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.userservice.dto.UserDTO;
import com.epam.userservice.restcontroller.UserRestController;
import com.epam.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserRestController.class)
class UsersRestControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAllUsers() throws Exception {
		List<UserDTO> users = Arrays.asList(
				UserDTO.builder().id(1).username("user1").email("user1@example.com").name("User One").build(),
				UserDTO.builder().id(2).username("user2").email("user2@example.com").name("User Two").build());

		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].username", is("user1")))
				.andExpect(jsonPath("$[0].email", is("user1@example.com")))
				.andExpect(jsonPath("$[0].name", is("User One"))).andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].username", is("user2")))
				.andExpect(jsonPath("$[1].email", is("user2@example.com")))
				.andExpect(jsonPath("$[1].name", is("User Two")));

		verify(userService, times(1)).getAllUsers();
		verifyNoMoreInteractions(userService);
	}

	@Test
	void testGetUserByUsername() throws Exception {
		UserDTO user = UserDTO.builder().id(1).username("user1").email("user1@example.com").name("User One").build();

		when(userService.getUserByUsername(anyString())).thenReturn(user);

		mockMvc.perform(get("/users/{username}", "user1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("user1"))).andExpect(jsonPath("$.email", is("user1@example.com")))
				.andExpect(jsonPath("$.name", is("User One")));

		verify(userService, times(1)).getUserByUsername("user1");
		verifyNoMoreInteractions(userService);
	}

	@Test
	void testAddUser() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("johnDoe");
		userDTO.setEmail("johndoe@example.com");
		userDTO.setName("John Doe");

		when(userService.addUser(any(UserDTO.class))).thenReturn(userDTO);

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDTO))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value(userDTO.getUsername()))
				.andExpect(jsonPath("$.email").value(userDTO.getEmail()))
				.andExpect(jsonPath("$.name").value(userDTO.getName()));

		verify(userService, times(1)).addUser(any(UserDTO.class));
	}

	@Test
	void testDeleteUser() throws Exception {
		mockMvc.perform(delete("/users/testuser")).andExpect(status().isNoContent());
	}

	@Test
	void testUpdateUser() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("johnDoe");
		userDTO.setEmail("johndoe@example.com");
		userDTO.setName("John Doe");

		String username = "johnDoe";

		when(userService.updateUser(eq(username), any(UserDTO.class))).thenReturn(userDTO);

		mockMvc.perform(put("/users/{username}", username).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value(userDTO.getUsername()))
				.andExpect(jsonPath("$.email").value(userDTO.getEmail()))
				.andExpect(jsonPath("$.name").value(userDTO.getName()));

		verify(userService).updateUser(eq(username), any(UserDTO.class));
	}

}
