package in.bushansirgur.invoicegeneratorapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.bushansirgur.invoicegeneratorapi.entity.User;
import in.bushansirgur.invoicegeneratorapi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.svix.Webhook;

@RestController
@RequestMapping("/api/webhooks")
public class ClerkWebhookController {

    @Value("${clerk.webhook.secret}")
    private String webhookSecret;

    private final UserService userService;

    public ClerkWebhookController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/clerk")
    public ResponseEntity<?> handleClerkWebhook(@RequestHeader("svix-id") String svixId,
            @RequestHeader("svix-timestamp") String svixTimestamp,
            @RequestHeader("svix-signature") String svixSignature,
            @RequestBody String payload) {
        try {
            verifyWebhookSignature(svixId, svixTimestamp, svixSignature, payload);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payload);

            String eventType = rootNode.path("type").asText();

            switch (eventType) {
                case "user.created":
                    handleUserCreated(rootNode.path("data"));
                    break;
                case "user.updated":
                    handleUserUpdated(rootNode.path("data"));
                    break;
                case "user.deleted":
                    handleUserDeleted(rootNode.path("data"));
                    break;
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    private void handleUserDeleted(JsonNode data) {
        String clerkId = data.path("id").asText();
        userService.deleteAccount(clerkId);
    }

    private void handleUserUpdated(JsonNode data) {
        String clerkId = data.path("id").asText();
        User existingUser = userService.getAccountByClerkId(clerkId);

        existingUser.setEmail(data.path("email_addresses").path(0).path("email_address").asText());
        existingUser.setFirstName(data.path("first_name").asText());
        existingUser.setLastName(data.path("last_name").asText());
        existingUser.setPhotoUrl(data.path("image_url").asText());

        userService.saveOrUpdateUser(existingUser);
    }

    private void handleUserCreated(JsonNode data) {

        User newUser = User.builder()
                .clerkId(data.path("id").asText())
                .email(data.path("email_addresses").path(0).path("email_address").asText())
                .firstName(data.path("first_name").asText())
                .lastName(data.path("last_name").asText())
                .build();
        userService.saveOrUpdateUser(newUser);
    }

    private boolean verifyWebhookSignature(String svixId, String svixTimestamp, String svixSignature, String payload) {
        try {
            Webhook webhook = new Webhook(webhookSecret);
            java.util.Map<String, java.util.List<String>> headersMap = new java.util.HashMap<>();
            headersMap.put("svix-id", java.util.List.of(svixId));
            headersMap.put("svix-timestamp", java.util.List.of(svixTimestamp));
            headersMap.put("svix-signature", java.util.List.of(svixSignature));
            java.net.http.HttpHeaders headers = java.net.http.HttpHeaders.of(headersMap, (k, v) -> true);
            webhook.verify(payload, headers);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Webhook verification failed", e);
        }
    }
}
