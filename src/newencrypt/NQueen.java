package newencrypt;

import java.util.Random;

public class NQueen {
	
	
    /** 
     * ctong 
     */  
    //可行解数组，从X[1]开始使用  
    private static int[] X ;  
    //X数组的长度  
    private static int nn ;  
    //记录可行解数目  
    private static int sum=0;  
    /** 
     * 迭代算法 
     */  
    public void iteration(int n){  
        int k=1;  
        while(k>0){  
            X[k]++;  
            while(X[k]<n&&!place(k)){  
                X[k]++;  
            }  
                if(X[k]<n){  
                    if(k==n-1){  
                        print();  
                    }  
                    else{  
                        k++;  
                        X[k]=0;  
                    }  
                }else k--;  
        }  
    }  
    /** 
     * 递归算法 
     */  
    public void recursion(int p){  
        for(X[p]=1;X[p]<nn;X[p]++){  
            if(place(p)){  
                if(p==nn-1){  
                    print();  
                }else{  
                    //递归调用  
                    recursion(1+p);  
                }  
            }  
        }  
    }  
    //是否可以放置？  
    public boolean place(int t){  
        for(int i=1;i<t;i++){  
            if(Math.abs(i-t)==Math.abs(X[i]-X[t])||X[i]==X[t])  
                return false;  
        }  
        return true;  
    }  
    //创建一个随机长度(5~8)的数组  
    public static int[] createArray(){  
        nn =new Random().nextInt(3)+5;  
        int[] a = new int[nn];  
        for(int i =0;i<nn;i++)  
            a[i]=0;  
        return a;  
    }  
    //打印函数  
    public void print(){  
        sum++;  
        System.out.println("可行解"+sum);  
        for(int j=1;j<X.length;j++)  
        System.out.print(X[j]+" ");  
        System.out.println();  
    }  
    /** 
     * 主函数 
     * @param args 
     */  
    public static void main(String[] args) {  
        X=createArray();  
        System.out.println("迭代回溯算法求得"+(X.length-1)+"皇后的可行解为:");  
        new NQueen().iteration(X.length);  
        System.out.println("可行解的个数是:"+sum);  
        sum=0;  
        System.out.println("递归算法求得"+(X.length-1)+"皇后的可行解为:");  
        new NQueen().recursion(1);   
        System.out.println("可行解的个数是:"+sum);  
    }  

}
