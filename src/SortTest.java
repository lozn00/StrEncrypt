import java.util.Arrays;

public class SortTest {
	public static void main(String[] args) {
		int[] a = { 8, 9, 5, 7, 0, 3,33,55,89,68,48,86,0, 4, 55, 3, 15, 22, 6, 2, 9 };
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(bubbleSort(a)));
	}

	public static int[] selectSort(int[] array) {
		int count = 0;
		for (int i = 0; i < array.length - 1; i++) {//最后一个不需要比较了,
			for (int j = i+1; j < array.length; j++) {
				if (array[i] > array[j]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
				count++;
				
			}
		}
		//21个数组就循环了210次
		System.out.println("arrlength:"+array.length+" compare count:"+count+",理论总数:"+(array.length*array.length)/2);
		return array;
	}
	
	 public static int[] bubbleSort(int[] array) {
			int count = 0;
	        for (int i = 0; i < array.length; i++) {
	            for (int j = 0; j < array.length-i-1; j++) {
	                if (array[j] > array[j+1]) {
	                    int temp = array[j];
	                    array[j] = array[j+1];
	                    array[j+1] = temp;
	                }
	                count++;
	           
	            }
	        }
	        System.out.println("arrlength:"+array.length+"bubble sort compare count:"+count+",理论总数:"+(array.length*array.length)/2);
	        return array;
	    }

}