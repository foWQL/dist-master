package com.pansky.user.service.impl;


import com.pansky.common.utils.JedisUtil;
import com.pansky.user.dao.TbUserDao;
import com.pansky.user.entity.TbUser;
import com.pansky.user.service.TbUserService;
import com.pansky.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.params.SetParams;


import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (TbUser)表服务实现类
 *
 * @author Fo
 * @since 2022-11-12 18:04:36
 */
@Slf4j
@Service("tbUserService")
public class TbUserServiceImpl implements TbUserService {
    @Resource
    private TbUserDao tbUserDao;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 反例：通过查询表中数据，控制不住
     */
    @Override
    @Transactional
    public TbUser insert(TbUser tbUser) {
        int n = 5;
//        CountDownLatch latch = new CountDownLatch(n);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));

        for (int i = 0; i < n ; i++) {
            taskExecutor.execute(() ->{

                TbUser user = null;
                Boolean flag1 = redisTemplate.hasKey(tbUser.getAddress());
                List list = this.tbUserDao.queryByAddr(tbUser.getAddress());
                if (list.size() >0) {
                    //String addr = (String) redisTemplate.opsForValue().get(tbUser.getAddress());
                    System.out.println("---------------已经存在了");
                }else {
                    System.out.println("不存在，可以插入");
                    this.tbUserDao.insert(tbUser);
                    redisTemplate.opsForValue().set(tbUser.getAddress(),sdf.format(date));
                }
                //countDown的计数，到n后 await 方法不再阻塞
//                latch.countDown();

            });
        }
        return tbUser;

    }

    /**
     * 例 1 ：通过 LUNA 脚本实现
     */
    @Override
    @Transactional
    public TbUser insert1(TbUser tbUser) {
        int n = 5;
//        CountDownLatch latch = new CountDownLatch(n);

        for (int i = 0; i < n ; i++) {
            taskExecutor.execute(() ->{

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//                boolean lock = redisUtil.lock(tbUser.getAddress(), sdf.format(date), 120);

                // todo 实现不过期
                boolean lock = redisUtil.lockForPersist(tbUser.getAddress(), sdf.format(date));

                if (!lock) {
                    log.info("当前锁---key:{}，value:{}-已被占用");
                    //提示给操作者信息
                }else {
                    System.out.println("不存在，可以插入");
                    this.tbUserDao.insert(tbUser);
                }
            });
        }
        return tbUser;

    }

    /**
     * 例 2 ：通过 redission 实现
     */
    @Override
    @Transactional
    public TbUser insert2(TbUser tbUser) {
        int n = 5;
        for (int i = 0; i < n ; i++) {
            taskExecutor.execute(() ->{

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                RLock lock = redissonClient.getLock(tbUser.getAddress());
                try {
                    boolean flag = lock.tryLock(10,-1, TimeUnit.SECONDS);
                    if (!flag) {
                        log.info("当前锁---key:{}，value:{}-已被占用");
                    }else {
                        System.out.println("不存在，可以插入");
                        this.tbUserDao.insert(tbUser);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return tbUser;

    }

    /**
     * 例 3 ：通过 jedis 实现
     */
    @Override
    @Transactional
    public TbUser insert3(TbUser tbUser) {
        int n = 5;
        for (int i = 0; i < n ; i++) {
            taskExecutor.execute(() ->{
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                JedisUtil jedisUtil = new JedisUtil();
                jedisUtil.execute(jedis -> {
                    try {
                        String set = jedis.set(tbUser.getAddress(), sdf.format(date),new SetParams().nx());
                        if ("OK".equals(set)) {
                            System.out.println("不存在，可以插入");
                            this.tbUserDao.insert(tbUser);
                        }else {
                            log.info("当前锁---key:{}，value:{}-已被占用");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
        return tbUser;

    }

// ===================================================================//
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TbUser queryById(Integer id) {
        return this.tbUserDao.queryById(id);
    }

    @Override
    public List<TbUser> queryByAddr(String addr) {
        return this.tbUserDao.queryByAddr(addr);
    }


    /**
     * 分页查询
     *
     * @param tbUser      筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public PageImpl<TbUser> queryByPage(TbUser tbUser, PageRequest pageRequest) {
        long total = this.tbUserDao.count(tbUser);
        return new PageImpl<TbUser>(this.tbUserDao.queryAllByLimit(tbUser, pageRequest), pageRequest, total);
    }

    @Override
    public int insertBatch(List<TbUser> list) {
        return this.tbUserDao.insertBatch(list);
    }

    /**
     * 修改数据
     *
     * @param tbUser 实例对象
     * @return 实例对象
     */
    @Override
    public TbUser update(TbUser tbUser) {
        this.tbUserDao.update(tbUser);
        return this.queryById(tbUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tbUserDao.deleteById(id) > 0;
    }
}
