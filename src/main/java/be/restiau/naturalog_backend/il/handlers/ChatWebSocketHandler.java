package be.restiau.naturalog_backend.il.handlers;

import be.restiau.naturalog_backend.il.adapters.OpenAIAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final OpenAIAdapter openAIAdapter;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userMessage = message.getPayload();
        log.info("Message reçu du client : {}", userMessage);

        openAIAdapter.getResponse(userMessage)
                .doOnNext(botResponse -> {
                    try {
                        session.sendMessage(new TextMessage(botResponse));
                        log.info("Réponse envoyée au client : {}", botResponse);
                    } catch (Exception e) {
                        log.error("Erreur lors de l’envoi de la réponse WebSocket", e);
                        try {
                            session.sendMessage(new TextMessage("❌ Une erreur est survenue, veuillez réessayer plus tard."));
                        } catch (Exception ex) {
                            log.error("Impossible d’envoyer le message d’erreur", ex);
                        }
                    }
                })
                .subscribe(); // ⚠️ indispensable pour exécuter le Mono
    }
}
