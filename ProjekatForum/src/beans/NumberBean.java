package beans;

public class NumberBean {

	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public NumberBean() {
		super();
		this.value = 0;
	}

	public NumberBean(NumberBean bean) {
		super();
		this.value = bean.value;
	}
}
