package com.tao.taoaicodemother.ai.model.message;

import dev.langchain4j.service.tool.ToolExecution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工具执行结果消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolExecutedMessage extends StreamMessage {

    private String id; //工具ID

    private String name;  //工具名称

    private String arguments;  //工具调用参数

    private String result;  //工具调用结果

    public ToolExecutedMessage(ToolExecution toolExecution) {
        //不支持从JSON对象中自动解析，所以需依次赋值
        super(StreamMessageTypeEnum.TOOL_EXECUTED.getValue());
        this.id = toolExecution.request().id();
        this.name = toolExecution.request().name();
        this.arguments = toolExecution.request().arguments();
        this.result = toolExecution.result();
    }
}
