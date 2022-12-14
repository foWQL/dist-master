package com.pansky.user.service.impl;

import com.pansky.user.entity.Stu;
import com.pansky.user.dao.StuDao;
import com.pansky.user.service.StuService;
import com.pansky.user.utils.RandomUtil;
import com.pansky.user.utils.ReadTxtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * (Stu)表服务实现类
 *
 * @author Fo
 * @since 2022-12-11 15:50:16
 */
@Service("stuService")
@Slf4j
public class StuServiceImpl implements StuService {
    @Resource
    private StuDao stuDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Stu queryById(Integer id) {
        return this.stuDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param stu         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Stu> queryByPage(Stu stu, PageRequest pageRequest) {
        long total = this.stuDao.count(stu);
        return new PageImpl<>(this.stuDao.queryAllByLimit(stu, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param stu 实例对象
     * @return 实例对象
     */
    @Override
    public Stu insert(Stu stu) {
        this.stuDao.insert(stu);
        return stu;
    }

    /**
     * 修改数据
     *
     * @param stu 实例对象
     * @return 实例对象
     */
    @Override
    public Stu update(Stu stu) {
        this.stuDao.update(stu);
        return this.queryById(stu.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.stuDao.deleteById(id) > 0;
    }

    @Override
    public void insertBatch(){
        // name, nickname, school, age, classNum, score, phone, email, ip, address
        try {
            for (int i = 0; i < 1000; i++) {
                long startTime = System.currentTimeMillis();
                List<Stu> entities = new ArrayList<Stu>();
                for (int j = 0; j < 5000; j++) {
                    String name = RandomUtil.genChineseName();
                    String nickname ="";
                    String school = ReadTxtUtil.getLine("D:\\temp\\createdata\\schoolNames.txt");
                    int age = RandomUtil.genInteger(10, 100);
                    int classNum = RandomUtil.genInteger(1, 15);
                    double score = RandomUtil.genInteger(10,150);
                    String phone = RandomUtil.genPhoneNum();
                    String email = RandomUtil.genEmail();
                    String ip = RandomUtil.genRandomIp();
                    String adress = ReadTxtUtil.getLine("D:\\temp\\createdata\\citys.txt");
                    Stu stu = new Stu(name,nickname,school,age,classNum,score,phone,email,ip,adress);
                    entities.add(stu);
                }
                stuDao.insertBatch(entities);
                long endTime = System.currentTimeMillis();
                long time = (endTime - startTime)/1000;
                log.info("stu数据插入第{}批成功，耗时{}", i, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    };

}
