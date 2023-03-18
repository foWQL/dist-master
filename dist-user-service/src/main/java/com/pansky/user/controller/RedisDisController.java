package com.pansky.user.controller;


import com.pansky.user.entity.TbUser;
import com.pansky.user.service.TbUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (TbUser)表控制层
 *
 * @author Fo
 * @since 2022-11-12 18:04:35
 */
@RestController
@RequestMapping("redisDis")
public class RedisDisController {

    @PostMapping("/delete")
    public String deleteKey(String key) {
        return this.tbUserService.deleteKey(key);
    }


    /**
     * 新增数据
     *
     * @param tbUser 实体
     * @return 新增结果
     */
    @PostMapping("/insert")
    public ResponseEntity<TbUser> insert(@RequestBody TbUser tbUser) {

        TbUser  user = this.tbUserService.insert(tbUser);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/insert1")
    public ResponseEntity<TbUser> insert1(@RequestBody TbUser tbUser) {

        TbUser  user = this.tbUserService.insert1(tbUser);

        return ResponseEntity.ok(user);
    }

    /**
     * 新增数据
     *
     * @param tbUser 实体
     * @return 新增结果
     */
    @PostMapping("/insert2")
    public ResponseEntity<TbUser> insert2(@RequestBody TbUser tbUser) {

        TbUser  user = this.tbUserService.insert2(tbUser);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/insert3")
    public ResponseEntity<TbUser> insert3(@RequestBody TbUser tbUser) {

        TbUser  user = this.tbUserService.insert3(tbUser);

        return ResponseEntity.ok(user);
    }
//=============================================================================//

    /**
     * 服务对象
     */
    @Resource
    private TbUserService tbUserService;



    /**
     * 分页查询
     *
     * @param tbUser      筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping("/queryByPage")
    public ResponseEntity<Page<TbUser>> queryByPage(@RequestBody TbUser tbUser, PageRequest pageRequest) {
        return ResponseEntity.ok(this.tbUserService.queryByPage(tbUser, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<TbUser> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.tbUserService.queryById(id));
    }


    /**
     * 编辑数据
     *
     * @param tbUser 实体
     * @return 编辑结果
     */
    @PutMapping("/edit")
    public ResponseEntity<TbUser> edit(@RequestBody TbUser tbUser) {
        return ResponseEntity.ok(this.tbUserService.update(tbUser));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping ("/deleteById")
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.tbUserService.deleteById(id));
    }

}

