package com.tao.taoaicodemother.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.tao.taoaicodemother.model.entity.App;
import com.tao.taoaicodemother.mapper.AppMapper;
import com.tao.taoaicodemother.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author 韬
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
