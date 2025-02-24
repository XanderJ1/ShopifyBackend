package shopify;

import java.util.Arrays;
import java.util.Scanner;

public class Solution {
    public static int searchInsert(int[] nums, int target) {
        int k = 0;
        for(int i=0; i<nums.length; i++){
            if(target > nums[i]){
                k++;
            }
        }
        return k;
    }

        public static void main(String[] args) {
            Scanner scan = new Scanner(System.in);
            String [] content = scan.nextLine().split("");
            System.out.println();
            System.out.println(Arrays.toString(content));
            int [] kane = new int[content.length];
            int i =0;
            for (String k: content) {
                kane[i] = Integer.parseInt(k);
                System.out.println(kane[i]);
                i++;
            }
            int kill = searchInsert(kane, 10);
            System.out.println(kill);
        }
    }
