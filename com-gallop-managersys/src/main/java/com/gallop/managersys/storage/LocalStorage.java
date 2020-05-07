package com.gallop.managersys.storage;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 服务器本地对象存储服务
 */
public class LocalStorage implements Storage {
    private String storagePath;
    private String address;

    private String key;

    private Path rootLocation;

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;

        this.rootLocation = Paths.get(storagePath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            this.key = generateKey(keyName);
            Path filePathTmp = rootLocation.resolve(this.key);
            Files.copy(inputStream, filePathTmp, StandardCopyOption.REPLACE_EXISTING);
            String osName = System.getProperties().getProperty("os.name");
            //System.out.println("==========the running system is:"+ osName);
            if(osName.startsWith("Linux")){
                //linux 下给新增的文件添加其他用户可读的权限
                PosixFileAttributes attrs = Files.readAttributes(filePathTmp, PosixFileAttributes.class);// 读取文件的权限
                Set perms = attrs.permissions();
                //Set perms = new HashSet();
                perms.add(PosixFilePermission.OTHERS_READ);
                //perms.add(PosixFilePermission.OWNER_WRITE);

                Files.setPosixFilePermissions(filePathTmp, perms);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + keyName, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(path -> rootLocation.relativize(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String filename) {
        Path file = load(filename);
        try {
            Files.delete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateUrl() {
        String url = address + this.key;
        return url;
    }

    @Override
    public String generateKey() {
        return this.key;
    }

    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);
        String key = null;
        //key = Sid.next() + suffix;
        key = UUID.randomUUID().toString() + suffix;

        return key;
    }
}