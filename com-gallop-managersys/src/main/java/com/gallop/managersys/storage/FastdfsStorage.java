package com.gallop.managersys.storage;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * author gallop
 * date 2020-04-27 17:08
 * Description:
 * Modified By:
 */
@Slf4j
public class FastdfsStorage implements Storage {
    private String baseUrl;
    private StorePath storePath;
    private Set<MetaData> metaDataSet = null;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private Environment env;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        this.metaDataSet = new HashSet<MetaData>();
        this.metaDataSet.add(new MetaData("Content-Length", String.valueOf(contentLength)));
        this.metaDataSet.add(new MetaData("Content-Type", contentType));

        String fileExtName = "png";
        String arr[] = contentType.split("\\/");
        if (arr.length >= 2) {
            fileExtName = arr[1];
        }

        this.storePath = storageClient.uploadImageAndCrtThumbImage(inputStream,
                contentLength, fileExtName, this.metaDataSet);

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            log.info("loadAsResource-url:{}", getBaseUrl() + keyName);
            URL url = new URL(getBaseUrl() + keyName);
            Resource resource = new UrlResource(url);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(String keyName) {
        if (StringUtils.isEmpty(keyName)) {
            return;
        }
        // log.info("xiao tu:{}", env.getProperty("fdfs.thumb-image.width"));
        String thumbImgWidth = env.getProperty("fdfs.thumb-image.width");
        //StorePath storePath = StorePath.parseFromUrl(this.baseUrl + keyName);
        String filePath = this.baseUrl + keyName;
        //log.info("delete file path:{}", filePath);
        storageClient.deleteFile(filePath);
        String arr[] = keyName.split("\\.");
        if (arr.length >= 2) {
            keyName = arr[0] + "_" + thumbImgWidth + "x" + thumbImgWidth + "." + arr[1];
            // log.info("80x80 file name:{}",this.baseUrl + keyName);
            storageClient.deleteFile(this.baseUrl + keyName);
        }
    }

    @Override
    public String generateUrl() {
        log.info("full path: {}", this.storePath.getFullPath());
        return this.baseUrl + this.storePath.getFullPath();
    }

    @Override
    public String generateKey() {
        String key = this.storePath.getFullPath();
        //int index = path.lastIndexOf('/');
        //String key = path.substring(index+1);
        return key;
    }
}
