import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TestReference {
	static class MyObj {
		public MyObj(int no) {
			this.no = no;
		}

		public String[] getCurrent() {
			return current;
		}

		public void setCurrent(String[] current) {
			this.current = current;
		}

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		String[] current = new String[1000];
		int no;

		@Override
		protected void finalize() throws Throwable {
			System.out.println("from:"+no+" :我被回收了我是hasCode：" + hashCode());
			super.finalize();
		}
	}

	/**
	 *Launch configurations for 'TestReference.java'  ->Argments选项卡  Vm argments里面填写参数-Xmx2m -xms2m   
	 * @param args
	 */
	public static void main(String[] args) {
		Object referent = new Object();
		java.util.List<WeakReference<MyObj>> list = new ArrayList<>();
		int loopindex = 0;
	System.err.println("=================================弱引用测试=================================");
			while(loopindex<200){
				
				String[] current=new String[500];
				for (int i = 0; i < current.length; i++) {
					
					current[i]=i+"";
				}
				MyObj obj=new MyObj(loopindex);
				obj.setCurrent(current);
				list.add(new WeakReference<TestReference.MyObj>(obj));
				if(loopindex%5==0&&loopindex>0){
					System.out.println("start================"+loopindex+"================================");
					System.out.println("即将调用调用主动gc,刚刚放进去的对象index:"+loopindex+"还在否:"+list.get(loopindex).get());
					System.gc();
					System.out.println("主动调用gc");
					System.out.println("调用之后刚刚放进去的对象index:"+loopindex+"还在否:"+list.get(loopindex).get());
					System.out.println("调用之后第一个还在否:"+list.get(0).get());
					System.out.println("end================"+loopindex+"================================");
				}
				loopindex++;
			}
			
		
		

		/**
		 *测试调节初始化数组大小为500 或者1000 如果是1千，我们发现即使没有调用System.gc()它已经开始进行回收了,也就是稍微吃紧就会回收了,而改成500之后 第一次loop,没有回收等到调用gc执行完毕start-end逻辑后， 3 ，4,2,1,0都一个一个被回收了，从这里的消息也可以看出，回收虽然不会有限回收刚刚放进去的，但是也不会百分百先回收第一个，或者也许是日志打印和本地native消息不同步导致的。
		 * 
		 */
		
	}

	private static void assertNotNull(Object object) {
		// TODO Auto-generated method stub
		System.out.println("obj 是否为空:" + object);
	}
}
