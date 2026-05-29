package in.bushansirgur.invoicegeneratorapi.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String clerkId;
    private String email;
    private String firstName;
    private String lastName;
    private String photoUrl;
    @CreatedDate
    private Instant createdAt;

    // Business Profile Fields
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyGst;
    private String homeCurrency = "INR";

    public User() {}

    public User(String id, String clerkId, String email, String firstName, String lastName, String photoUrl, Instant createdAt) {
        this.id = id;
        this.clerkId = clerkId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
    }

    public User(String id, String clerkId, String email, String firstName, String lastName, String photoUrl, Instant createdAt,
                String companyName, String companyEmail, String companyPhone, String companyAddress, String companyGst, String homeCurrency) {
        this.id = id;
        this.clerkId = clerkId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.companyGst = companyGst;
        this.homeCurrency = homeCurrency != null ? homeCurrency : "INR";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClerkId() { return clerkId; }
    public void setClerkId(String clerkId) { this.clerkId = clerkId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyEmail() { return companyEmail; }
    public void setCompanyEmail(String companyEmail) { this.companyEmail = companyEmail; }

    public String getCompanyPhone() { return companyPhone; }
    public void setCompanyPhone(String companyPhone) { this.companyPhone = companyPhone; }

    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }

    public String getCompanyGst() { return companyGst; }
    public void setCompanyGst(String companyGst) { this.companyGst = companyGst; }

    public String getHomeCurrency() { return homeCurrency; }
    public void setHomeCurrency(String homeCurrency) { this.homeCurrency = homeCurrency; }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String id;
        private String clerkId;
        private String email;
        private String firstName;
        private String lastName;
        private String photoUrl;
        private Instant createdAt;
        private String companyName;
        private String companyEmail;
        private String companyPhone;
        private String companyAddress;
        private String companyGst;
        private String homeCurrency;

        UserBuilder() {}

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder clerkId(String clerkId) {
            this.clerkId = clerkId;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public UserBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public UserBuilder companyEmail(String companyEmail) {
            this.companyEmail = companyEmail;
            return this;
        }

        public UserBuilder companyPhone(String companyPhone) {
            this.companyPhone = companyPhone;
            return this;
        }

        public UserBuilder companyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
            return this;
        }

        public UserBuilder companyGst(String companyGst) {
            this.companyGst = companyGst;
            return this;
        }

        public UserBuilder homeCurrency(String homeCurrency) {
            this.homeCurrency = homeCurrency;
            return this;
        }

        public User build() {
            return new User(id, clerkId, email, firstName, lastName, photoUrl, createdAt,
                    companyName, companyEmail, companyPhone, companyAddress, companyGst, homeCurrency);
        }
    }
}
