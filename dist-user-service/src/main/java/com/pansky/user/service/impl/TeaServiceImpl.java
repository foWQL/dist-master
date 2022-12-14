package com.pansky.user.service.impl;

import com.pansky.user.entity.Stu;
import com.pansky.user.entity.Tea;
import com.pansky.user.dao.TeaDao;
import com.pansky.user.service.TeaService;
import com.pansky.user.utils.RandomUtil;
import com.pansky.user.utils.ReadTxtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Tea)表服务实现类
 *
 * @author Fo
 * @since 2022-12-12 10:29:27
 */
@Slf4j
@Service("teaService")
public class TeaServiceImpl implements TeaService {
    @Resource
    private TeaDao teaDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Tea queryById(Integer id) {
        return this.teaDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tea         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<Tea> queryByPage(Tea tea, PageRequest pageRequest) {
        long total = this.teaDao.count(tea);
        return new PageImpl<>(this.teaDao.queryAllByLimit(tea, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tea 实例对象
     * @return 实例对象
     */
    @Override
    public Tea insert(Tea tea) {
        this.teaDao.insert(tea);
        return tea;
    }

    @Override
    public void insertBatch(){
        // id, name, school, age, classNum, sourceId, phone
        try {
            for (int i = 0; i < 10; i++) {
                long startTime = System.currentTimeMillis();
                List<Tea> entities = new ArrayList<Tea>();
                for (int j = 0; j < 5000; j++) {
                    String name = ReadTxtUtil.getLine("D:\\temp\\createdata\\teacherNames.txt");
                    String school = ReadTxtUtil.getLine("D:\\temp\\createdata\\schoolNames.txt");
                    int age = RandomUtil.genInteger(10, 100);
                    int sourceId = RandomUtil.genInteger(1, 10);
                    int classNum = RandomUtil.genInteger(1, 15);
                    String phone = RandomUtil.genPhoneNum();
                    Tea tea = new Tea(name,school,age,classNum,sourceId,phone);
                    entities.add(tea);
                }
                teaDao.insertBatch(entities);
                long endTime = System.currentTimeMillis();
                long time = (endTime - startTime)/1000;
                log.info("tea数据插入第{}批成功，耗时{}秒", i, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据
     *
     * @param tea 实例对象
     * @return 实例对象
     */
    @Override
    public Tea update(Tea tea) {
        this.teaDao.update(tea);
        return this.queryById(tea.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.teaDao.deleteById(id) > 0;
    }
}
