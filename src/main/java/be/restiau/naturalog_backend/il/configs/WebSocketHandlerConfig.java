package be.restiau.naturalog_backend.il.configs;

import be.restiau.naturalog_backend.il.adapters.OpenAIAdapter;
import be.restiau.naturalog_backend.il.handlers.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketHandlerConfig {

    @Bean
    public ChatWebSocketHandler chatWebSocketHandler(OpenAIAdapter openAIAdapter) {
        return new ChatWebSocketHandler(openAIAdapter);
    }
}
