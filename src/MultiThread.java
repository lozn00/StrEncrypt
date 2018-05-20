
public class MultiThread {
		static Object object=new Object();
		static	Object object2=new Object();
	
	public static void main(String[] args) {
		
		deathLock();
	}

	private static void deathLock() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (object) {
					System.out.println("线程1 拿了锁1");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("线程1准备拿锁2");
					synchronized (object2) {//由于锁2 被线程2 拿了，线程2 又在等待锁1释放，去拿，所以你不让我我不让你，造成了死锁
						System.out.println("线程1 拿了锁2");
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("线程1 释放了锁2");
				}
				System.out.println("线程1 释放锁1所完毕");
				
				
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.err.println("线程2准备拿锁2");
				synchronized (object2) {
					try {
						System.err.println("线程2了拿锁2");
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.println("线程2准备拿锁1");
					synchronized (object) {
						System.err.println("线程2拿了锁1");
						
					}
					System.err.println("线程2释放了锁1");
					
				}
				System.out.println("线程2释放锁2");
				
				
			}
		}).start();
		
		/*
死锁日志
线程1 拿了锁1
线程2准备拿锁2
线程2了拿锁2
线程1准备拿锁2
线程2准备拿锁1

		解决方法，避免嵌套锁或者加标记判断是否持锁中。
		 */
	}
	
}
