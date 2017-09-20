package beans;

import java.util.ArrayList;

import javax.ws.rs.FormParam;

public class Comment {

	private int id;
	private Topic parent;
	private String author;
	private String date;
	private Comment parentCom;
	private int parentComId;
	private ArrayList<String> children;
	@FormParam("content")
	private String content;
	private int poz;
	private int neg;
	private boolean changed;
	private ArrayList<String> usrResp;
	private boolean deleted;
	private ArrayList<String> usrSaved;

	public ArrayList<String> getUsrSaved() {
		return usrSaved;
	}

	public void setUsrSaved(ArrayList<String> usrSaved) {
		this.usrSaved = usrSaved;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public ArrayList<String> getUsrResp() {
		return usrResp;
	}

	public void setUsrResp(ArrayList<String> userResp) {
		this.usrResp = userResp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Comment() {
		super();
		this.id = -1;
		this.parent = null;
		this.author = "";
		this.date = "";
		this.parentCom = null;
		this.parentComId = -1;
		this.children = new ArrayList<String>();
		this.content = "";
		this.poz = 0;
		this.neg = 0;
		this.changed = false;
		this.usrResp = new ArrayList<String>();
		this.deleted = false;
		this.usrSaved = new ArrayList<String>();
	}

	public Comment(Comment c) {
		super();
		this.parent = c.parent;
		this.author = c.author;
		this.date = c.date;
		this.parentCom = c.parentCom;
		this.id = c.id;
		this.children = c.children;
		this.content = c.content;
		this.poz = c.poz;
		this.neg = c.neg;
		this.changed = c.changed;
		this.parentComId = c.parentComId;
		this.usrResp = c.usrResp;
		this.deleted = c.deleted;
		this.usrSaved = c.usrSaved;
	}

	public Comment(int id, Topic parent, String author, String date, Comment parentCom, ArrayList<String> children,
			String content, int poz, int neg, boolean changed, int parentComId) {
		super();
		this.id = id;
		this.parent = parent;
		this.author = author;
		this.date = date;
		this.parentCom = parentCom;
		this.children = children;
		this.content = content;
		this.poz = poz;
		this.neg = neg;
		this.changed = changed;
		this.parentComId = parentComId;
	}

	public Comment(int id, Topic parent, String author, String date, Comment parentCom, ArrayList<String> children,
			String content, int poz, int neg, boolean changed, int parentComId, ArrayList<String> usrResp,
			boolean deleted, ArrayList<String> usrSaved) {
		super();
		this.id = id;
		this.parent = parent;
		this.author = author;
		this.date = date;
		this.parentCom = parentCom;
		this.children = children;
		this.content = content;
		this.poz = poz;
		this.neg = neg;
		this.changed = changed;
		this.parentComId = parentComId;
		this.usrResp = usrResp;
		this.deleted = deleted;
		this.usrSaved = usrSaved;
	}

	public Topic getParent() {
		return parent;
	}

	public void setParent(Topic roditelj) {
		this.parent = roditelj;
	}

	public String getAuthor() {
		return author;
	}

	public int getParentComId() {
		return parentComId;
	}

	public void setParentComId(int parentComId) {
		this.parentComId = parentComId;
	}

	public void setAuthor(String autor) {
		this.author = autor;
	}

	public String getDate() {
		return date;
	}

	public Comment getParentCom() {
		return parentCom;
	}

	public void setParentCom(Comment roditeljKom) {
		this.parentCom = roditeljKom;
	}

	public ArrayList<String> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<String> children) {
		this.children = children;
	}

	public void setDate(String date) {
		this.date = date;
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

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean izmena) {
		this.changed = izmena;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
