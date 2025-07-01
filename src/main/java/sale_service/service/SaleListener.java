package sale_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sale_service.security.TokenService;

@Service
public class SaleListener {
    private static final Logger logger = LoggerFactory.getLogger(SaleListener.class);

    @Autowired
    private TokenService tokenService;

    @KafkaListener(topics = "auth-topic", groupId = "auth-group")
    public void listen(String token) {
        logger.info("Received authentication token: {}", token);
        validateAndStoreToken(token);
    }

    private void validateAndStoreToken(String token) {
        if (token != null && !token.isEmpty()) {
            String subject = tokenService.validateToken(token);
            if (!subject.isEmpty()) {
                logger.info("Token validated successfully for user: {}", subject);
            } else {
                logger.error("Invalid token received");
            }
        } else {
            logger.error("Empty token received");
        }
    }
}
