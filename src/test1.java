import java.util.ArrayList;
import java.util.Scanner;
/*
求n维数组的最大子数组
时间限制：C/C++语言 1000MS；其他语言 3000MS
内存限制：C/C++语言 65536KB；其他语言 589824KB
题目描述：
设有n维数组，元素分别为a00  a01 ... a0(n-1) a10 a11 ... a1(n-1) ... ... a(n-1)0 a(n-1)1 ... a(n-1)(n-1)，求其相加和最大子数组的值max

输入
n

a00  a01 ... a0(n-1) a10 a11 ... a1(n-1) ... ... a(n-1)0 a(n-1)1 ... a(n-1)(n-1)

（数据范围：n在1到100之间，每个数在-128到127之间）

输出
max


样例输入
2
-1 2 -3 6
样例输出
8
*/
public class test1 {
    public int fun(int[] array){
        if(array.length == 0 || array == null){
            return 0;
        }
        int curSum = 0;
        int greatestSum = Integer.MIN_VALUE;
        for(int i=0; i< array.length;i++){
            if(curSum<=0){
                curSum = array[i];
            }else{
                curSum +=array[i];
            }
            if(curSum > greatestSum){
                greatestSum = curSum;
            }
        }
        return greatestSum;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        ArrayList<Integer> list = new ArrayList<Integer>();
        while(in.hasNext()){
            for(int i=0;i<n;i++){

            }
        }
    }
}
