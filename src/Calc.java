
public class Calc {

	public static void main(String[] args) {

		int[] a = new int[] { 1,2, 6, 5,7, 8, 9, 14, 10, 22, 43, 333,5555 };
		System.out.println("arr length"+a.length+",search idnex" + binarySearch(a, 333));

	}

	private static int binarySearchMy(int[] arr, int searchValue) {
		int maxIndex=arr.length-1;
		int minIndex=0;
		
		while(minIndex<=maxIndex){
			
			int middleIndex=(minIndex+maxIndex)>>>1;//就是除2
			int middleValue=arr[middleIndex];
			if(middleValue>searchValue){//大于被搜索的值
				maxIndex--;
			}else if(middleValue<searchValue){//小于被搜索的值,
				minIndex++;
			}else{
				return middleIndex;
			}
		}
		return -minIndex;
	}

	public static int binarySearch(int[] a, int key) {
		return binarySearch(a, 0, a.length, key);
	}


/**
 * 
 * 官方的源码刚开始不知道是干嘛的,经过修改明明，把low 改成lowIndex,就瞬间明白了,我这个人容易被假象所迷惑干扰!
 * 二分查找 必须 是有序的,否则 是无法找出正确的值, 方法是通过(最高index+最小index)/2求中间index的方式进行查找，如果中间index的值大于那么 最高index要减去1，如果中间index的值小于 被搜索的值，则加1，
 * 如果不大于也不小于就成立，或者知道lowIndex超过hightIndex二终止退出
 * 
 * @param a
 * @param fromIndex
 * @param toIndex
 * @param key
 * @return
 */
	private static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
		int lowIndex = fromIndex;
		int highIndex = toIndex - 1;
//		int highIndex = toIndex;//为什么减去1 ， 这个不好说-不减去都没关系，有时候不减去找的速度更快，这好比0到11 的中间到底取出5 还是取出6 比如取出6,
		//那么上面有,789 10 11  5个值，下面有6个值 012345 那么取 5 的话，是0 1 2 3 4后面是6 7 8 9 10 11 照样的没法权衡, 
		while (lowIndex <= highIndex) {
			// int mid = (low + high) >>> 1;
			int midIndex = (lowIndex + highIndex) / 2;//根据最大值和最小值求出 中间idnex
			
			float middleIndex=(lowIndex + highIndex) / 2f;
			System.out.println("最小值:minIndex" + lowIndex+"+maxIndex"+highIndex +"="+(lowIndex+highIndex)+",折半index:"+midIndex+",floatIndex:"+middleIndex);
//			System.out.println("最小值:min" + high+"+max"+high +"="+((low + high) >>>1));
			int midVal = a[midIndex];
			if (midVal < key){
				System.out.println("折半index" + midIndex+"的值"+midVal+"小于查找的值"+key+",因此最小index要+1");
				lowIndex = midIndex + 1;
			}
			else if (midVal > key) {
				System.out.println("折半index" + midIndex+"的值"+midVal+"大于查找的值"+key+",因此最大值要-1");
				highIndex = midIndex - 1;
			} else{
				return midIndex; // key found
				
			}
		}
		return -(lowIndex + 1); // key not found.
	}
}
