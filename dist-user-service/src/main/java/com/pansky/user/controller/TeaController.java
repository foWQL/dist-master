package com.pansky.user.controller;

import com.pansky.user.entity.Tea;
import com.pansky.user.service.TeaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Tea)表控制层
 *
 * @author Fo
 * @since 2022-12-12 10:29:26
 */
@RestController
@RequestMapping("tea")
public class TeaController {
    /**
     * 服务对象
     */
    @Resource
    private TeaService teaService;

    /**
     * 分页查询
     *
     * @param tea         筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Tea>> queryByPage(Tea tea, PageRequest pageRequest) {
        return ResponseEntity.ok(this.teaService.queryByPage(tea, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Tea> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.teaService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param tea 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Tea> add(Tea tea) {
        return ResponseEntity.ok(this.teaService.insert(tea));
    }

    /**
     * 编辑数据
     *
     * @param tea 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Tea> edit(Tea tea) {
        return ResponseEntity.ok(this.teaService.update(tea));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.teaService.deleteById(id));
    }

    /**
     * 批量插入
     *
     */
    @PostMapping("/insertBatch")
    public void insertBatch() {
        this.teaService.insertBatch();
    }
}

