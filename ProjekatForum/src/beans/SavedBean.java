package beans;

import java.util.ArrayList;
import java.util.Map;

public class SavedBean {

	private ArrayList<Comment> comments;
	private Map<String, Topic> topics;
	private ArrayList<Message> messages;

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public SavedBean(SavedBean sb) {
		super();
		this.comments = sb.comments;
		this.topics = sb.topics;
		this.messages = sb.messages;
	}

	public SavedBean() {
		super();
	}

	public SavedBean(ArrayList<Comment> comments, Map<String, Topic> topics, ArrayList<Message> messages) {
		super();
		this.comments = comments;
		this.topics = topics;
		this.messages = messages;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public Map<String, Topic> getTopics() {
		return topics;
	}

	public void setTopics(Map<String, Topic> topics) {
		this.topics = topics;
	}

}
