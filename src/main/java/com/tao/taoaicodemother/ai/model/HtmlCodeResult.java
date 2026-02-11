package com.tao.taoaicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * HTML代码结果
 */
@Description("生成 HTML 代码文件的结果")
@Data
public class HtmlCodeResult {

    //html代码
    @Description("HTML代码")
    private String htmlCode;

    //描述
    @Description("HTML代码的描述")
    private String description;
}