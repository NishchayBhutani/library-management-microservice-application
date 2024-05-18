package com.epam.userservice.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epam.userservice.dao.User;
import com.epam.userservice.dto.UserDTO;
import com.epam.userservice.repo.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper modelMapper;

	private static final String NOT_FOUND = "no user found with given username";

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(user -> UserDTO.builder().id(user.getId())
				.username(user.getUsername()).email(user.getEmail()).name(user.getName()).build()).toList();
	}

	public UserDTO getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(user -> UserDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
						.username(user.getUsername()).build())
				.orElseGet(() -> UserDTO.builder().developerMessage(NOT_FOUND).timeStamp(new Date().toString())
						.httpStatus(HttpStatus.NO_CONTENT).build());
	}

	public UserDTO addUser(UserDTO userDTO) {
		log.info("adding user: {}", userDTO);
		UserDTO result;
		if (userRepository.existsByUsername(userDTO.getUsername())) {
			result = UserDTO.builder().developerMessage("username already taken").httpStatus(HttpStatus.NO_CONTENT)
					.timeStamp(new Date().toString()).build();
		} else {
			result = modelMapper.map(userRepository.save(modelMapper.map(userDTO, User.class)), UserDTO.class);
		}
		return result;
	}

	@Transactional
	public void deleteUser(String username) {
		log.info("deleting user with username: {}", username);
		userRepository.deleteByUsername(username);
	}

	public UserDTO updateUser(String username, UserDTO userDTO) {
		log.info("updating user: {}", userDTO);
		return userRepository.findByUsername(username).map(user -> {
			user.setName(userDTO.getName());
			user.setEmail(userDTO.getEmail());
			user.setUsername(userDTO.getUsername());
			return modelMapper.map(user, UserDTO.class);
		}).orElseGet(() -> UserDTO.builder().developerMessage(NOT_FOUND).timeStamp(new Date().toString())
				.httpStatus(HttpStatus.NO_CONTENT).build());
	}
}