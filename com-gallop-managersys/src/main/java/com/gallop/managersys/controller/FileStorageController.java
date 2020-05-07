package com.gallop.managersys.controller;

import com.gallop.managersys.annotation.RequiresPermissionsDesc;
import com.gallop.managersys.pojo.FileStorage;
import com.gallop.managersys.service.FileStorageService;
import com.gallop.managersys.storage.StorageService;
import com.gallop.utils.JSONResult;
import com.gallop.utils.PagedResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 15:13 2019/6/2
 * Modified By:
 */
@RestController
@RequestMapping("/admin/storage")
@Slf4j
public class FileStorageController {
    @Autowired
    @Qualifier("storageService")
    private StorageService storageService;
    @Autowired
    private FileStorageService fileStorageService;

    @RequiresPermissions("admin:storage:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "上传")
    @PostMapping("/create")
    public Object create(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        log.info("=========upload file name:{}",file.getName());
        FileStorage fileStorage = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
        return JSONResult.ok(fileStorage);
    }
    /**
     * 访问存储对象
     *
     * @param key 存储对象key
     * @return
     */

    // @RequiresAuthentication
    @GetMapping("/fetch/{key:.+}")
    public ResponseEntity<Resource> fetch(@PathVariable String key) {
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        if (key.contains("../")) {
            return ResponseEntity.badRequest().build();
        }
        log.info("===============key:{}",key);
        FileStorage fileStorage = fileStorageService.findByKey(key);
        String type = fileStorage.getType();
        MediaType mediaType = MediaType.parseMediaType(type);

        Resource file = storageService.loadAsResource(key);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(mediaType).body(file);
    }

    @RequiresPermissions("admin:storage:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "查询")
    @GetMapping("/list")
    public Object list(String key, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order) {
        PagedResult pagedResult = fileStorageService.querySelective(key, name, page, pageSize, sort, order);
        return JSONResult.ok(pagedResult);
    }

    /*@RequiresPermissions("admin:storage:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "详情")
    @PostMapping("/read")
    public Object read(@NotNull Integer id) {
        FileStorage storageInfo = fileStorageService.findById(id);
        if (storageInfo == null) {
            return JSONResult.badArgumentValue();
        }
        return JSONResult.ok(storageInfo);
    }*/

    @RequiresPermissions("admin:storage:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody FileStorage fileStorage) {
        if (fileStorageService.update(fileStorage) == 0) {
            return JSONResult.updatedDataFailed();
        }
        return JSONResult.ok(fileStorage);
    }

    @RequiresPermissions("admin:storage:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody FileStorage fileStorage) {
        String key = fileStorage.getKey();
        if (StringUtils.isEmpty(key)) {
            return JSONResult.badArgument();
        }
        log.info("delete-file:{}",fileStorage.toString());
        fileStorageService.deleteByKey(key);
        storageService.delete(key);
        return JSONResult.ok();
    }
}
