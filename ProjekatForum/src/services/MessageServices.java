package services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.databind.ObjectMapper;

import beans.ErrorBean;
import beans.Message;
import beans.NumberBean;
import beans.User;
import utils.FileUtilities;

@Path("/message")
public class MessageServices {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorBean sendMsg(Message m) {
		ErrorBean er = new ErrorBean();
		File f = FileUtilities.getUsersFile();
		ObjectMapper mapper = new ObjectMapper();
		User u = (User) request.getSession().getAttribute("user");
		if (u.equals(null)) {
			er.setFailed(true);
			er.setErrCode(ErrorBean.SERVER_ERROR);
			return er;
		}
		m.setSender(u.getUsername());
		try {
			HashMap<String, User> tmp = mapper.readValue(f, new TypeReference<HashMap<String, User>>() {
			});

			if (tmp.containsKey(m.getRecipient())) {
				File msgFile = FileUtilities.getUsersMsgsFile(m.getRecipient());
				ArrayList<Message> msgs = new ArrayList<>();
				if (msgFile.exists()) {
					msgs = mapper.readValue(msgFile, new TypeReference<ArrayList<Message>>() {
					});
					msgs.add(m);
				} else {
					msgs.add(m);
				}
				String msgRetVal = mapper.writeValueAsString(msgs);
				try {
					PrintWriter writer = new PrintWriter(msgFile, "UTF-8");
					writer.println(msgRetVal);
					writer.close();
				} catch (IOException e) {

				}
			} else {
				er.setFailed(true);
				er.setErrCode(ErrorBean.NO_USER);
			}
		} catch (Exception e) {
			er.setFailed(true);
			er.setErrCode(ErrorBean.FILE_ERROR);
			e.printStackTrace();
		}
		return er;
	}

	@GET
	@Path("/inbox")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> getInbox() {
		ArrayList<Message> tmp = new ArrayList<>();
		User u = (User) request.getSession().getAttribute("user");
		File f = FileUtilities.getUsersMsgsFile(u.getUsername());
		if (f.exists()) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				tmp = mapper.readValue(f, new TypeReference<ArrayList<Message>>() {
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tmp;
	}

	@POST
	@Path("/readMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean readMessage(NumberBean index) {
		boolean retVal = true;
		int idx = index.getValue();
		ArrayList<Message> tmp = new ArrayList<>();
		User u = (User) request.getSession().getAttribute("user");
		File f = FileUtilities.getUsersMsgsFile(u.getUsername());
		if (f.exists()) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				tmp = mapper.readValue(f, new TypeReference<ArrayList<Message>>() {
				});
				tmp.get(idx).setRead(true);

				String msgRetVal = mapper.writeValueAsString(tmp);

				try {
					PrintWriter writer = new PrintWriter(f, "UTF-8");
					writer.println(msgRetVal);
					writer.close();
				} catch (IOException e) {
					retVal = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				retVal = false;
			}
		}
		return retVal;
	}

}
