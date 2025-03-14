package com.bash.shopify;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ShopifyTest {
    class Calc{
     int num1;
     int num2;

     public Calc(int i, int j){
         this.num2 = i;
         this.num1 = j;
     }

     public int sum() {
         return num1+num2;
     }

     public int sum(int i, int j){
         return i+j;
     }
    }

    Calc test = new Calc(1,2);

    @Test
    void addTwoNumbers(){
        int result = test.sum();
        assertThat(result).isEqualTo(3);

    }
}
