/**
 * Created by 14334 on 2017/4/20.
 */

public class Main {
    public static void main(String[] args) {
        int arr[] = {1,3,2,7,3,6,1,9};
        NumberSort num = new NumberSort();
        num.bubbleSort(arr);
        for(int i = 0;i < arr.length; i++){
            System.out.println(arr[i]);
        }
    }
}
