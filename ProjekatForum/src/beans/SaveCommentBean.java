package beans;

public class SaveCommentBean {
	private int id;
	private String subforum;
	private String topic;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubforum() {
		return subforum;
	}

	public void setSubforum(String subforum) {
		this.subforum = subforum;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public SaveCommentBean(int id, String subforum, String topic) {
		super();
		this.id = id;
		this.subforum = subforum;
		this.topic = topic;
	}

	public SaveCommentBean() {
		super();
	}

	public SaveCommentBean(SaveCommentBean scb) {
		super();
		this.id = scb.id;
		this.subforum = scb.subforum;
		this.topic = scb.topic;
	}
}
