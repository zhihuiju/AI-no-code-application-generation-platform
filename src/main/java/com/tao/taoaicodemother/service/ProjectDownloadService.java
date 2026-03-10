package com.tao.taoaicodemother.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ProjectDownloadService {

    /**
     * 下载项目为压缩包
     * @param projectPath  项目路径
     * @param downloadFileName  下载名称
     * @param response  用于构造响应头
     * @return
     */
    void downloadProjectAsZip(String projectPath , String downloadFileName , HttpServletResponse response);
}
