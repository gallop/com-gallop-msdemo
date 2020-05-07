package com.gallop.managersys.storage;

import com.gallop.managersys.pojo.FileStorage;
import com.gallop.managersys.service.FileStorageService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 提供存储服务类，所有存储服务均由该类对外提供
 */
@Setter
@Getter
@Slf4j
public class StorageService {

    private String active;
    private Storage storage;
    @Autowired
    private FileStorageService fileStorageService;


    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public FileStorage store(InputStream inputStream, long contentLength, String contentType, String fileName) {
        // String key = generateKey(fileName);
        storage.store(inputStream, contentLength, contentType, fileName);
        String key = getKey();
        String url = getUrl();

        FileStorage storageInfo = new FileStorage();
        storageInfo.setName(fileName);
        storageInfo.setSize((int) contentLength);
        storageInfo.setType(contentType);
        storageInfo.setKey(key);
        storageInfo.setUrl(url);
        storageInfo.setDeleted(false);
        log.info("storageInfo:"+storageInfo.toString());
        fileStorageService.add(storageInfo);

        return storageInfo;
    }



    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public Path load(String keyName) {
        return storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String getKey(){ return  storage.generateKey();}

    private String getUrl() {
        return storage.generateUrl();
    }


}
