package utils;

import java.io.File;

public class FileUtilities {

	public static File getUsersFile() {
		String t = System.getProperty("user.dir");
		String ts = t.substring(0, t.lastIndexOf(File.separatorChar, t.lastIndexOf(File.separatorChar) - 1));
		ts = ts.concat(File.separatorChar + "resources" + File.separatorChar + "users.json");
		File f = new File(ts);
		return f;
	}

	public static File getSubforumsFile() {
		String t = System.getProperty("user.dir");
		String ts = t.substring(0, t.lastIndexOf(File.separatorChar, t.lastIndexOf(File.separatorChar) - 1));
		ts = ts.concat(File.separatorChar + "resources" + File.separatorChar + "subforums.json");
		File f = new File(ts);
		return f;
	}

	public static File getSubforumDir(String dir) {
		String t = System.getProperty("user.dir");
		String ts = t.substring(0, t.lastIndexOf(File.separatorChar, t.lastIndexOf(File.separatorChar) - 1));
		ts = ts.concat(File.separatorChar + "resources" + File.separatorChar + "subforums" + File.separatorChar + dir);
		File sfFile = new File(ts);
		return sfFile;
	}

	public static File getTopicsFile(String subforum) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		ts = ts.concat(File.separatorChar + "resources" + File.separatorChar + "subforums" + File.separatorChar
				+ subforum + File.separatorChar + "topics.json");
		File f = new File(ts);
		return f;
	}

	public static File getTopicsDir(String subforum, String topic) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		ts = ts.concat(File.separatorChar + "resources" + File.separatorChar + "subforums" + File.separatorChar
				+ subforum + File.separatorChar + topic);
		File f = new File(ts);
		return f;
	}

	public static File getCommentFile(String subforum, String topic) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		String temp = ts.concat(File.separatorChar + "resources" + File.separatorChar + "subforums" + File.separatorChar
				+ subforum + File.separatorChar + topic + File.separatorChar + "comments.json");
		File f = new File(temp);
		return f;
	}

	public static File getCommChildrenFile(String subforum, String topic, String id) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		String temp = ts.concat(File.separatorChar + "resources" + File.separatorChar + "subforums" + File.separatorChar
				+ subforum + File.separatorChar + topic + File.separatorChar + id + ".json");
		File f = new File(temp);
		return f;
	}

	public static File getUsersDir(String user) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		String temp = ts
				.concat(File.separatorChar + "resources" + File.separatorChar + "users" + File.separatorChar + user);
		File f = new File(temp);
		return f;
	}

	public static File getUsersSavedCommsFile(String user) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		String temp = ts.concat(File.separatorChar + "resources" + File.separatorChar + "users" + File.separatorChar
				+ user + File.separatorChar + user + "-comms.json");
		File f = new File(temp);
		return f;
	}

	public static File getUsersMsgsFile(String user) {
		String s = System.getProperty("user.dir");
		String ts = s.substring(0, s.lastIndexOf(File.separatorChar, s.lastIndexOf(File.separatorChar) - 1));
		String temp = ts.concat(File.separatorChar + "resources" + File.separatorChar + "users" + File.separatorChar
				+ user + File.separatorChar + user + "-msgs.json");
		File f = new File(temp);
		return f;
	}
	

}
