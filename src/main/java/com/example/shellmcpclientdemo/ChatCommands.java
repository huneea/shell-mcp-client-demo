package com.example.shellmcpclientdemo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ChatCommands {
    private final ChatClient chatClient;


    public ChatCommands(ChatClient.Builder builder, ToolCallbackProvider tools) {
        this.chatClient = builder
            .defaultTools(tools)
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultSystem("""
                               너는 스마트 에이전트야.
                               국내 주소를 찾을때는 `korea-address-finder` 툴을 사용해.
                               그리고 답변은 한국어로 해줘.
                               """)
            .build();
    }


    @ShellMethod(key = "chat")
    public String interactiveChat(@ShellOption(defaultValue = "Hello MCP Client!") String question) {
        return this.chatClient.prompt(question).call().content();
    }
}
