package com.pansky.user.controller;

import com.pansky.user.entity.TbUser;
import com.pansky.user.service.AsyncService;
import com.pansky.user.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Fo
 * @date 2022/11/12 21:16
 */
@RestController
public class ThreadController {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private AsyncService asyncService;

   @Autowired
   private TbUserService tbUserService;

    @RequestMapping("/test")
    public void test() throws InterruptedException {
        for (int i = 0; i <5 ; i++) {
            asyncService.function1();
            asyncService.function2();
        }

    }

    @RequestMapping("/addUserBatch")
    public void addUserBatch(Integer len){
        long start = System.currentTimeMillis();
        if (len > 50000) {
            int l = len / 50000;
            for (int i = 0; i < l; i++) {
                List<TbUser> list = new ArrayList<>();
                for (int j = 0; j < 50000; j++) {
                    TbUser dataUser = new TbUser();
                    dataUser.setUsername(((i + 1) * (j + 1)) + "");
                    list.add(dataUser);
                }
                tbUserService.insertBatch(list);
            }
        }else {
            List<TbUser> list = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                TbUser dataUser = new TbUser();
                dataUser.setUsername((i + 1) + "");
                list.add(dataUser);
            }
            tbUserService.insertBatch(list);
        }
        System.out.println((System.currentTimeMillis() - start));
    }

    @RequestMapping("/test1")
    public String test1(){
        System.out.println("??????????????????----->"+ LocalDateTime.now());
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10000);
                System.out.println("task1 ??????name-->" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, taskExecutor);
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("task2 ??????name-->" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, taskExecutor);
        try {
            CompletableFuture.allOf(task1, task2).get();
            System.out.println("??????????????????----->"+ LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }


    /**
     * ????????????
     * @param input ????????????
     * @return ????????????
     */
   /* @Override
    public Output singleProcess(Input input) {
        log.info("Processing...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Output(false, null);
        }
        return new Output(true, String.valueOf(2 * input.getI() + 1));
    }

    *//**
     * ????????????
     * @param inputList ??????????????????
     * @return ??????????????????
     *//*
    @Override
    public List<Output> multiProcess(List<Input> inputList) {
        ThreadPoolTaskExecutor executor
                = SpringUtils.getBean("threadPoolTaskExecutor", ThreadPoolTaskExecutor.class);
        CountDownLatch countDown = new CountDownLatch(inputList.size());
        List<Output> outputList = Collections.synchronizedList(new ArrayList<>(inputList.size()));

        for (Input input : inputList) {
            executor.execute(() -> {
                try {
                    Output output = singleProcess(input);
                    outputList.add(output);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

         // ??????????????????????????????
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return outputList;
    }

    *//**
     * ????????????
     * @param input ????????????
     * @return ??????Future??????
     *//*
    @Async("threadPoolTaskExecutor")
    @Override
    public Future<Output> asyncProcess(Input input) {
        return new AsyncResult<>(singleProcess(input));
    }*/

}
