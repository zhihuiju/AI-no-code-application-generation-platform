package com.tao.taoaicodemother.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tao.taoaicodemother.constant.AppConstant;
import com.tao.taoaicodemother.exception.BusinessException;
import com.tao.taoaicodemother.exception.ErrorCode;
import com.tao.taoaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 模板模式：代码文件保存器模板
 * @param <T>
 */
public abstract class CodeFileSaverTemplate<T> {

    /**
     * 文件保存的根目录
     */
    private static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;

    /**
     * 模板方法：保存代码的标准流程
     * @param result 代码结果对象
     * @param appId 应用ID
     * @return 保存的目录
     */
    public final File saveCode(T result , Long appId){

        //1.验证输入
        validateInput(result);

        //2.构建唯一目录
        String baseDirPath = buildUniqueDir(appId);

        //3.保存文件（具体实现交给子类）
        saveFiles(result,baseDirPath);
        
        //4.返回文件目录对象
        return new File(baseDirPath);
    }

    /**
     * 写入文件内容到指定目录 (通用步骤供子类使用)
     * @param dirPath 目录路径
     * @param filename 文件名
     * @param content 文件内容
     */
    public final void writeToFile(String dirPath,String filename,String content){
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
    }

    /**
     * 验证输入参数是否合法
     * @param result 代码结果对象
     */
    protected void validateInput(T result) {
        if(result == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"代码结果对象不能为空");
        }
    }

    /**
     * 构建文件的唯一路径：tmp/code_output/bizType_appId(bizType_雪花ID)
     * @param appId 应用ID
     * @return
     */
    protected String buildUniqueDir(Long appId){
        if(appId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"应用ID不能为空");
        }
        String codeType = getCodeType().getValue();
//        String uniqueDirName = StrUtil.format("{}_{}",codeType, IdUtil.getSnowflakeNextIdStr());
        String uniqueDirName = StrUtil.format("{}_{}",codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存代码文件（具体实现交给子类）
     * @param result 代码结果对象
     * @param baseDirPath 基础目录路径
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * 获取代码生成类型（具体实现交给子类）
     * @return
     */
    protected abstract CodeGenTypeEnum getCodeType();
}
