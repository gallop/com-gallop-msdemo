package com.gallop.managersys.config;

import com.gallop.managersys.storage.FastdfsStorage;
import com.gallop.managersys.storage.LocalStorage;
import com.gallop.managersys.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author gallop
 * date 2020-04-18 15:55
 * Description:
 * Modified By:
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {
    private final StorageProperties properties;

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Bean(autowire = Autowire.BY_NAME,value = "storageService")
    public StorageService storageService() {
        StorageService storageService = new StorageService();
        String active = this.properties.getActive();
        storageService.setActive(active);
        if (active.equals("local")) {
            storageService.setStorage(localStorage());
        } else if(active.equals("fastdfs")){
            storageService.setStorage(fastdfsStorage());
        }else {
            throw new RuntimeException("当前存储模式 " + active + " 不支持");
        }

        return storageService;
    }

    @Bean()
    public LocalStorage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        StorageProperties.Local local = this.properties.getLocal();

        localStorage.setAddress(local.getAddress());
        localStorage.setStoragePath(local.getStoragePath());

        System.out.println("=======localStorage===============getStoragePath:"+localStorage.getStoragePath());
        return localStorage;
    }

    @Bean
    public FastdfsStorage fastdfsStorage(){
        FastdfsStorage fastdfsStorage = new FastdfsStorage();
        StorageProperties.Fastdfs fastdfs = this.properties.getFastdfs();

        fastdfsStorage.setBaseUrl(fastdfs.getBaseUrl());

        return fastdfsStorage;
    }
}
