package com.pansky.user.service;

import com.pansky.user.entity.Tea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (Tea)表服务接口
 *
 * @author Fo
 * @since 2022-12-12 10:29:27
 */
public interface TeaService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Tea queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tea         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<Tea> queryByPage(Tea tea, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tea 实例对象
     * @return 实例对象
     */
    Tea insert(Tea tea);

    void insertBatch();

    /**
     * 修改数据
     *
     * @param tea 实例对象
     * @return 实例对象
     */
    Tea update(Tea tea);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
