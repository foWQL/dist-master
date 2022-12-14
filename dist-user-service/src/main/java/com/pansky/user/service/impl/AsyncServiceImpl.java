package com.pansky.user.service.impl;

import com.pansky.user.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Fo
 * @date 2022/11/12 21:29
 */
@Service
public class AsyncServiceImpl implements AsyncService {
    @Autowired
    private ThreadPoolTaskExecutor threadPool;

    @Override
    @Async()
    public void function1() throws InterruptedException {
        System.out.println("f1 : " + Thread.currentThread().getName() + "   " + UUID.randomUUID().toString());
//        try {
//            Thread.sleep(10000);
//            System.out.println("EEEE");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //故意等10秒，那么异步线程开起来，这样明显看到 2方法不用等1方法执行完再调用了
        Thread.sleep(10000);
        System.out.println("aaaa");
    }

    @Override
    @Async
    public void function2() {
        System.out.println("f2 : " + Thread.currentThread().getName() + "   " + UUID.randomUUID().toString());
        try {
            Thread.sleep(100);
            System.out.println("bbbb");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
