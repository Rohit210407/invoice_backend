package in.bushansirgur.invoicegeneratorapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*") // Adjust based on your security config
public class PaymentController {

    @Value("${frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @PostMapping("/create-link/{invoiceId}")
    public ResponseEntity<Map<String, String>> createMockPaymentLink(@PathVariable String invoiceId) {
        // Direct to a local mock checkout page
        String paymentLink = frontendUrl + "/pay/" + invoiceId;
        
        Map<String, String> response = new HashMap<>();
        response.put("paymentLink", paymentLink);
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
}
