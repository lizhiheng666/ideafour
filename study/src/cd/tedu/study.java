package cd.tedu;

import java.lang.reflect.Array;
import java.util.*;

public class study {
    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        int add1 = sc.nextInt();
        int add2 = sc.nextInt();
        E e = new E();                                        //实例化
        int sum = e.reverseadd(add1, add2);                   //调用reverseadd函数

        System.out.println(sum);
    }

        static class E {
            public int reverseadd(int add1, int add2) {
                ArrayList<Integer> array_add1 = new ArrayList<Integer>();//保存加数1的链表
                ArrayList<Integer> array_add2 = new ArrayList<Integer>();//保存加数2的链表
                while (add1 != 0 && add2 != 0) {                         //分别分离每位上的数字
                    if (add1 != 0) {                                  //分离加数1
                        array_add1.add(add1 % 10);
                        add1 = add1 / 10;
                    }
                    if (add2 != 0) {                                  //分离加数2
                        array_add2.add(add2 % 10);
                        add2 = add2 / 10;
                    }
                }
                int reverseadd1 = 0, reverseadd2 = 0;                    //把链表里的数字重新组合成一个数
                for (int i = 0; i < array_add1.size(); i++) {
                    reverseadd1 = reverseadd1 * 10 + array_add1.get(i);
                }
                for (int i = 0; i < array_add2.size(); i++) {
                    reverseadd2 = reverseadd2 * 10 + array_add2.get(i);
                }

                return reverseadd1 + reverseadd2;                       //返回逆置后的两个加数和
            }
        }

}
