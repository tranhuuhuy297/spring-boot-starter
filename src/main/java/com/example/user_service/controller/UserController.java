package com.example.user_service.controller;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.util.ConstantUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String name) {
        try {
//            LOGGER.info("Get list users with keyword: " + name);
            kafkaTemplate.send(ConstantUtil.KAFKA_TOPIC_LOG_USER_SERVICE, "Get list users with keyword: " + name);
            List<User> listUser = new ArrayList<User>();

            if (name == null) {
                listUser.addAll(userRepository.findAll());
            } else {
                listUser.addAll(userRepository.findByNameContaining(name));
            }

            return new ResponseEntity<>(listUser, HttpStatus.OK);
        } catch (Exception e) {
//            LOGGER.error("Get list users" + e);
            kafkaTemplate.send(ConstantUtil.KAFKA_TOPIC_LOG_USER_SERVICE, "Get list users" + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") String id) {
        LOGGER.info("Get user by id: " + id);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUsers(@RequestBody User user) {
        LOGGER.info("Create new user: " + user.toString());
        try {
            User newUser = userRepository.save(user);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Create new user fail" + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        LOGGER.info("Update user by id: " + id);
        Optional<User> existUser = userRepository.findById(id);

        if (existUser.isPresent()) {
            User updateUser = existUser.get();
            updateUser.setName(user.getName());
            updateUser.setGmail(user.getGmail());
            updateUser.setPassword(user.getPassword());
            updateUser.setActivate(user.isActivate());
            return new ResponseEntity<>(userRepository.save(updateUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") String id) {
        LOGGER.info("Delete user by id" + id);
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Delete user by id " + id + " " + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("users")
    public ResponseEntity<List<String>> deleteListUser(@RequestBody List<String> ids) {
        LOGGER.info("Delete many user by ids: " + ids);
        try {
            userRepository.deleteAllById(ids);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Delete many users fail " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
