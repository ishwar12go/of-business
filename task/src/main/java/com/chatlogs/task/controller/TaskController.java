package com.chatlogs.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatlogs.task.entity.UserChat;
import com.chatlogs.task.service.TaskService;

@RestController
@RequestMapping("/chatlogs")
public class TaskController {
	@Autowired
	private TaskService service;

	@PostMapping("/{user}")
	public String createNewEntry(@PathVariable("user") String user, @RequestBody UserChat userChat) {
		return "Your message Id : " + service.createNewEntry(user, userChat);
	}

	@GetMapping("/{user}")
	public List<UserChat> userChatLogs(@PathVariable("user") String user,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") int start) {
		return service.userChatLogs(user, limit, start);
	}

	@DeleteMapping("/{user}")
	public boolean deleteAllUserChats(@PathVariable("user") String user) {
		return service.deleteAllUserChats(user);
	}

	@DeleteMapping("/{user}/{msgId}")
	public ResponseEntity<String> deleteAllUserChats(@PathVariable("user") String user,
			@PathVariable("msgId") int msgId) {
		String responseMessage = service.deleteAllUserMsgs(user, msgId);
		if (responseMessage.contains("not")) {
			return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);

	}
}
