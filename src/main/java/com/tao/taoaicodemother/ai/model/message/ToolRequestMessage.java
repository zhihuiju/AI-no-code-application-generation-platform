package com.tao.taoaicodemother.ai.model.message;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工具调用消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolRequestMessage extends StreamMessage {

    private String id;  //工具ID

    private String name;  //工具名称

    private String arguments;  //工具调用参数

    public ToolRequestMessage(ToolExecutionRequest toolExecutionRequest) {
        //不支持从JSON对象中自动解析，所以需依次赋值
        super(StreamMessageTypeEnum.TOOL_REQUEST.getValue());
        this.id = toolExecutionRequest.id();
        this.name = toolExecutionRequest.name();
        this.arguments = toolExecutionRequest.arguments();
    }
}