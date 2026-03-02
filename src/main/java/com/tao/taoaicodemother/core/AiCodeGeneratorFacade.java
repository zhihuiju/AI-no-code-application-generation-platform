package com.tao.taoaicodemother.core;

import com.tao.taoaicodemother.ai.AiCodeGeneratorService;
import com.tao.taoaicodemother.ai.AiCodeGeneratorServiceFactory;
import com.tao.taoaicodemother.ai.model.HtmlCodeResult;
import com.tao.taoaicodemother.ai.model.MultiFileCodeResult;
import com.tao.taoaicodemother.core.parser.CodeParserExecutor;
import com.tao.taoaicodemother.core.saver.CodeFileSaverExecutor;
import com.tao.taoaicodemother.exception.BusinessException;
import com.tao.taoaicodemother.exception.ErrorCode;
import com.tao.taoaicodemother.model.enums.CodeGenTypeEnum;
import com.tao.taoaicodemother.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    /**
     * 生成并保存代码门面 （统一入口）
     * @param userMessage 用户输入
     * @param codeGenTypeEnum 生成类型
     * @param appId 应用ID
     * @return
     */
    public File generateAndSaveCode(String userMessage , CodeGenTypeEnum codeGenTypeEnum , Long appId){
        if(codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"生成类型不能为空");
        }
        //根据appid获取相应的Ai服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum){
//            case HTML -> generateAndSaveHtmlCode(userMessage);
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML , appId);
            }
//            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE , appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.PARAMS_ERROR,errorMessage);
            }
        };
    }

    /**
     * 生成并保存代码门面（流式）（统一入口）
     * @param userMessage 用户输入
     * @param codeGenTypeEnum 生成类型
     * @param appId 应用Id
     * @return 代码内容流
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage , CodeGenTypeEnum codeGenTypeEnum , Long appId){
        if(codeGenTypeEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"生成类型不能为空");
        }
        //根据appid获取相应的Ai服务实例
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum){
//            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
//            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream,codeGenTypeEnum , appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream,codeGenTypeEnum , appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.PARAMS_ERROR,errorMessage);
            }
        };
    }

    /**
     * 生成并保存HTML代码
     * @param userMessage
     * @return
     */
//    private File generateAndSaveHtmlCode(String userMessage) {
//        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
//        return CodeFileSaver.saveHtmlCodeResult(result);
//    }

    /**
     * 生成并保存多文件代码
     * @param userMessage
     * @return
     */
//    private File generateAndSaveMultiFileCode(String userMessage) {
//        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
//        return CodeFileSaver.saveMultiFileCodeResult(result);
//    }

    /**
     * 处理代码流 （流式）
     * @param codeStream
     * @param codeGenType
     * @param appId 应用Id
     * @return
     */
    private Flux<String> processCodeStream(Flux<String> codeStream,CodeGenTypeEnum codeGenType , Long appId)  {
        // 字符串拼接器 用于当流式返回所有的代码后 再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            //实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            try {
                //流式返回完成后，保存代码
                String completeCode = codeBuilder.toString();
                // 使用执行器解析代码
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                // 使用执行器保存代码
                File saverDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType , appId);
                log.info("代码生成完成，目录为：{}",saverDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("生成并保存多文件代码流失败",e.getMessage());
            }
        });

    }

    /**
     * 生成并保存多文件代码流（流式）
     * @param userMessage
     * @return 代码内容流
     */
//    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
//        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
//        StringBuilder codeBuilder = new StringBuilder();
//        return result.doOnNext(chunk -> {
//            //实时收集代码片段
//            codeBuilder.append(chunk);
//        }).doOnComplete(() -> {
//            try {
//                //流式返回完成后，保存代码
//                String completeCode = codeBuilder.toString();
//                //解析代码为对象
//                MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(completeCode);
//                //保存代码到文件
//                File saverDir = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
//                log.info("代码生成完成，目录为：{}",saverDir.getAbsolutePath());
//            } catch (Exception e) {
//                log.error("生成并保存多文件代码流失败",e.getMessage());
//            }
//        });
//
//    }

    /**
     * 生成并保存HTML代码流（流式）
     * @param userMessage
     * @return 代码内容流
     */
//    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
//        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
//        StringBuilder codeBuilder = new StringBuilder();
//        return result.doOnNext(chunk -> {
//            //实时收集代码片段
//            codeBuilder.append(chunk);
//        }).doOnComplete(() -> {
//            try {
//                //流式返回完成后，保存代码
//                String completeCode = codeBuilder.toString();
//                //解析代码为对象
//                HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeCode);
//                //保存代码到文件
//                File saverDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
//                log.info("代码生成完成，目录为：{}",saverDir.getAbsolutePath());
//            } catch (Exception e) {
//                log.error("生成并保存HTML代码流失败",e.getMessage());
//            }
//        });
//    }

}
