package in.bushansirgur.invoicegeneratorapi.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;

    private Company company;
    private Billing billing;
    private Shipping shipping;
    private InvoiceDetails invoice;
    private List<Item> items;
    private String notes;
    private String logo;
    private double tax;
    private String clerkId;
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastUpdatedAt;
    private String thumbnailUrl;
    private String template;
    private String title;

    // Enterprise Features
    private String status; // Draft, Sent, Paid, Overdue
    private String themeColor;
    private String paymentLink;

    public Invoice() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public Billing getBilling() { return billing; }
    public void setBilling(Billing billing) { this.billing = billing; }
    public Shipping getShipping() { return shipping; }
    public void setShipping(Shipping shipping) { this.shipping = shipping; }
    public InvoiceDetails getInvoice() { return invoice; }
    public void setInvoice(InvoiceDetails invoice) { this.invoice = invoice; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }
    public String getClerkId() { return clerkId; }
    public void setClerkId(String clerkId) { this.clerkId = clerkId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getThemeColor() { return themeColor; }
    public void setThemeColor(String themeColor) { this.themeColor = themeColor; }
    public String getPaymentLink() { return paymentLink; }
    public void setPaymentLink(String paymentLink) { this.paymentLink = paymentLink; }

    public static class Company {
        private String name;
        private String phone;
        private String address;
        private String email;
        private String gst;
        public Company() {}
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
        public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
        public String getGst() { return gst; } public void setGst(String gst) { this.gst = gst; }
    }

    public static class Billing {
        private String name;
        private String phone;
        private String address;
        public Billing() {}
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    }

    public static class Shipping {
        private String name;
        private String phone;
        private String address;
        public Shipping() {}
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    }

    public static class InvoiceDetails {
        private String number;
        private String date;
        private String dueDate;
        public InvoiceDetails() {}
        public String getNumber() { return number; } public void setNumber(String number) { this.number = number; }
        public String getDate() { return date; } public void setDate(String date) { this.date = date; }
        public String getDueDate() { return dueDate; } public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    }

    public static class Item {
        private String name;
        private int qty;
        private double amount;
        private String description;
        public Item() {}
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public int getQty() { return qty; } public void setQty(int qty) { this.qty = qty; }
        public double getAmount() { return amount; } public void setAmount(double amount) { this.amount = amount; }
        public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    }
}