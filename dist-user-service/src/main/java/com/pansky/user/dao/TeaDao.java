package com.pansky.user.dao;

import com.pansky.user.entity.Tea;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (Tea)表数据库访问层
 *
 * @author Fo
 * @since 2022-12-12 10:29:27
 */
public interface TeaDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Tea queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tea      查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Tea> queryAllByLimit(Tea tea, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tea 查询条件
     * @return 总行数
     */
    long count(Tea tea);

    /**
     * 新增数据
     *
     * @param tea 实例对象
     * @return 影响行数
     */
    int insert(Tea tea);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Tea> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Tea> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Tea> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Tea> entities);

    /**
     * 修改数据
     *
     * @param tea 实例对象
     * @return 影响行数
     */
    int update(Tea tea);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

