package be.restiau.interactivesatlasspecies_v3.il.configs;

import be.restiau.interactivesatlasspecies_v3.il.adapters.OpenAIAdapter;
import be.restiau.interactivesatlasspecies_v3.il.handlers.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketHandlerConfig {

    @Bean
    public ChatWebSocketHandler chatWebSocketHandler(OpenAIAdapter openAIAdapter) {
        return new ChatWebSocketHandler(openAIAdapter);
    }
}
