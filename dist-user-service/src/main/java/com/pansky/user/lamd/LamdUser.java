package com.pansky.user.lamd;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fo
 * @date 2023/1/5 9:50
 */
@Data
@AllArgsConstructor
public class LamdUser {
    private String id;
    private String name;
    private String address;
    private int age;
    private String phone;
    private String sex;
    private List<String> list;

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f","g", "h", "i","j", "k", "l", "m", "n");

        System.out.println("list 大小 " + list.size());

        int batchsize = 3;
        int batchnum = list.size()/batchsize +1;
        System.out.println("总批次 = " + batchnum);
        for (int i = 0; i <batchnum ; i++) {
            List<String> temp = null ;
            if(i<batchnum-1){
                temp = list.subList(batchsize*i, batchsize*(i+1));
            }else{
                temp = list.subList(batchsize*i, list.size());
            }

            System.out.println("temp = " + temp);
        }
    }
}
