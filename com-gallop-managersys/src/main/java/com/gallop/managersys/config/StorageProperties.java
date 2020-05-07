package com.gallop.managersys.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 18:43 2020/4/17
 * Modified By:
 */
@Data
@ConfigurationProperties(prefix = "msdemo.storage")
public class StorageProperties {
    private String active;
    private Local local;
    private Fastdfs fastdfs;


    @Data
    public static class Local {
        private String address;
        private String storagePath;
    }

    @Data
    public static class Fastdfs {
        private String baseUrl;
    }
}
