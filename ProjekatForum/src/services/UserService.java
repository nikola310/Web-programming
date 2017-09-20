package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import com.groupbyinc.common.jackson.core.JsonProcessingException;
import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.databind.ObjectMapper;

import beans.Comment;
import beans.ErrorBean;
import beans.LikeBean;
import beans.Message;
import beans.NumberBean;
import beans.SaveCommentBean;
import beans.SaveTopicBean;
import beans.SavedBean;
import beans.StringBean;
import beans.Subforum;
import beans.Topic;
import beans.User;
import utils.FileUtilities;
import utils.StringUtilities;

@Path("/user")
public class UserService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean login(User u) {
		boolean retVal = false;
		File f = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		try {
			HashMap<String, User> tmp = mapper.readValue(f, new TypeReference<HashMap<String, User>>() {
			});
			if (tmp.containsKey(u.getUsername())) {
				if (u.getPassword().equals(tmp.get(u.getUsername()).getPassword())) {
					System.out.println("Korisnik " + u.getUsername() + " se prijavio na forum.");
					request.getSession().setAttribute("user", tmp.get(u.getUsername()));
					retVal = true;
					return retVal;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	@GET
	@Path("/testlogin")
	@Produces(MediaType.TEXT_PLAIN)
	public String testLogin() {
		User retVal = null;
		retVal = (User) request.getSession().getAttribute("user");
		if (retVal == null) {
			return "No user logged in.";
		}
		return "User " + retVal.getUsername() + " logged in.";
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorBean register(User u) {
		System.out.println("---------------------------------------------------------");
		ErrorBean retVal = new ErrorBean();
		File f = FileUtilities.getUsersFile();
		File udir = FileUtilities.getUsersDir(u.getUsername());
		if (udir.mkdirs()) {

		} else {
			System.out.println("user dir not created");
		}
		System.out.println(udir.getAbsolutePath());
		u.setUserType(User.NORMAL);
		ObjectMapper mapper = new ObjectMapper();
		if (f.exists()) {

			try {
				HashMap<String, User> tmp = mapper.readValue(f, new TypeReference<HashMap<String, User>>() {
				});

				if (tmp.containsKey(u.getUsername())) {
					System.out.println("Vec postoji korisnik sa imenom: " + u.getUsername());
					retVal.setFailed(true);
					retVal.setErrCode(ErrorBean.USER_ERROR);
				} else {
					for (String s : tmp.keySet()) {
						if (tmp.get(s).getEmail().equals(u.getEmail())) {
							System.out.println("Vec postoji korisnik sa emailom: " + u.getEmail());
							retVal.setFailed(true);
							retVal.setErrCode(ErrorBean.EMAIL_ERROR);
						}
					}
					if (retVal.isFailed() == false) {
						System.out.println("---------------------------------------------------------");
						System.out.println("Registriran korisnik: " + u.getUsername());
						tmp.put(u.getUsername(), u);
						String sUser = "";
						sUser = mapper.writeValueAsString(tmp);
						// System.out.println(sUser);
						// File novo = new File(ts);
						// f.delete();
						try {
							PrintWriter writer = new PrintWriter(f, "UTF-8");
							writer.println(sUser);
							writer.close();
						} catch (IOException e) {
							// do something
						}
						System.out.println("---------------------------------------------------------");
					}
				}

			} catch (Exception e) {
				retVal.setFailed(true);
				retVal.setErrCode(ErrorBean.FILE_ERROR);
				e.printStackTrace();
			}
		} else {
			Map<String, User> map = new CaseInsensitiveMap<>();
			map.put(u.getUsername(), u);
			System.out.println("---------------------------------------------------------");
			System.out.println("Registriran korisnik: " + u.getUsername());
			String sUser = "";
			try {
				sUser = mapper.writeValueAsString(map);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				PrintWriter writer = new PrintWriter(f, "UTF-8");
				writer.println(sUser);
				writer.close();
			} catch (IOException e) {
				// do something
			}
			System.out.println("---------------------------------------------------------");
		}
		return retVal;
	}

	@POST
	@Path("/setHeader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setHeader(StringBean s) {
		boolean retVal = false;
		System.out.println("setHeader-------------------------------------------------------------");
		System.out.println(s.getHeader() + s.getValue());
		if (s.getHeader().equals("subforum")) {
			File f = FileUtilities.getSubforumsFile();
			ObjectMapper mapper = new ObjectMapper();
			try {
				System.out.println("try");
				Map<String, Subforum> map = mapper.readValue(f,
						new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
						});
				// Subforum sf = map.get(s.getValue());
				request.getSession().setAttribute(s.getHeader(), map.get(s.getValue()));
				retVal = true;
			} catch (FileNotFoundException fe) {
				System.out.println(fe.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("subforum header set err");
				retVal = false;
			}
		} else if (s.getHeader().equals("user")) {
			File f = FileUtilities.getUsersFile();
			ObjectMapper mapper = new ObjectMapper();
			try {
				HashMap<String, User> map = mapper.readValue(f, new TypeReference<HashMap<String, User>>() {
				});
				System.out.println(map.get(s.getValue()).getUsername());
				System.out.println("StringBean: " + s.getHeader() + " " + s.getValue());
				request.getSession().setAttribute(s.getHeader(), map.get(s.getValue()));
				retVal = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.err.println("user header set err");
				retVal = false;
			}
		} else if (s.getHeader().equals("topic")) {
			Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
			File f = FileUtilities.getTopicsFile(sf.getTitle());
			Map<String, Topic> map = new CaseInsensitiveMap<>();
			ObjectMapper mapper = new ObjectMapper();
			try {
				map = mapper.readValue(f, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				System.out.println("StringBean: " + s.getHeader() + " " + s.getValue());
				request.getSession().setAttribute(s.getHeader(), map.get(s.getValue()));
				retVal = true;
			} catch (IOException e) {
				System.err.println("topic header set err");
				retVal = false;
			}
		}
		System.out.println("setHeaderEND-------------------------------------------------------------");
		return retVal;
	}

	@GET
	@Path("/getHeader/{headerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public StringBean getHeader(@PathParam("headerName") String s) {
		StringBean sb = new StringBean();
		String retVal = null;
		Enumeration<String> e = request.getSession().getAttributeNames();
		while (e.hasMoreElements()) {
			String param = e.nextElement();
			if (param.equals(s)) {
				if (s.equals("user")) {
					User u = (User) request.getSession().getAttribute(s);

					System.out.println(u.equals(null));
					if (u.equals(null)) {
						retVal = "";
					} else {
						retVal = u.getUsername();
					}
				} else if (s.equals("subforum")) {
					Subforum sf = (Subforum) request.getSession().getAttribute(s);
					if (sf.equals(null)) {
						retVal = "";
					} else {
						retVal = sf.getTitle();
					}
				}
				sb.setHeader(s);
				sb.setValue(retVal);
			}
		}
		return sb;
	}

	@POST
	@Path("/changeUserType")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorBean changeUserType(StringBean sb) {
		ErrorBean er = new ErrorBean();
		System.out.println(sb.getHeader() + sb.getValue());
		File f = FileUtilities.getUsersFile();
		File subforumsFile = FileUtilities.getSubforumsFile();
		ObjectMapper mapper = new ObjectMapper();
		if (f.exists()) {
			try {
				HashMap<String, User> tmp = new HashMap<>();
				tmp = mapper.readValue(f, new TypeReference<HashMap<String, User>>() {
				});
				Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
				if (!subforumsFile.exists()) {
					String sSubforum = mapper.writeValueAsString(subforums);
					PrintWriter writer = new PrintWriter(subforumsFile, "UTF-8");
					writer.println(sSubforum);
					writer.close();
				} else {
					subforums = mapper.readValue(subforumsFile,
							new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
							});
				}
				if (tmp.get(sb.getHeader()).getUserType().equals(User.NORMAL)) {
					tmp.get(sb.getHeader()).setUserType(sb.getValue());
				} else {
					for (String s : subforums.keySet()) {
						ArrayList<String> moderators = subforums.get(s).getModerators();
						if (moderators.contains(sb.getHeader()) && (moderators.size() == 1)) {
							er.setFailed(true);
							er.setErrCode(ErrorBean.SUBFORUM_ERROR);
							return er;
						} else if (moderators.contains(sb.getHeader()) && moderators.size() > 1) {
							tmp.get(sb.getHeader()).setUserType(sb.getValue());
							for (int i = 0; i < moderators.size(); i++) {
								if (moderators.get(i).equals(sb.getHeader())) {
									moderators.remove(i);
									subforums.get(s).setModerators(moderators);
								}
							}
						}
					}
					if (!er.isFailed()) {
						String sSubforum = mapper.writeValueAsString(subforums);
						PrintWriter writer = new PrintWriter(subforumsFile, "UTF-8");
						writer.println(sSubforum);
						writer.close();
					}
				}
				System.out.println("4");
				String sUser = mapper.writeValueAsString(tmp);
				PrintWriter pw = new PrintWriter(f, "UTF-8");
				pw.println(sUser);
				pw.close();
				System.out.println("tryEnd----------------------");
				return er;
			} catch (IOException e) {
				// TODO: handle exception
				// e.printStackTrace();
				er.setFailed(true);
				er.setErrCode(ErrorBean.FILE_ERROR);
				return er;
			}
		} else {
			er.setFailed(true);
			er.setErrCode(ErrorBean.SERVER_ERROR);
			return er;
		}
	}

	@GET
	@Path("/hasPriviledge")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean hasPriviledge() {
		boolean retVal = false;
		Enumeration<String> e = request.getSession().getAttributeNames();
		while (e.hasMoreElements()) {
			String param = e.nextElement();
			if (param.equals("user")) {
				User u = (User) request.getSession().getAttribute("user");
				if (u.equals(null)) {
					retVal = false;
				} else if (u.getUserType().equals(User.ADMIN) || u.getUserType().equals(User.MODERATOR)) {
					retVal = true;
				}
				return retVal;
			}
		}
		return retVal;
	}

	@GET
	@Path("/isUserType/{userType}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isUserType(@PathParam("userType") String userType) {
		Enumeration<String> e = request.getSession().getAttributeNames();
		while (e.hasMoreElements()) {
			String param = e.nextElement();
			if (param.equals("user")) {
				User u = (User) request.getSession().getAttribute("user");
				if (u.getUserType().equals(userType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@GET
	@Path("/sfDelete")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteSf() {
		User u = (User) request.getSession().getAttribute("user");
		if (u.getUserType().equals(User.ADMIN))
			return true;
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		if (sf.getModerators().get(0).equals(u.getUsername()))
			return true;
		return false;
	}

	@GET
	@Path("/subscribed")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isSubscribed() {
		System.out.println("subscribed");
		User u = (User) request.getSession().getAttribute("user");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		ArrayList<String> s = u.getSubforum();
		for (int i = 0; i < s.size(); i++) {
			if (s.get(i).equals(sf.getTitle()))
				return true;
		}
		return false;
	}

	@POST
	@Path("/subscribe")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean subscribe() {
		User u = (User) request.getSession().getAttribute("user");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		File usersFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, User> users = new HashMap<>();
		ArrayList<String> subs = u.getSubforum();
		subs.add(sf.getTitle());
		try {
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			users.get(u.getUsername()).setSubforum(subs);
			String sUsers;
			sUsers = mapper.writeValueAsString(users);
			PrintWriter writer;
			writer = new PrintWriter(usersFile, "UTF-8");
			writer.println(sUsers);
			writer.close();
			return true;
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return false;
	}

	@POST
	@Path("/unsubscribe")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean unsubscribe() {
		User u = (User) request.getSession().getAttribute("user");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		File usersFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, User> users = new HashMap<>();
		ArrayList<String> subs = u.getSubforum();
		int i = 0;
		for (; i < subs.size(); i++) {
			if (subs.get(i).equals(sf.getTitle()))
				break;
		}
		subs.remove(i);
		try {
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			users.get(u.getUsername()).setSubforum(subs);
			String sUsers;
			sUsers = mapper.writeValueAsString(users);
			PrintWriter writer;
			writer = new PrintWriter(usersFile, "UTF-8");
			writer.println(sUsers);
			writer.close();
			return true;
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return false;
	}

	@GET
	@Path("/getRating")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer> getRating() {
		ArrayList<Integer> rating = new ArrayList<>();
		User u = (User) request.getSession().getAttribute("user");
		if (u.equals(null))
			return rating;
		rating.add(u.getPozTotal());
		rating.add(u.getNegTotal());
		return rating;
	}

	@GET
	@Path("/getSubforums")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Subforum> getSubforums() {
		User u = (User) request.getSession().getAttribute("user");
		File f = FileUtilities.getSubforumsFile();
		System.out.println(f.getAbsolutePath());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> tmp = new CaseInsensitiveMap<>();
		Map<String, Subforum> retVal = new CaseInsensitiveMap<>();
		ArrayList<String> subforums = u.getSubforum();
		System.out.println(subforums.size());
		if (f.exists()) {
			try {
				tmp = mapper.readValue(f, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
				});
				System.out.println(tmp.size());
				for (String s : tmp.keySet()) {
					for (int i = 0; i < subforums.size(); i++) {
						if (subforums.get(i).equals(s)) {
							retVal.put(s, tmp.get(s));
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}

	@POST
	@Path("/saveComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean saveComment(LikeBean lb) {
		System.out.println("saveComment----------------------------------------------");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		User u = (User) request.getSession().getAttribute("user");
		SaveCommentBean scb = new SaveCommentBean();
		scb.setId(lb.getId());
		scb.setSubforum(sf.getTitle());
		scb.setTopic(t.getTitle());
		File userFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<SaveCommentBean> savedComm = new ArrayList<>();
		try {
			boolean flag = true;
			savedComm = u.getSavedComments();
			for (SaveCommentBean s : savedComm) {
				if ((s.getId() == scb.getId() && s.getSubforum().equals(scb.getSubforum())
						&& s.getTopic().equals(scb.getTopic()))) {
					flag = false;
					break;
				}
			}
			if (flag)
				savedComm.add(scb);
			HashMap<String, User> users = mapper.readValue(userFile, new TypeReference<HashMap<String, User>>() {
			});
			users.get(u.getUsername()).setSavedComments(savedComm);
			String sUsers = mapper.writeValueAsString(users);
			PrintWriter writer = new PrintWriter(userFile, "UTF-8");
			writer.println(sUsers);
			writer.close();
			System.out.println("saveCommentEND----------------------------------------------");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/saveMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean saveMessage(NumberBean index) {
		ArrayList<Integer> msgsIndex = new ArrayList<>();
		User u = (User) request.getSession().getAttribute("user");
		File userFile = FileUtilities.getUsersFile();
		HashMap<String, User> users = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			users = mapper.readValue(userFile, new TypeReference<HashMap<String, User>>() {
			});
			msgsIndex = u.getSavedMessages();
			if (!msgsIndex.contains(index.getValue())) {
				System.out.println("JEBENI IF");
				msgsIndex.add(index.getValue());
			}
			users.get(u.getUsername()).setSavedMessages(msgsIndex);
			String sUsers = "";
			sUsers = mapper.writeValueAsString(users);
			PrintWriter writer = new PrintWriter(userFile);
			writer.print(sUsers);
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/saveTopic")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean saveTopic(StringBean sb) {
		User u = (User) request.getSession().getAttribute("user");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		File userFile = FileUtilities.getUsersFile();
		ArrayList<SaveTopicBean> savedTopics = new ArrayList<>();
		HashMap<String, User> users = new HashMap<>();
		SaveTopicBean stb = new SaveTopicBean(sb.getValue(), sf.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		try {
			savedTopics = u.getSavedTopics();
			boolean flag = true;
			for (SaveTopicBean s : savedTopics) {
				if ((s.getTopic().equals(stb.getTopic()) && s.getSubforum().equals(stb.getSubforum()))) {
					flag = false;
					break;
				}
			}
			if (flag)
				savedTopics.add(stb);
			users = mapper.readValue(userFile, new TypeReference<HashMap<String, User>>() {
			});
			users.get(u.getUsername()).setSavedTopics(savedTopics);
			String usersString = "";
			usersString = mapper.writeValueAsString(users);
			PrintWriter pw = new PrintWriter(userFile, "UTF-8");
			pw.println(usersString);
			pw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@GET
	@Path("/getSaved")
	@Produces(MediaType.APPLICATION_JSON)
	public SavedBean getSaved() {
		SavedBean sb = new SavedBean();
		User u = (User) request.getSession().getAttribute("user");

		ArrayList<SaveCommentBean> savedCommsId = new ArrayList<>();
		ArrayList<Comment> commentsRetVal = new ArrayList<>();
		ArrayList<Message> messagesRetVal = new ArrayList<>();
		Map<String, Topic> topicsRetVal = new CaseInsensitiveMap<>();
		ArrayList<Integer> messagesIndex = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		try {
			savedCommsId = u.getSavedComments();
			for (int i = 0; i < savedCommsId.size(); i++) {
				File commsFile = FileUtilities.getCommentFile(savedCommsId.get(i).getSubforum(),
						savedCommsId.get(i).getTopic());
				ArrayList<Comment> comments = mapper.readValue(commsFile, new TypeReference<ArrayList<Comment>>() {
				});
				System.out.println(i);
				commentsRetVal.add(comments.get(i));
			}

			ArrayList<SaveTopicBean> topicList = u.getSavedTopics();

			for (int i = 0; i < topicList.size(); i++) {
				File topicFile = FileUtilities.getTopicsFile(topicList.get(i).getSubforum());
				Map<String, Topic> topics = mapper.readValue(topicFile,
						new TypeReference<CaseInsensitiveMap<String, Topic>>() {
						});
				topicsRetVal.put(topicList.get(i).getTopic(), topics.get(topicList.get(i).getTopic()));
			}

			messagesIndex = u.getSavedMessages();
			if (!messagesIndex.isEmpty())

			{
				File msgsFile = FileUtilities.getUsersMsgsFile(u.getUsername());
				ArrayList<Message> messages = mapper.readValue(msgsFile, new TypeReference<ArrayList<Message>>() {
				});
				for (int i = 0; i < messagesIndex.size(); i++) {
					messagesRetVal.add(messages.get(i));
				}
			}
			sb.setComments(commentsRetVal);
			sb.setTopics(topicsRetVal);
			sb.setMessages(messagesRetVal);
		} catch (IOException e) {
			sb = new SavedBean();
		}
		return sb;
	}

	@GET
	@Path("/getUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, User> getUsers() {
		File usrFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, User> users = new HashMap<>();
		try {
			users = mapper.readValue(usrFile, new TypeReference<HashMap<String, User>>() {
			});
			return users;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return users;
		}
	}

	@POST
	@Path("/logoff")
	public void logoff() {
		request.getSession().removeAttribute("user");
	}

	@GET
	@Path("/search/{searchString}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, User> search(@PathParam("searchString") String searchString) {
		HashMap<String, User> retVal = new HashMap<>();
		HashMap<String, User> users = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		File usersFile = FileUtilities.getUsersFile();
		try {
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			for (String s : users.keySet()) {
				if (StringUtilities.containsString(users.get(s).getUsername(), searchString, false))
					retVal.put(s, users.get(s));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}

}
