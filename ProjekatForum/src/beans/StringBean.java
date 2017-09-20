package beans;

public class StringBean {
	private String value;
	private String header;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StringBean() {
		super();
		this.value = "";
		this.header = "";
	}

	public StringBean(StringBean sb) {
		this.value = sb.value;
		this.header = sb.header;
	}

	public StringBean(String value, String header) {
		super();
		this.value = value;
		this.header = header;
	}
}
