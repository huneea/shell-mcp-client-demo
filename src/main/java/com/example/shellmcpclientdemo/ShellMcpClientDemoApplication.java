package com.example.shellmcpclientdemo;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ShellMcpClientDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShellMcpClientDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(List<McpSyncClient> mcpSyncClients, ConfigurableApplicationContext applicationContext) {
        return args -> {
            System.out.println("MCP Client Application started!");
            System.out.println("MCP Sync Clients: " + mcpSyncClients);
            System.out.println("MCP Sync Client Count: " + mcpSyncClients.size());

            // mcpSyncClients 에서 특정 이름의 클라이언트를 가져와서 사용
            for (McpSyncClient client : mcpSyncClients) {

                System.out.println("Server Name: " + client.getServerInfo().name());
            }

            mcpSyncClients
                .stream()
                .filter(client -> client.getServerInfo().name().equals("secure-filesystem-server"))
                .findFirst()
                .ifPresent(fileSystem -> {
                    McpSchema.ListToolsResult listTools = fileSystem.listTools();
                    System.out.println(listTools);

                    McpSchema.CallToolResult listDirectory = fileSystem.callTool(new McpSchema.CallToolRequest("list_directory",
                                                                                                               Map.of("path", "/Users/nhn/Desktop/demo")));
                    System.out.println(listDirectory);
                });

            mcpSyncClients
                .stream()
                .filter(client -> client.getServerInfo().name().equals("kor-address-mcp-demo"))
                .findFirst()
                .ifPresent(koreaAddressFinder -> {
                    McpSchema.ListToolsResult listTools = koreaAddressFinder.listTools();
                    System.out.println(listTools);

                    McpSchema.CallToolResult findAddressResult = koreaAddressFinder.callTool(new McpSchema.CallToolRequest("kor-address-find-address",
                                                                                                                           Map.of("currentPage", 1,
                                                                                                                                  "countPerPage", 10,
                                                                                                                                  "searchKeyword", "NHN")));
                    System.out.println(findAddressResult);
                });

            applicationContext.close();
        };
    }


}
