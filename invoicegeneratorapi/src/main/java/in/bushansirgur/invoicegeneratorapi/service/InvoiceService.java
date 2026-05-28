package in.bushansirgur.invoicegeneratorapi.service;

import in.bushansirgur.invoicegeneratorapi.entity.Invoice;
import in.bushansirgur.invoicegeneratorapi.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }


    @SuppressWarnings("null")
    public Invoice saveInvoice(Invoice invoice) {
        if (invoice.getTax() < 0 || invoice.getTax() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tax rate must be between 0 and 100");
        }
        return repository.save(invoice);
    }

    public List<Invoice> fetchInvoices(String clerkId) {
        return repository.findByClerkId(clerkId);
    }

    @SuppressWarnings("null")
    public void removeInvoice(String clerkId, String invoiceId) {
        Invoice existingInvoice = repository.findByClerkIdAndId(clerkId, invoiceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found:" + invoiceId));
        repository.delete(existingInvoice);
    }

    public Invoice fetchInvoiceById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found: " + id));
    }
}
