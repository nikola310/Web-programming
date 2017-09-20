package beans;

public class ErrorBean {

	public static String USER_ERROR = "USER_EXISTS";
	public static String EMAIL_ERROR = "EMAIL_EXISTS";
	public static String FILE_ERROR = "FILE_ERROR";
	public static String NO_USER = "NO_USER";
	public static String SERVER_ERROR = "SERVER_ERROR";
	public static String ALREADY_EXISTS_ERROR = "ALREADY_EXISTS_ERROR";
	public static String SUBFORUM_ERROR = "SUBFORUM_ERROR";
	
	private boolean failed;
	private String errCode;

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public ErrorBean(boolean f, String e) {
		failed = f;
		errCode = e;
	}

	public ErrorBean() {
		failed = false;
		errCode = "";
	}

}
