package beans;

public class SaveTopicBean {

	private String topic;
	private String subforum;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSubforum() {
		return subforum;
	}

	public void setSubforum(String subforum) {
		this.subforum = subforum;
	}

	public SaveTopicBean(String topic, String subforum) {
		super();
		this.topic = topic;
		this.subforum = subforum;
	}

	public SaveTopicBean() {
		super();
	}

	public SaveTopicBean(SaveTopicBean stb) {
		super();
		this.topic = stb.topic;
		this.subforum = stb.subforum;
	}

}
