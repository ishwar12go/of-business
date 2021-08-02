package com.chatlogs.task.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.chatlogs.task.entity.UserChat;

@Service
public class TaskService {
	private Map<String, Set<UserChat>> chatLogs;

	public TaskService() {
		chatLogs = new HashMap<>();
	}

	public Integer createNewEntry(String user, UserChat userChat) {
		int messageID = userChat.hashCode();

		Comparator<UserChat> comparator = (userChat1, userChat2) -> Long.compare(userChat1.getTimestamp(),
				userChat2.getTimestamp());
		Set<UserChat> userLogs = new TreeSet<>(Collections.reverseOrder(comparator));

		if (chatLogs.containsKey(user)) {
			if (chatLogs.containsValue(chatLogs.get(user))) {
				userLogs = chatLogs.get(user);
			}
			if (userLogs.contains(userChat)) {
				for (UserChat chat : userLogs) {
					if (chat.equals(userChat)) {
						messageID = chat.getMsgId();
						break;
					}
				}
			} else {
				userChat.setMsgId(messageID);
				userLogs.add(userChat);
				chatLogs.put(user, userLogs);
			}

		} else {
			userChat.setMsgId(messageID);
			userLogs.add(userChat);
			chatLogs.put(user, userLogs);
		}
		return messageID;
	}

	public List<UserChat> userChatLogs(String user, int limit, int start) {
		if (chatLogs.containsKey(user)) {
			Set<UserChat> userLogs = chatLogs.get(user);
			if (null != userLogs && userLogs.size() >= start) {
				List<UserChat> list = new ArrayList<>(userLogs);
				return (list.size() >= (start + limit)) ? list.subList(start, start + limit)
						: list.subList(start, list.size());
			}
		}
		return new ArrayList<>();
	}

	public boolean deleteAllUserChats(String user) {
		if (chatLogs.containsKey(user)) {
			chatLogs.remove(user);
		}
		return true;
	}

	public String deleteAllUserMsgs(String user, int msgId) {
		if (chatLogs.containsKey(user)) {
			Set<UserChat> userLogs = chatLogs.get(user);
			for (UserChat userChat : userLogs) {
				if (msgId == userChat.getMsgId()) {
					userLogs.remove(userChat);
					return "MsgId is deleted";
				}
			}
		}
		return "Msgid is not found";
	}

}
