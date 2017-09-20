package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

import com.groupbyinc.common.jackson.core.JsonProcessingException;
import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.databind.ObjectMapper;

import beans.Comment;
import beans.LikeBean;
import beans.Subforum;
import beans.Topic;
import beans.User;
import utils.FileUtilities;

@Path("/comments")
public class CommentServices {

	@Context
	HttpServletRequest request;

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getComments() {
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic topic = (Topic) request.getSession().getAttribute("topic");
		File f = FileUtilities.getCommentFile(sf.getTitle(), topic.getTitle());
		ArrayList<Comment> comm = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			comm = mapper.readValue(f, new TypeReference<ArrayList<Comment>>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comm;
	}

	@GET
	@Path("/getChildren/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getChildren(@PathParam("id") String comm) {
		ArrayList<Comment> children = new ArrayList<>();
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic topic = (Topic) request.getSession().getAttribute("topic");
		File f = FileUtilities.getCommChildrenFile(sf.getTitle(), topic.getTitle(), comm);
		ObjectMapper mapper = new ObjectMapper();
		try {
			children = mapper.readValue(f, new TypeReference<ArrayList<Comment>>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return children;
	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean newComment(Comment comm) {
		System.out.println("newComm--------------------------------------------");
		boolean retVal = true;
		comm.setAuthor(((User) request.getSession().getAttribute("user")).getUsername());
		comm.setParentCom(null);
		comm.setParentComId(-1);
		comm.setChildren(new ArrayList<String>());
		comm.setUsrResp(new ArrayList<String>());
		Topic t = (Topic) request.getSession().getAttribute("topic");
		if (!t.getComments()) {
			t.setComments(true);
			request.getSession().setAttribute("topic", t);
			File top = FileUtilities
					.getTopicsFile(((Subforum) request.getSession().getAttribute("subforum")).getTitle());
			ObjectMapper obj = new ObjectMapper();
			try {
				Map<String, Topic> topicMap = obj.readValue(top, new TypeReference<CaseInsensitiveMap<String, Topic>>() {
				});
				topicMap.get(t.getTitle()).setComments(true);
				String sTopic = "";
				sTopic = obj.writeValueAsString(topicMap);
				PrintWriter writer = new PrintWriter(top, "UTF-8");
				writer.println(sTopic);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		comm.setParent(t);
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		File f = FileUtilities.getCommentFile(sf.getTitle(), t.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comms = new ArrayList<>();
		if (f.exists()) {
			try {
				comms = mapper.readValue(f, new TypeReference<ArrayList<Comment>>() {
				});
				comm.setId(comms.size());
				comms.add(comm);
				String sComm = mapper.writeValueAsString(comms);
				PrintWriter writer = new PrintWriter(f, "UTF-8");
				writer.println(sComm);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.err.println("add comm error");
				retVal = false;
			}
		} else {
			comm.setId(comms.size());
			comms.add(comm);
			String sComm;
			try {
				sComm = mapper.writeValueAsString(comms);
				PrintWriter writer;
				writer = new PrintWriter(f, "UTF-8");
				writer.println(sComm);
				writer.close();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				retVal = false;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retVal = false;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retVal = false;
			}
		}
		System.out.println("newCommEND--------------------------------------------");
		return retVal;
	}

	@POST
	@Path("/reply")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean reply(Comment comm) {
		boolean retVal = true;
		System.out.println("REPLY----------------------------------------------------");
		int i = comm.getParentComId();
		comm.setAuthor(((User) request.getSession().getAttribute("user")).getUsername());
		Topic t = (Topic) request.getSession().getAttribute("topic");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		File f = FileUtilities.getCommentFile(sf.getTitle(), t.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comms = new ArrayList<>();
		try {
			comms = mapper.readValue(f, new TypeReference<ArrayList<Comment>>() {
			});
			comm.setId(comms.size());
			comm.setParentCom(comms.get(i));
			comm.setChildren(new ArrayList<String>());
			comm.setParent(t);
			comm.setChanged(false);
			comm.setNeg(0);
			comm.setPoz(0);
			ArrayList<String> cl = new ArrayList<>();
			cl.add(Integer.toString(comm.getId()));
			comms.get(i).setChildren(cl);
			comms.get(i).setUsrResp(cl);
			comms.add(comm);

			String sComms = mapper.writeValueAsString(comms);
			PrintWriter writer;
			writer = new PrintWriter(f, "UTF-8");
			writer.println(sComms);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FileOutputStream fos;
			try {
				File fsad = new File("exception.txt");
				System.out.println(fsad.getAbsolutePath());
				fos = new FileOutputStream(fsad, true);
				PrintStream ps = new PrintStream(fos);
				e.printStackTrace(ps);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("REPLY ERROR");
			retVal = false;
		}
		return retVal;
	}

	@POST
	@Path("/like")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean like(LikeBean lb) {
		boolean retVal = true;
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		User u = (User)request.getSession().getAttribute("user");
		File commsFile = FileUtilities.getCommentFile(sf.getTitle(), t.getTitle());
		File usersFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comms = new ArrayList<>();
		HashMap<String, User> users = new HashMap<>();
		try {
			comms = mapper.readValue(commsFile, new TypeReference<ArrayList<Comment>>() {
			});
			
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			
			int likes = (comms.get(lb.getId())).getPoz();
			likes++;
			comms.get(lb.getId()).setPoz(likes);
			ArrayList<String> sss = comms.get(lb.getId()).getUsrResp();
			sss.add(lb.getAuthor());
			comms.get(lb.getId()).setUsrResp(sss);;
			likes = 0;
			likes = users.get(u.getUsername()).getPozTotal();
			likes++;
			users.get(u.getUsername()).setPozTotal(likes);
			
			String sComms = mapper.writeValueAsString(comms);
			String sUsers = mapper.writeValueAsString(users);
			
			PrintWriter writer;
			writer = new PrintWriter(commsFile, "UTF-8");
			writer.println(sComms);
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
	public boolean dislike(LikeBean lb) {
		boolean retVal = true;
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		User u = (User)request.getSession().getAttribute("user");
		File commsFile = FileUtilities.getCommentFile(sf.getTitle(), t.getTitle());
		File usersFile = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comms = new ArrayList<>();
		HashMap<String, User> users = new HashMap<>();
		try {
			comms = mapper.readValue(commsFile, new TypeReference<ArrayList<Comment>>() {
			});
			
			users = mapper.readValue(usersFile, new TypeReference<HashMap<String, User>>() {
			});
			
			int dislikes = (comms.get(lb.getId())).getNeg();
			dislikes++;
			comms.get(lb.getId()).setNeg(dislikes);
			ArrayList<String> sss = comms.get(lb.getId()).getUsrResp();
			sss.add(lb.getAuthor());
			comms.get(lb.getId()).setUsrResp(sss);;
			dislikes = 0;
			dislikes = users.get(u.getUsername()).getNegTotal();
			dislikes++;
			users.get(u.getUsername()).setNegTotal(dislikes);
			
			String sComms = mapper.writeValueAsString(comms);
			String sUsers = mapper.writeValueAsString(users);
			
			PrintWriter writer;
			writer = new PrintWriter(commsFile, "UTF-8");
			writer.println(sComms);
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteComment(LikeBean lb) {
		boolean retVal = true;
		System.out.println("deleteComm---------------------------------------------");
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");

		File commentsFile = FileUtilities.getCommentFile(sf.getTitle(), t.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comments = new ArrayList<>();
		try {
			System.out.println("TRY");
			comments = mapper.readValue(commentsFile, new TypeReference<ArrayList<Comment>>() {
			});
			comments.get(lb.getId()).setDeleted(true);
			
			String sComments = mapper.writeValueAsString(comments);
			
			PrintWriter writer;
			writer = new PrintWriter(commentsFile, "UTF-8");
			writer.println(sComments);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retVal = false;
			System.out.println("deleteCommErr");
		}
		System.out.println("deleteCommEND---------------------------------------------");
		return retVal;
	}
	
	@POST
	@Path("/edit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean editComment(Comment c) {
		Subforum sf = (Subforum) request.getSession().getAttribute("subforum");
		Topic t = (Topic) request.getSession().getAttribute("topic");
		User u = (User)request.getSession().getAttribute("user");
		
		File commFile = FileUtilities.getCommentFile(sf.getTitle(), t.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Comment> comms = new ArrayList<>();
		try {
			comms = mapper.readValue(commFile, new TypeReference<ArrayList<Comment>>() {
			});

			comms.get(c.getId()).setContent(c.getContent());
			if(!sf.getModerators().get(0).equals(u.getUsername()))
				comms.get(c.getId()).setChanged(true);
			String sComms = mapper.writeValueAsString(comms);
			PrintWriter writer = new PrintWriter(commFile, "UTF-8");
			writer.println(sComms);
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
