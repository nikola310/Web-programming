package beans;

public class LikeBean {

	private int id;
	private String author;
	private String title;
	
	public LikeBean() {
		super();
		id = -1;
		author = "";
		title = "";
	}
	public LikeBean(int id, String author, String title) {
		super();
		this.id = id;
		this.author = author;
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
