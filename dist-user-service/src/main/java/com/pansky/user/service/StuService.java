package com.pansky.user.service;

import com.pansky.user.entity.Stu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (Stu)表服务接口
 *
 * @author Fo
 * @since 2022-12-11 15:50:16
 */
public interface StuService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Stu queryById(Integer id);

    /**
     * 分页查询
     *
     * @param stu         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Stu> queryByPage(Stu stu, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param stu 实例对象
     * @return 实例对象
     */
    Stu insert(Stu stu);

    /**
     * 修改数据
     *
     * @param stu 实例对象
     * @return 实例对象
     */
    Stu update(Stu stu);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);


    void insertBatch();
}
