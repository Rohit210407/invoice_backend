package in.bushansirgur.invoicegeneratorapi.scheduler;

import in.bushansirgur.invoicegeneratorapi.entity.Invoice;
import in.bushansirgur.invoicegeneratorapi.repository.InvoiceRepository;
import in.bushansirgur.invoicegeneratorapi.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Component
public class RecurringInvoiceScheduler {

    private final InvoiceRepository invoiceRepository;
    private final EmailService emailService;

    public RecurringInvoiceScheduler(InvoiceRepository invoiceRepository, EmailService emailService) {
        this.invoiceRepository = invoiceRepository;
        this.emailService = emailService;
    }

    // Runs once every hour to process due recurring invoices
    @Scheduled(fixedRate = 3600000) 
    public void processRecurringInvoices() {
        System.out.println("Processing recurring invoices at: " + Instant.now());
        try {
            List<Invoice> activeRecurringTemplates = invoiceRepository.findByIsRecurringAndRecurrenceStatus(true, "ACTIVE");

            for (Invoice template : activeRecurringTemplates) {
                if (template.getNextGenerationDate() != null && template.getNextGenerationDate().isBefore(Instant.now())) {
                    try {
                        // 1. Create a clone representing the newly generated invoice
                        Invoice nextInvoice = new Invoice();
                        nextInvoice.setCompany(template.getCompany());
                        nextInvoice.setBilling(template.getBilling());
                        nextInvoice.setShipping(template.getShipping());
                        
                        // Generate new details
                        Invoice.InvoiceDetails nextDetails = new Invoice.InvoiceDetails();
                        nextDetails.setNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                        nextDetails.setDate(Instant.now().toString().substring(0, 10));
                        
                        // Calculate due date (default to 30 days due)
                        Instant now = Instant.now();
                        Instant nextDueDate = now.plus(30, ChronoUnit.DAYS);
                        nextDetails.setDueDate(nextDueDate.toString().substring(0, 10));
                        
                        nextInvoice.setInvoice(nextDetails);
                        nextInvoice.setItems(template.getItems());
                        nextInvoice.setNotes(template.getNotes());
                        nextInvoice.setLogo(template.getLogo());
                        nextInvoice.setTax(template.getTax());
                        nextInvoice.setClerkId(template.getClerkId());
                        nextInvoice.setTemplate(template.getTemplate());
                        nextInvoice.setTitle("Recurring: " + template.getTitle());
                        nextInvoice.setStatus("Sent"); // Mark as Sent immediately
                        nextInvoice.setThemeColor(template.getThemeColor());
                        
                        // Multi-currency details
                        nextInvoice.setCurrency(template.getCurrency());
                        nextInvoice.setExchangeRate(template.getExchangeRate());
                        nextInvoice.setLanguage(template.getLanguage());

                        // Save the new invoice
                        Invoice savedInvoice = invoiceRepository.save(nextInvoice);

                        // 2. Generate Public Payment Checkout Link
                        String payLink = "http://localhost:5173/pay/" + savedInvoice.getId();
                        savedInvoice.setPaymentLink(payLink);
                        invoiceRepository.save(savedInvoice);

                        // 3. Email client the payment link
                        if (template.getBilling() != null && template.getBilling().getEmail() != null && !template.getBilling().getEmail().isEmpty()) {
                            double subtotal = savedInvoice.getItems().stream().mapToDouble(i -> i.getQty() * i.getAmount()).sum();
                            double total = subtotal + (subtotal * savedInvoice.getTax() / 100);
                            emailService.sendRecurringInvoiceLinkEmail(
                                    template.getBilling().getEmail(),
                                    savedInvoice.getInvoice().getNumber(),
                                    payLink,
                                    total,
                                    savedInvoice.getCurrency()
                            );
                        }

                        // 4. Update the template record for the next interval
                        template.setLastGeneratedDate(now);
                        Instant nextGen = calculateNextGenerationDate(now, template.getRecurringInterval());
                        template.setNextGenerationDate(nextGen);
                        invoiceRepository.save(template);
                        
                        System.out.println("Successfully processed and emailed recurring invoice: " + nextDetails.getNumber());
                    } catch (Exception e) {
                        System.err.println("Failed to process recurring invoice for template: " + template.getId());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing recurring invoices scheduler");
            e.printStackTrace();
        }
    }

    private Instant calculateNextGenerationDate(Instant from, String interval) {
        if ("WEEKLY".equalsIgnoreCase(interval)) {
            return from.plus(7, ChronoUnit.DAYS);
        } else if ("YEARLY".equalsIgnoreCase(interval)) {
            return from.plus(365, ChronoUnit.DAYS);
        } else { // default to MONTHLY
            return from.plus(30, ChronoUnit.DAYS);
        }
    }
}
