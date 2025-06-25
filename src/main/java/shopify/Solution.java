package shopify;

import java.time.Instant;
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


            System.out.println(Instant.now());
        }
    }
