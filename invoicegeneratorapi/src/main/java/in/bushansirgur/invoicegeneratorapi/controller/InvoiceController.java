package in.bushansirgur.invoicegeneratorapi.controller;

import in.bushansirgur.invoicegeneratorapi.entity.Invoice;
import in.bushansirgur.invoicegeneratorapi.service.EmailService;
import in.bushansirgur.invoicegeneratorapi.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*") // for frontend access
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService service;
    private final EmailService emailService;

    public InvoiceController(InvoiceService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }


    @PostMapping
    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice, Authentication authentication) {
        invoice.setClerkId(authentication.getName());
        return ResponseEntity.ok(service.saveInvoice(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> fetchInvoices(Authentication authentication) {
        return ResponseEntity.ok(service.fetchInvoices(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeInvoice(@PathVariable String id, Authentication authentication) {
        if (authentication.getName() != null) {
            service.removeInvoice(authentication.getName(), id);
            return ResponseEntity.noContent().build();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "User does not have permission to access this resource");
    }

    @PostMapping("/sendinvoice")
    public ResponseEntity<?> sendInvoice(@RequestPart("file") MultipartFile file,
            @RequestPart("email") String customerEmail) {
        try {
            emailService.sendInvoiceEmail(customerEmail, file);
            return ResponseEntity.ok().body("Invoice sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send invoice.");
        }
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Invoice> getPublicInvoice(@PathVariable String id) {
        return ResponseEntity.ok(service.fetchInvoiceById(id));
    }

    @PostMapping("/public/{id}/pay")
    public ResponseEntity<?> payPublicInvoice(@PathVariable String id) {
        Invoice invoice = service.fetchInvoiceById(id);
        invoice.setStatus("Paid");
        service.saveInvoice(invoice);
        return ResponseEntity.ok().body("Payment completed successfully!");
    }
}
