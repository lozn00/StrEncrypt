import java.util.ArrayList;

public class TestSmaliReturnValue {

	public static void main(String[] args) {
		System.out.println("test");
		ArrayList list=new ArrayList<>();
		TestSmaliReturnValue returnValue=new TestSmaliReturnValue();
		returnValue.isEnablePreventRescinder(list);
	}
	public void testAram(ArrayList list){
		
		TestSmaliReturnValue returnValue=new TestSmaliReturnValue();
		returnValue.isEnablePreventRescinder(list);
		
	}
	public int getInt() {
		return 10000000;
	}

	public short getShort() {
		return 10000;
	}

	public double getDouble() {
		return 0.01;
	}
	public double getDoubleBig() {
		return 1111111111111f;
	}
	public double geFloat() {
		return 0.01;
	}


	public short getByte() {
		return 53;
	}

	public boolean getBoolean() {
		return true;
	}
	

	
	public Object getNull() {
		return null;
	}

	public String getString() {
		return "ddddddd";
	}
	
	public boolean isEnablePreventRescinder(ArrayList list){
		return false;
	}
	
}
