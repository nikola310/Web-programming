package beans;

import java.util.ArrayList;

public class Topic {

	public static String TEXT = "TEXT";
	public static String PICTURE = "PICTURE";
	public static String LINK = "LINK";

	private String parent;
	private String title;
	private String type;
	private String author;
	private boolean comments;
	private String content;
	private String datum;
	private int poz;
	private int neg;
	private ArrayList<String> usrResp;

	public ArrayList<String> getUsrResp() {
		return usrResp;
	}

	public void setUsrResp(ArrayList<String> usrResp) {
		this.usrResp = usrResp;
	}

	public Topic() {
		super();
		this.author = null;
		this.comments = false;
		this.content = "";
		this.datum = null;
		this.neg = 0;
		this.poz = 0;
		this.title = "";
		this.type = "";
		this.usrResp = new ArrayList<String>();
	}

	public Topic(Topic t) {
		super();
		this.author = t.author;
		this.comments = t.comments;
		this.content = t.content;
		this.datum = t.datum;
		this.neg = t.neg;
		this.poz = t.poz;
		this.title = t.title;
		this.type = t.type;
		this.usrResp = t.usrResp;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String roditelj) {
		this.parent = roditelj;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String naslov) {
		this.title = naslov;
	}

	public String getType() {
		return type;
	}

	public void setType(String tip) {
		this.type = tip;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String autor) {
		this.author = autor;
	}

	public boolean getComments() {
		return comments;
	}

	public void setComments(boolean komentari) {
		this.comments = komentari;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String sadrzaj) {
		this.content = sadrzaj;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public int getPoz() {
		return poz;
	}

	public void setPoz(int poz) {
		this.poz = poz;
	}

	public int getNeg() {
		return neg;
	}

	public void setNeg(int neg) {
		this.neg = neg;
	}
}
