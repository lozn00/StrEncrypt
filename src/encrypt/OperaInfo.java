package encrypt;

public class OperaInfo {

	@Override
	public String toString() {
		return "OpearaInfo [doWhileResultText=" + doWhileResultText + ", message=" + message + ", result=" + result
				+ ", code=" + code + "]";
	}

	String doWhileResultText;

	public String getDoWhileResultText() {
		return doWhileResultText;
	}

	public void setDoWhileResultText(String doWhileResultText) {
		this.doWhileResultText = doWhileResultText;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	String message;
	boolean result;
	int code;
}