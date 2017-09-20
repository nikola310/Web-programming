package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 203114413238122824L;
	public static String MODERATOR = "MODERATOR";
	public static String ADMIN = "ADMIN";
	public static String NORMAL = "NORMAL";

	private String username;
	private String password;
	private String name;
	private String surname;
	private String tel;
	private String email;
	private String regDate;
	private ArrayList<String> subforum;
	private ArrayList<SaveTopicBean> savedTopics;
	private ArrayList<SaveCommentBean> savedComments;
	private ArrayList<Integer> savedMessages;
	private String userType;
	private int pozTotal;
	private int negTotal;

	public int getPozTotal() {
		return pozTotal;
	}

	public void setPozTotal(int pozTotal) {
		this.pozTotal = pozTotal;
	}

	public int getNegTotal() {
		return negTotal;
	}

	public void setNegTotal(int negTotal) {
		this.negTotal = negTotal;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String lozinka) {
		this.password = lozinka;
	}

	public String getName() {
		return name;
	}

	public void setName(String ime) {
		this.name = ime;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String prezime) {
		this.surname = prezime;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String datum) {
		this.regDate = datum;
	}

	public ArrayList<String> getSubforum() {
		return subforum;
	}

	public void setForums(ArrayList<String> forumi) {
		this.subforum = forumi;
	}

	public ArrayList<SaveTopicBean> getSavedTopics() {
		return savedTopics;
	}
	
	public ArrayList<SaveCommentBean> getSavedComments() {
		return savedComments;
	}

	public void setSavedTopics(ArrayList<SaveTopicBean> savedTopics) {
		this.savedTopics = savedTopics;
	}

	public ArrayList<Integer> getSavedMessages() {
		return savedMessages;
	}

	public void setSavedMessages(ArrayList<Integer> savedMessages) {
		this.savedMessages = savedMessages;
	}

	public void setSavedComments(ArrayList<SaveCommentBean> savedComments) {
		this.savedComments = savedComments;
	}

	public void setSubforum(ArrayList<String> subforum) {
		this.subforum = subforum;
	}

	public User(String username, String password, String name, String surname, String tel, String email, String regDate,
			ArrayList<String> subforum, ArrayList<SaveTopicBean> savedTopics, ArrayList<SaveCommentBean> savedComments,
			String userType, int pozTotal, int negTotal) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.tel = tel;
		this.email = email;
		this.regDate = regDate;
		this.subforum = subforum;
		this.savedTopics = savedTopics;
		this.savedComments = savedComments;
		this.userType = userType;
		this.pozTotal = pozTotal;
		this.negTotal = negTotal;
	}

	public User() {
		this.username = "";
		this.password = "";
		this.name = "";
		this.surname = "";
		this.tel = "";
		this.email = "";
		this.regDate = "";
		this.subforum = new ArrayList<String>();
		this.savedTopics = new ArrayList<SaveTopicBean>();
		this.savedComments = new ArrayList<SaveCommentBean>();
		this.userType = User.NORMAL;
		this.pozTotal = 0;
		this.negTotal = 0;
		this.savedMessages = new ArrayList<Integer>();
	}

	public User(User u) {
		this.username = u.username;
		this.password = u.password;
		this.name = u.name;
		this.surname = u.surname;
		this.tel = u.tel;
		this.email = u.email;
		this.regDate = u.regDate;
		this.subforum = u.subforum;
		this.savedTopics = u.savedTopics;
		this.savedComments = u.savedComments;
		this.userType = u.userType;
		this.pozTotal = u.pozTotal;
		this.negTotal = u.negTotal;
		this.savedMessages = u.savedMessages;
	}

}
