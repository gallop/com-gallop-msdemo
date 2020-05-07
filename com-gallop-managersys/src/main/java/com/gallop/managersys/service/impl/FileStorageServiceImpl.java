package com.gallop.managersys.service.impl;

import com.gallop.managersys.mapper.FileStorageMapper;
import com.gallop.managersys.pojo.FileStorage;
import com.gallop.managersys.service.FileStorageService;
import com.gallop.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author gallop
 * date 2020-04-18 18:06
 * Description:
 * Modified By:
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    private FileStorageMapper fileStorageMapper;

    @Override
    public void add(FileStorage fileStorage) {
        fileStorage.setAddTime(LocalDateTime.now());
        fileStorage.setUpdateTime(LocalDateTime.now());

        fileStorageMapper.insertUseGeneratedKeys(fileStorage);
    }

    @Override
    public void deleteByKey(String key) {
        Example fileStorageExample = new Example(FileStorage.class);
        fileStorageExample.or().andEqualTo("key",key);

        fileStorageMapper.deleteByExample(fileStorageExample);
    }

    @Override
    public FileStorage findByKey(String key) {
        Example fileStorageExample = new Example(FileStorage.class);
        fileStorageExample.or().andEqualTo("key",key).andEqualTo("deleted",false);

        return fileStorageMapper.selectOneByExample(fileStorageExample);
    }

    @Override
    public int update(FileStorage fileStorageInfo) {
        fileStorageInfo.setUpdateTime(LocalDateTime.now());

        return fileStorageMapper.updateByPrimaryKeySelective(fileStorageInfo);
    }

    @Override
    public FileStorage findById(Integer id) {
        return fileStorageMapper.selectByPrimaryKey(id);
    }

    @Override
    public PagedResult querySelective(String key, String name, Integer page, Integer pageSize, String sort, String order) {
        Example fileStorageExample = new Example(FileStorage.class);
        Example.Criteria criteria = fileStorageExample.createCriteria();


        if (!StringUtils.isEmpty(key)) {
            criteria.andEqualTo("key",key);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name",name);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            fileStorageExample.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, pageSize);

        List<FileStorage> list = fileStorageMapper.selectByExample(fileStorageExample);

        PageInfo<FileStorage> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Override
    public int count() {
        Example fileStorageExample = new Example(FileStorage.class);
        return fileStorageMapper.selectCountByExample(fileStorageExample);
    }
}
