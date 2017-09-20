package beans;

public class Message {
	private String sender;
	private String recipient;
	private String text;
	private boolean read;

	public Message(Message m) {
		super();
		this.sender = m.sender;
		this.recipient = m.recipient;
		this.text = m.text;
		this.read = m.read;
	}

	public Message() {
		super();
		this.sender = null;
		this.recipient = null;
		this.text = "";
		this.read = false;
	}

	public Message(String sender, String recipient, String text, boolean read) {
		super();
		this.sender = sender;
		this.recipient = recipient;
		this.text = text;
		this.read = read;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
