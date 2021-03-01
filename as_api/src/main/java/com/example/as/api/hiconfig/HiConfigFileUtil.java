package com.example.as.api.hiconfig;

import com.example.as.api.util.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HiConfigFileUtil {
    private static final String APPEND = "file/as/";
    private static String targetDir = PropertyUtil.getDocPath(APPEND);
    private static final String CND_PREFIX = PropertyUtil.getCDNPrefix(APPEND);

    public static void saveContent(HiConfigModel configModel) {
        String fileName = genFileName(configModel.namespace, configModel.version);
        configModel.originalUrl = saveContent(fileName, configModel.content);
        configModel.jsonUrl = saveContent(fileName + ".json", JsonUtil.toJsonString(configModel.contentMap));
    }

    /**
     * 将配置数据保存到CDN
     *
     * @param filename
     * @param content
     * @return
     */
    public static String saveContent(String filename, String content) {
        FileOutputStream fos = null;
        File tempFile = null;
        String cdnUrl = null;
        try {
            File targetFile = new File(targetDir, filename);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            tempFile = File.createTempFile(filename, ".temp", targetFile.getParentFile());

            fos = new FileOutputStream(tempFile);
            fos.write(content.getBytes());
            fos.flush();

            tempFile.renameTo(targetFile);
            cdnUrl = CND_PREFIX + filename;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HiConfigUtil.close(fos);
            HiConfigUtil.delete(tempFile);
        }
        return cdnUrl;
    }

    private static String genFileName(String namespace, String version) {
        return namespace + "_" + version;
    }
}
