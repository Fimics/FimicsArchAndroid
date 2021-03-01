package com.example.as.api.hiconfig;

import org.springframework.boot.system.ApplicationHome;

public class PropertyUtil {
    /**
     * 获取文件存储路径
     *
     * @param append
     * @return
     */
    public static String getDocPath(String append) {
        return new ApplicationHome(HiConfigFileUtil.class).getSource().getParentFile().getParentFile().getPath() + "/" + append;
    }

    /**
     * 获取CDN路径
     *
     * @param append
     * @return
     */
    public static String getCDNPrefix(String append) {
        return "http://127.0.0.1:8080/as/" + append;
    }
}
