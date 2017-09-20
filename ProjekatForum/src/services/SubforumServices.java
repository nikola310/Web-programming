package services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.groupbyinc.common.jackson.core.JsonProcessingException;
import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.databind.ObjectMapper;

import beans.ErrorBean;
import beans.Subforum;
import beans.Topic;
import beans.User;
import utils.FileUtilities;
import utils.StringUtilities;

@Path("/subforum")
public class SubforumServices {

	@Context
	HttpServletRequest request;

	@Context
	ServletContext ctx;

	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorBean newSubforum(Subforum sf) {
		ErrorBean er = new ErrorBean();
		File sfFile = FileUtilities.getSubforumDir(sf.getTitle());
		User u = (User) request.getSession().getAttribute("user");
		if (u.getUserType().equals(User.NORMAL)) {
			er.setFailed(true);
			er.setErrCode(ErrorBean.SERVER_ERROR);
			return er;
		}

		if (sfFile.mkdirs()) {

		} else {

		}
		File f = FileUtilities.getSubforumsFile();
		ObjectMapper mapper = new ObjectMapper();
		if (f.exists()) {
			try {
				Map<String, Subforum> tmp = new CaseInsensitiveMap<>();
				tmp = mapper.readValue(f, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
				});

				if (tmp.containsKey(sf.getTitle())) {
					er.setFailed(true);
					er.setErrCode(ErrorBean.ALREADY_EXISTS_ERROR);
				} else {
					ArrayList<String> p = new ArrayList<>();
					p.add(u.getUsername());
					sf.setModerators(p);
					request.getSession().setAttribute("subforum", sf);
					tmp.put(sf.getTitle(), sf);
					String subForum = "";
					subForum = mapper.writeValueAsString(tmp);
					try {
						PrintWriter writer = new PrintWriter(f, "UTF-8");
						writer.println(subForum);
						writer.close();
					} catch (IOException e) {
						// TODO: handle exception
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Map<String, Subforum> map = new CaseInsensitiveMap<>();

			ArrayList<String> p = new ArrayList<>();
			p.add(u.getUsername());
			sf.setModerators(p);
			request.getSession().setAttribute("subforum", sf);
			map.put(sf.getTitle(), sf);
			String subForum = "";
			try {
				subForum = mapper.writeValueAsString(map);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				PrintWriter writer = new PrintWriter(f, "UTF-8");
				writer.println(subForum);
				writer.close();
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		return er;
	}

	@GET
	@Path("/getSubforums")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Subforum> getSubforums() {
		File f = FileUtilities.getSubforumsFile();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Subforum> tmp = new CaseInsensitiveMap<>();
		if (f.exists()) {
			try {
				tmp = mapper.readValue(f, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tmp;
	}

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteSubforum() {
		File f = FileUtilities.getSubforumsFile();
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		ObjectMapper mapper = new ObjectMapper();
		if (f.exists()) {
			try {
				Map<String, Subforum> tmp = mapper.readValue(f,
						new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
						});
				if (tmp.remove(sf.getTitle()) != null) {
					String subForum = "";
					subForum = mapper.writeValueAsString(tmp);
					try {
						PrintWriter writer = new PrintWriter(f, "UTF-8");
						writer.println(subForum);
						writer.close();
						FileUtils.deleteDirectory(FileUtilities.getSubforumDir(sf.getTitle()));
					} catch (IOException e) {
						
					}
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	@GET
	@Path("/isResMod")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean getResponsible() {
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		User u = (User) request.getSession().getAttribute("user");
		return sf.getModerators().get(0).equals(u.getUsername());
	}

	@GET
	@Path("/getSubscribed")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Topic> getSubscribed() {
		Map<String, Topic> topicsRetVal = new CaseInsensitiveMap<>();
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			return topicsRetVal;
		}
		ArrayList<String> subscribed = u.getSubforum();
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (int i = 0; i < subscribed.size(); i++) {
				File topicsFile = FileUtilities.getTopicsFile(subscribed.get(i));
				Map<String, Topic> topicsTemp = mapper.readValue(topicsFile,
						new TypeReference<CaseInsensitiveMap<String, Topic>>() {
						});
				for (String string : topicsTemp.keySet()) {
					topicsRetVal.put(string, topicsTemp.get(string));
				}
			}
			return topicsRetVal;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topicsRetVal;
	}

	@GET
	@Path("/search/{searchString}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Subforum> search(@PathParam("searchString") String searchString) {
		File subforumFile = FileUtilities.getSubforumsFile();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		Map<String, Subforum> retVal = new CaseInsensitiveMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				if (StringUtilities.containsString(subforums.get(s).getTitle(), searchString, false)) {
					retVal.put(s, subforums.get(s));
				} else if (StringUtilities.containsString(subforums.get(s).getDescription(), searchString, false)) {
					retVal.put(s, subforums.get(s));
				} else if (StringUtilities.containsString((subforums.get(s).getModerators()).get(0), searchString,
						false)) {
					retVal.put(s, subforums.get(s));
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
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Subforum> searchTitle(@PathParam("title") String title) {
		File subforumFile = FileUtilities.getSubforumsFile();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		Map<String, Subforum> retVal = new CaseInsensitiveMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				if (StringUtilities.containsString(subforums.get(s).getTitle(), title, false)) {
					retVal.put(s, subforums.get(s));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}

	@GET
	@Path("/searchDesc/{desc}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Subforum> searchDesc(@PathParam("desc") String desc) {
		File subforumFile = FileUtilities.getSubforumsFile();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		Map<String, Subforum> retVal = new CaseInsensitiveMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				if (StringUtilities.containsString(subforums.get(s).getDescription(), desc, false)) {
					retVal.put(s, subforums.get(s));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}

	@GET
	@Path("/searchResMod/{resMod}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Subforum> searchResMod(@PathParam("resMod") String resMod) {
		File subforumFile = FileUtilities.getSubforumsFile();
		Map<String, Subforum> subforums = new CaseInsensitiveMap<>();
		Map<String, Subforum> retVal = new CaseInsensitiveMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			subforums = mapper.readValue(subforumFile, new TypeReference<CaseInsensitiveMap<String, Subforum>>() {
			});
			for (String s : subforums.keySet()) {
				if (StringUtilities.containsString((subforums.get(s).getModerators()).get(0), resMod, false)) {
					retVal.put(s, subforums.get(s));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
}
