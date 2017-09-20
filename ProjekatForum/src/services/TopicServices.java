package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.groupbyinc.common.jackson.core.JsonProcessingException;
import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.databind.ObjectMapper;

import beans.ErrorBean;
import beans.StringBean;
import beans.Subforum;
import beans.Topic;
import beans.User;
import utils.FileUtilities;
import utils.StringUtilities;

@Path("/topic")
public class TopicServices {

	@Context
	HttpServletRequest request;

	@GET
	@Path("/getTopics")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> getTopics() {
		System.out.println("getTopics--------------------------------------------------------------------");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		File f = FileUtilities.getTopicsFile(sf.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Topic> tmp = new CaseInsensitiveMap<>();
		if (f.exists()) {
			try {
				tmp = mapper.readValue(f, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("getTopicsEND--------------------------------------------------------------------");
		return tmp;
	}

	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorBean createNew(Topic t) {
		System.out.println("new topic---------------------------------------");
		ErrorBean er = new ErrorBean();
		User u = (User) request.getSession().getAttribute("user");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		t.setParent(sf.getTitle());
		t.setAuthor(u.getUsername());
		t.setComments(false);
		t.setUsrResp(new ArrayList<String>());
		File f = FileUtilities.getTopicsFile(sf.getTitle());
		Map<String, Topic> topics = new CaseInsensitiveMap<>();
		System.out.println(topics.getClass());
		ObjectMapper mapper = new ObjectMapper();
		if (f.exists()) {
			try {
				topics = mapper.readValue(f, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(topics.getClass());
			if (topics.containsKey(t.getTitle())) {
				er.setErrCode(ErrorBean.ALREADY_EXISTS_ERROR);
				er.setFailed(true);
			} else {
				File directory = FileUtilities.getTopicsDir(sf.getTitle(), t.getTitle());
				directory.mkdirs();
				t.setPoz(0);
				t.setNeg(0);
				topics.put(t.getTitle(), t);
			}
		} else {
			topics.put(t.getTitle(), t);
			File directory = FileUtilities.getTopicsDir(sf.getTitle(), t.getTitle());
			directory.mkdirs();
		}

		try {
			String sTopics = mapper.writeValueAsString(topics);
			PrintWriter writer = new PrintWriter(f, "UTF-8");
			writer.println(sTopics);
			writer.close();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er.setFailed(true);
			er.setErrCode(ErrorBean.SERVER_ERROR);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er.setErrCode(ErrorBean.FILE_ERROR);
			er.setFailed(true);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			er.setFailed(true);
			er.setErrCode(ErrorBean.SERVER_ERROR);
		}

		return er;
	}

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Topic getTopic() {
		Topic t = (Topic) request.getSession().getAttribute("topic");
		System.out.println(t.getTitle());
		return t;
	}

	@POST
	@Path("/like")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean like() {
		boolean retVal = true;
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		User u = (User) request.getSession().getAttribute("user");

		File topicsFile = FileUtilities.getTopicsFile(sf.getTitle());
		File usersFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, User> users = new HashMap<>();
		Map<String, Topic> topics = new HashMap<>();

		try {
			topics = mapper.readValue(topicsFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
			});
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			int likes = t.getPoz();
			likes++;
			t.setPoz(likes);
			topics.get(t.getTitle()).setPoz(likes);

			ArrayList<String> st = t.getUsrResp();
			st.add(u.getUsername());
			System.out.println(st.size());
			t.setUsrResp(st);
			topics.get(t.getTitle()).setUsrResp(st);

			likes = users.get(u.getUsername()).getPozTotal();
			likes++;
			users.get(u.getUsername()).setPozTotal(likes);

			String sTopics = mapper.writeValueAsString(topics);
			String sUsers = mapper.writeValueAsString(users);

			PrintWriter writer;
			writer = new PrintWriter(topicsFile, "UTF-8");
			writer.println(sTopics);
			writer.close();

			PrintWriter pw;
			pw = new PrintWriter(usersFile, "UTF-8");
			pw.println(sUsers);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retVal = false;
		}

		return retVal;
	}

	@POST
	@Path("/dislike")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean dislike() {
		boolean retVal = true;
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		User u = (User) request.getSession().getAttribute("user");

		File topicsFile = FileUtilities.getTopicsFile(sf.getTitle());
		File usersFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, User> users = new HashMap<>();
		Map<String, Topic> topics = new HashMap<>();

		try {
			topics = mapper.readValue(topicsFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
			});
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			int dislikes = t.getNeg();
			dislikes++;
			t.setNeg(dislikes);
			topics.get(t.getTitle()).setNeg(dislikes);

			ArrayList<String> st = t.getUsrResp();
			st.add(u.getUsername());
			System.out.println(st.size());
			t.setUsrResp(st);
			topics.get(t.getTitle()).setUsrResp(st);

			dislikes = users.get(u.getUsername()).getNegTotal();
			dislikes++;
			users.get(u.getUsername()).setNegTotal(dislikes);

			String sTopics = mapper.writeValueAsString(topics);
			String sUsers = mapper.writeValueAsString(users);

			PrintWriter writer;
			writer = new PrintWriter(topicsFile, "UTF-8");
			writer.println(sTopics);
			writer.close();

			PrintWriter pw;
			pw = new PrintWriter(usersFile, "UTF-8");
			pw.println(sUsers);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retVal = false;
		}
		return retVal;
	}

	@POST
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteTopic() {
		System.out.println("delete topic");
		boolean retVal = true;
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		System.out.println(sf.getTitle() + t.getTitle());
		File topicsFile = FileUtilities.getTopicsFile(sf.getTitle());
		System.out.println(topicsFile.getAbsolutePath());
		File topicsDir = FileUtilities.getTopicsDir(sf.getTitle(), t.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Topic> topics = new HashMap<>();
		try {
			System.out.println("readvalue");
			topics = mapper.readValue(topicsFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
			});
			topics.remove(t.getTitle());
			if (!topicsDir.delete())
				System.out.println("topic dir not deleted");
			String sTopics = mapper.writeValueAsString(topics);
			System.out.println("writer");
			PrintWriter writer;
			writer = new PrintWriter(topicsFile, "UTF-8");
			writer.println(sTopics);
			writer.close();
			FileUtils.deleteDirectory(topicsDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retVal = false;
		}

		return retVal;
	}

	@GET
	@Path("/canDelete")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean canDelete() {
		User u = (User) request.getSession().getAttribute("user");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		if (t.getAuthor().equals(u.getUsername()))
			return true;
		if (sf.getModerators().contains(u.getUsername()))
			return true;
		if (u.getUserType().equals(User.ADMIN))
			return true;
		return false;
	}

	@GET
	@Path("/editType")
	@Produces(MediaType.APPLICATION_JSON)
	public StringBean editType() {
		User u = (User) request.getSession().getAttribute("user");
		return new StringBean(u.getUserType(), "user");
	}

	@POST
	@Path("/edit")
	public void edit(Topic t) {
		Topic tc = (Topic) request.getSession().getAttribute("topic");
		if (!t.getDatum().equals("")) {
			tc.setDatum(t.getDatum());
		}
		tc.setContent(t.getContent());
		request.getSession().setAttribute("topic", tc);
		File topicFile = FileUtilities
				.getTopicsFile(((Subforum) request.getSession().getAttribute("subforum")).getTitle());
		Map<String, Topic> topics = new CaseInsensitiveMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			topics = mapper.readValue(topicFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
			});
			topics.remove(tc.getTitle());
			topics.put(tc.getTitle(), tc);
			String sTopics = mapper.writeValueAsString(topics);
			PrintWriter pw = new PrintWriter(topicFile, "UTF-8");
			pw.println(sTopics);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GET
	@Path("/search/{searchString}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> search(@PathParam("searchString") String searchString) {
		Map<String, Topic> retVal = new CaseInsensitiveMap<String, Topic>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		File subforumFile = FileUtilities.getSubforumsFile();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			System.out.println("pre for");
			for (String s : subforums.keySet()) {
				File topicFile = FileUtilities.getTopicsFile(s);
				System.out.println(topicFile.getAbsolutePath());
				Map<String, Topic> tmp = new CaseInsensitiveMap<String, Topic>();
				tmp = mapper.readValue(topicFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				if (StringUtilities.containsString(s, searchString, false)) {
					retVal.putAll(tmp);
				} else {
					for (String t : tmp.keySet()) {
						if (StringUtilities.containsString(tmp.get(t).getTitle(), searchString, false)) {
							retVal.put(t, tmp.get(t));
						} else if (StringUtilities.containsString(tmp.get(t).getAuthor(), searchString, false)) {
							retVal.put(t, tmp.get(t));
						} else if (StringUtilities.containsString(tmp.get(t).getContent(), searchString, false)) {
							retVal.put(t, tmp.get(t));
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	@GET
	@Path("/searchTitle/{title}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> searchTitle(@PathParam("title") String title) {
		Map<String, Topic> retVal = new CaseInsensitiveMap<String, Topic>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		File subforumFile = FileUtilities.getSubforumsFile();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				File topicFile = FileUtilities.getTopicsFile(s);
				System.out.println(topicFile.getAbsolutePath());
				Map<String, Topic> tmp = new CaseInsensitiveMap<String, Topic>();
				tmp = mapper.readValue(topicFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				for (String t : tmp.keySet()) {
						if (StringUtilities.containsString(tmp.get(t).getTitle(), title, false)) {
							retVal.put(t, tmp.get(t));
						}
					}
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	@GET
	@Path("/searchContent/{content}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> searchContent(@PathParam("content") String content) {
		Map<String, Topic> retVal = new CaseInsensitiveMap<String, Topic>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		File subforumFile = FileUtilities.getSubforumsFile();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				File topicFile = FileUtilities.getTopicsFile(s);
				System.out.println(topicFile.getAbsolutePath());
				Map<String, Topic> tmp = new CaseInsensitiveMap<String, Topic>();
				tmp = mapper.readValue(topicFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				for (String t : tmp.keySet()) {
						if (StringUtilities.containsString(tmp.get(t).getContent(), content, false)) {
							retVal.put(t, tmp.get(t));
						}
					}
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	@GET
	@Path("/searchAuthor/{author}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> searchAuthor(@PathParam("author") String author) {
		Map<String, Topic> retVal = new CaseInsensitiveMap<String, Topic>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		File subforumFile = FileUtilities.getSubforumsFile();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				File topicFile = FileUtilities.getTopicsFile(s);
				System.out.println(topicFile.getAbsolutePath());
				Map<String, Topic> tmp = new CaseInsensitiveMap<String, Topic>();
				tmp = mapper.readValue(topicFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				for (String t : tmp.keySet()) {
						if (StringUtilities.containsString(tmp.get(t).getAuthor(), author, false)) {
							retVal.put(t, tmp.get(t));
						}
					}
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}

	@GET
	@Path("/searchSubforum/{subforum}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> searchSubforum(@PathParam("subforum") String subforum) {
		Map<String, Topic> retVal = new CaseInsensitiveMap<String, Topic>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		File subforumFile = FileUtilities.getSubforumsFile();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			System.out.println("pre for");
			for (String s : subforums.keySet()) {
				File topicFile = FileUtilities.getTopicsFile(s);
				System.out.println(topicFile.getAbsolutePath());
				Map<String, Topic> tmp = new CaseInsensitiveMap<String, Topic>();
				tmp = mapper.readValue(topicFile, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				if (StringUtilities.containsString(s, subforum, false)) {
					retVal.putAll(tmp);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
}
