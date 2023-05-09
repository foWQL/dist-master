package com.pansky.user.lamd;

import org.junit.Test;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;


/**
 * @author Fo
 * @date 2023/1/5 9:48
 */
public class LambdaTest {
    List<LamdUser> userList = Arrays.asList(
            new LamdUser("1","张三", "北京朝阳十八里屯", 24, "15860307895", "W", Arrays.asList("g1", "g2")),
            new LamdUser("2","李四", "南京外城烟雨台", 20, "13959606235", "W", Arrays.asList("g3", "g4", "g5")),
            new LamdUser("2","王五", "漳州金庭馆", 28, "15860301597", "M", Arrays.asList("g6")),
            new LamdUser("3","赵四", "厦门五缘湾", 36, "15860301547", "W", Arrays.asList("g7", "g8", "g9", "g10")),
            new LamdUser("3","海大富", "漳州金宾馆", 45, "15860312013", "M", Arrays.asList("g11", "g12", "g13"))
    );

    List<LamdUser> tempList = Arrays.asList(
            new LamdUser("22","AA", "Tokyo", 24, "15860307895", "W", Arrays.asList("g1", "g2")),
             new LamdUser("33","BB", "Tokyo", 28, "15860301597", "M", Arrays.asList("g6"))
     );

    /**
     * filter : 条件过滤 类似于if,过滤后还是当前流
     */
    @Test
    public void streamFilter(){

        //中间操作filter的作用是过滤年龄大于24的User，返回新的流
        Stream<LamdUser> userStream = userList.stream().filter(user ->
                user.getAge() > 24); //如果没有值没有大于24 就没有返回，相当于进行下一次循环

        //条件过滤后得到一个新的list
        List<LamdUser> list2 = userList.stream().filter(user -> {
            return user.getAge() > 23 ;
        }).collect(Collectors.toList());

        List<LamdUser> list3 = userList.stream().filter(user ->user.getAge() > 23).collect(Collectors.toList());

        //userStream是过滤后获得的新的流 ， 对原users不产生改变
        userStream.forEach(x -> System.out.println("过滤后"+x.getAge()));

        userList.stream().distinct().forEach(System.out::println);
        System.out.println("-------------------------");
        list2.stream().forEach(System.out::println);
    }

    /**
     * filter+limit  注意limit位置先后结果的不同
     */
    @Test
    public void streamFilter2() {
        userList.stream().limit(1).filter(u -> {
            System.out.println("age:" + u.getAge());
            return u.getAge() > 24 ;
        }).forEach(System.out::println);

        System.out.println("-------------------------");

        userList.stream().filter(u -> {
            System.out.println("age:" + u.getAge());
            return u.getAge() > 24 ;
        }).limit(1).forEach(System.out::println);

        /**
         * 输出结果：
         * age:24
         * -------------------------
         * age:24
         * age:20
         * age:28
         * TempUser{name='王五', address='漳州', age=28, tel='15860301597', sex='M', girlfriends=[g6]}
         */
        /**
         * 中间操作是流水线形式的，即不是中间操作完成才进行终结操作，而是执行完一条中间操作接着执行一条终结操作。以此循环
         * 第二次情形，当28大于24，返回，则接着就是limit(1),还有很多大于24的对象，但是filter里面只执行完一条中间操作返回之后接着就是下一个limit操作了，这个可以验证上面的说法
         */


    }

    /*
     *  map：把一个元素类型为 T 的流转换成元素类型为 R 的流。
     * 收集(stream的终止操作之一，forEach，reduce等也是)
     *  collect：将流转换为其他形式，接收一个collection接口的实现，用于给stream元素做汇总的方法
     */
    @Test
    public void listTest(){
        //map是数据转换，将List<TempUser> 转换为List<String> ,  存储的值是User.getName
        List<String> list1 = userList.stream().map(LamdUser::getName).collect(Collectors.toList());
        list1.forEach(System.out::println);

        //转换为set , 存储的值是User.getName
        Set<String> set1 = userList.stream().map(LamdUser::getName).collect(Collectors.toSet());
        set1.forEach(System.out::println);

        //把list转为其它的数据结构，这里是hashset
        HashSet<String> hashset = userList.stream().map(LamdUser::getName).collect(Collectors.toCollection(HashSet::new));
        hashset.forEach(System.out::println);

    }

    /**
     * 接收一个函数作为参数，此函数作用到Stream中每一个元素，形成一个新的元素，所有新的元素组成一个新的流。
     * map : 使用map过滤的内容,map 中 使用的是当前该对象通用的内容,可以转成map集合 通用类型（共性），或者转成数据流。
     * @param
     */
    @Test
    public void listToSet(){
//        List<String> collectList = userList.stream().map(String::length).collect(Collectors.toList());

        Set<String> set = userList.stream()
                .map(u -> u.getName())
                .filter(s -> s.contains("四"))
                .collect(Collectors.toSet());
        set.stream().forEach(System.out::println);

    }

    /**
     * list转为map
     */
    @Test
    public void listToMap(){
        //第一种（会出现键重复异常）：
        //以id为主键转为map
        /*Map<String, String> map1 = userList.stream().collect(Collectors.toMap(TempUser::getId, TempUser::getName));
        for(Map.Entry<String,String>  entry: map1.entrySet()){
            System.out.println(entry.getKey() + " : "+entry.getValue());
        }*/

        //给出key重复时，使用哪个key作为主键，以下代码中的(id1, id2) -> id2)代表id1和id2键重复时返回id2做主键
        // 第二种（返回整个对象）：
        Map<String, LamdUser> map2 = userList.stream().collect(Collectors.toMap(LamdUser::getId, Function.identity() ,(id1, id2)->id2 ));
        for(Map.Entry<String, LamdUser>  entry: map2.entrySet()){
            System.out.println(entry.getKey() + " : "+entry.getValue());
        }

        //第三种（同一， 但是可以处理重复的key：(id1, id2) -> id2)，重复时返回后一个）：
        Map<String, String> map3 = userList.stream().collect(Collectors.toMap(LamdUser::getId, LamdUser::getName ,(id1, id2)->id2 ));
        for(Map.Entry<String,String>  entry: map3.entrySet()){
            System.out.println(entry.getKey() + " : "+entry.getValue());
        }
    }


    /**
     * 分组
     */
    @Test
    public void groupBy(){

        //以某个属性分组
        Map<String, List<LamdUser>> map = userList.stream().collect(Collectors.groupingBy(LamdUser::getId));
        System.out.println("collectors分组功能， map:" + map);

        //多级分组 先按sex分组，分完组之后，在每个组里面再进行分一次组
        Map<String, Map<String, List<LamdUser>>> collect = userList.stream().collect(Collectors.groupingBy(
                LamdUser::getSex, Collectors.groupingBy(u -> {
                    if (u.getAge() <= 24) {
                        return "happy";
                    } else {
                        return "sad";
                    }
                })
        ));
        System.out.println("多级分组：" + collect);

        //按条件分区,true放在一组，false放在另一组
        Map<Boolean, List<LamdUser>> map1 = userList.stream().collect(Collectors.partitioningBy(u -> u.getAge() >= 30));
        System.out.println("partitioningBy:" + map1);

        //字符串拼接
        String collect2 = userList.stream().map(s -> s.getAddress()).collect(Collectors.joining(","));
        System.out.println("joining:" + collect2);

    }


    /**
     * 排序
     */
    @Test
    public  void sorted(){
        System.out.println("**********************sorted**************************");
/*
        System.out.println("升序1：");
        Collections.sort(userList, new Comparator<TempUser>() {
            @Override
            public int compare(TempUser o1, TempUser o2) {
                int age1 = o1.getAge();
                int age2= o2.getAge();
                return age2 - age1;

            }
        });*/

        System.out.println("升序2：");
        userList.stream().sorted(Comparator.comparing(LamdUser::getId)).collect(Collectors.toList()).forEach(System.out::println);

        System.out.println("升序3：");
        userList.stream().map(user -> user.getAge()).sorted().forEach(System.out::println);

        //sorted的入参是Comparator接口，所以(u1,u2)->{return u1-u2;}成了Comparator接口的匿名实现
        System.out.println("升序4：");
        userList.stream().map(s -> s.getAge()).sorted((u1, u2) -> {
            return u1 - u2;
        }).forEach(System.out::println);

        /**
         * 倒序
         * reversed(),java8泛型推导的问题，所以如果comparing里面是非方法引用的lambda表达式就没办法直接使用reversed()
         * Comparator.reverseOrder():也是用于翻转顺序，用于比较对象（Stream里面的类型必须是可比较的）
         * Comparator. naturalOrder()：返回一个自然排序比较器，用于比较对象（Stream里面的类型必须是可比较的）
         */
        System.out.println("降序：");
        //.sorted(Comparator.comparing(TempUser::getAge).reversed()) 加上reversed()方法就是逆序排序
        userList.stream().sorted(Comparator.comparing(LamdUser::getId).reversed()).collect(Collectors.toList()).forEach(System.out::println);
        userList.stream().map(s->s.getId()).sorted(Comparator.comparing(String::length).reversed()).forEach(System.out::println);
        userList.stream().map(s->s.getAddress()).sorted(Comparator.naturalOrder()).forEach(System.out::println);

        /**
         * thenComparing
         * 先按照首字母排序
         * 之后按照Adress的String长度排序
         */
        userList.stream().map(s->s.getAddress()).sorted(Comparator.comparing(this::com1).thenComparing(String::length)).forEach(System.out::println);

    }
    public char com1(String x){
        return x.charAt(0);
    }


    /**
     * mapToInt 使用当前方法是内置方法结果返回流数据必须是int类型,返回为 IntStream 类型,可再次作其他操作。
     * 强转类型一定要注意内容是否存在非强转类型
     */
    @Test
    public void mapToInt(){
        IntStream intStream = userList.stream().mapToInt(c -> c.getAge());
        intStream.forEach(System.out::println);
    }

    /**
     * mapToLong 使用当前方法是内置方法结果返回流数据必须是Long 或 可向下强转 类型,返回为 LongStream 类型,可再次作其他操作。
     * 强转类型一定要注意内容是否存在非强转类型
     */
    @Test
    public void mapToLong(){
        LongStream longStream  = userList.stream().mapToLong(c -> c.getAge());
        longStream .forEach(System.out::println);
    }

    /**
     * mapToDouble 使用当前方法是内置方法结果返回流数据必须是Double 或 可向下强转 类型,返回为 DoubleStream 类型,可再次作其他操作。
     * 强转类型一定要注意内容是否存在非强转类型
     */
    public  void mapToDouble(){
        DoubleStream stream = userList.stream().mapToDouble(c -> c.getAge());
        stream.forEach(System.out::println);
    }

    /**
     * 中间操作 对数据进行加工或者展示,操作的流对象没有更换。
     */
    @Test
    public void peek(){
        userList.stream().peek(c -> {
            // 可以对内附对象最初修正操作或者其他业务操作
            System.out.println(c);

        }).collect(Collectors.toList());
    }

    @Test
    public void flatMap(){
        userList.stream().flatMap(u -> Stream.of(u.getAddress())).collect(Collectors.toList()).forEach(System.out::println);

        Stream.of(tempList,userList).flatMap(Collection::stream).distinct().collect(Collectors.toList()).forEach(System.out::println);
    }

    /**
     * 操作的流数据必须返回的是int类型的流内容
     * flatMapToLong
     * flatMapToDouble
     * 以上同理
     */
    @Test
    public  void flatMapToInt(){
        System.out.println("**********************flatMapToInt**************************");
        List<List<LamdUser>> flatList = Arrays.asList(userList);
//         userList.stream().flatMapToInt(c -> Stream.of(c.getId()).mapToInt(Integer::valueOf)).forEach(System.out::println);
        flatList.stream().flatMapToInt(c -> c.stream().map(LamdUser::getId).mapToInt(Integer::valueOf)).forEach(System.out::println);
    }



    /**
     * 限制流内容的数量
     * limit/skip 也不是终止操作,中间操作,操作完成的对象还是当前流本身，只是对数据内容做了处理。
     */
    public  void limit( ){
        System.out.println("**********************limit**************************");
        // 截取第一个
        userList.stream().limit(1).forEach(System.out::println);
        // 跳过第一个截取两个
        userList.stream().skip(1).limit(2).forEach(System.out::println);
    }

    @Test
    public void reduce(){
        System.out.println("**********************reduce**************************");
        // 求和
        userList.stream().mapToInt(c -> c.getAge()).reduce(Integer::sum).ifPresent(System.out::println);
        // 求最大值
        userList.stream().mapToInt(c -> c.getAge()).reduce(Integer::max).ifPresent(System.out::println);

        // 基于一个变量取最大值 ，与50比较 大于展示自身 小于展示50
        System.out.println(userList.stream().mapToInt(c -> c.getAge()).reduce(50, Integer::max));
        // 基于一个变量值累加后，再与之相加
        System.out.println(userList.stream().mapToInt(c -> c.getAge()).reduce(200, Integer::sum));

        //Collections.sort 倒序排列
        List<Integer> arrList = Arrays.asList(1,23,40,60,70,34,35);
        Collections.sort(arrList,Collections.reverseOrder());
        System.out.println(arrList.get(0));

    }

    /**
     * 最大、最小 、求和
     */
    @Test
    public void minAMax(){
        List<Integer> arrList = Arrays.asList(1,23,40,60,70,34,35);
//        arrList.stream().min(Integer::compareTo).ifPresent(System.out::println);
        userList.stream().map(user -> user.getAge()).max(Integer::compareTo).ifPresent(System.out::println);
        userList.stream().max((u1,u2) -> {
            return u1.getAge() - u2.getAge();
        });
    }

}
