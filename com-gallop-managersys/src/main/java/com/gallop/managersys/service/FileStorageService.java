package com.gallop.managersys.service;

import com.gallop.managersys.pojo.FileStorage;
import com.gallop.utils.PagedResult;

/**
 * author gallop
 * date 2020-04-18 18:01
 * Description:
 * Modified By:
 */
public interface FileStorageService {
    public void add(FileStorage fileStorage);

    public void deleteByKey(String key);

    public FileStorage findByKey(String key);

    public int update(FileStorage fileStorageInfo);

    public FileStorage findById(Integer id);

    public PagedResult querySelective(String key, String name, Integer page, Integer pageSize, String sort, String order);

    public int count();
}
