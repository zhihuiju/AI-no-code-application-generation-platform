package com.tao.taoaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.tao.taoaicodemother.model.dto.app.AppQueryRequest;
import com.tao.taoaicodemother.model.entity.App;
import com.tao.taoaicodemother.model.entity.User;
import com.tao.taoaicodemother.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author 韬
 */
public interface AppService extends IService<App> {

    /**
     * 通过对话生成应用代码
     * @param appId 应用ID
     * @param message 提示词
     * @param longinUser  登录用户
     * @return
     */
    Flux<String> chatTOGenCode(Long appId , String message , User longinUser);

    /**
     * 应用部署
     * @param appId 应用ID
     * @param longinUser 登录用户
     * @return 可访问的部署地址
     */
    String deployApp(Long appId , User longinUser);

    /**
     * 异步生成应用截图并更新封面
     * @param appId  应用ID
     * @param appUrl  应用访问的URL
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 查询应用关联信息封装类
     * @param app
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用封装列表 优化逻辑，避免重复查询
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构造应用分页查询条件
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

}
