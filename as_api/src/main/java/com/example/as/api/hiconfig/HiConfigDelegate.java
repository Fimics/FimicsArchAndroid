package com.example.as.api.hiconfig;

import com.example.as.api.util.JsonUtil;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 探针功能
 */
public class HiConfigDelegate {
    public static final String HI_CONFIG_KEY = "hi-config";
    public static final String HI_NAMES_PACE = "namespace";

    /**
     * 探针模式添加，判断是否需要携带配置元数据
     *
     * @param extra
     */
    public static void bindConfig(@NonNull Map<String, Object> extra) {
        Map configHeader = getConfigHeader();
        if (configHeader == null) return;
        String namespace = (String) configHeader.get(HI_NAMES_PACE);
        HiConfigModel configModel = CacheManager.getInstance().getCache(namespace, HiConfigModel.class);
        if (configModel == null) return;
        String version = (String) configHeader.get("version");
        if (configModel.namespace.equals(namespace)
                && HiConfigUtil.compareVersion(configModel.version, version)) {
            extra.put("hiConfig", configModel);
        }
    }

    /**
     * 从每次请求中获取元数据
     *
     * @return
     */
    private static Map getConfigHeader() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
            String hiConfig = request.getHeader(HI_CONFIG_KEY);
            if (StringUtils.isEmpty(hiConfig)) return null;
            Map<String, String> config = JsonUtil.fromJson(hiConfig, Map.class);
            String namespace = config.get(HI_NAMES_PACE);
            return namespace != null ? config : null;
        }
        return null;
    }
}
