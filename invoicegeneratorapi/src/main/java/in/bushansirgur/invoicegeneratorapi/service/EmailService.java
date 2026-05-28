package in.bushansirgur.invoicegeneratorapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @SuppressWarnings("null")
    public void sendInvoiceEmail(String toEmail, MultipartFile file) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Your Invoice from SmartInvoice");

        String htmlContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "  <meta charset='utf-8'>"
                + "  <style>"
                + "    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f3f4f6; margin: 0; padding: 0; }"
                + "    .wrapper { width: 100%; background-color: #f3f4f6; padding: 40px 0; }"
                + "    .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05); }"
                + "    .header { background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%); padding: 40px 20px; text-align: center; }"
                + "    .header h1 { color: #ffffff; font-size: 28px; margin: 0; font-weight: 800; letter-spacing: -1px; }"
                + "    .content { padding: 40px 30px; color: #374151; line-height: 1.6; }"
                + "    .content h2 { font-size: 20px; color: #111827; margin-top: 0; font-weight: 700; }"
                + "    .content p { font-size: 15px; margin-bottom: 20px; color: #4b5563; }"
                + "    .badge-container { text-align: center; margin: 30px 0; }"
                + "    .badge { display: inline-block; padding: 12px 28px; background-color: #f5f3ff; border: 1px solid #ddd6fe; border-radius: 9999px; font-weight: 600; color: #6366f1; font-size: 14px; }"
                + "    .footer { background-color: #f9fafb; padding: 24px 30px; border-top: 1px solid #f3f4f6; text-align: center; font-size: 13px; color: #9ca3af; }"
                + "    .footer a { color: #6366f1; text-decoration: none; }"
                + "  </style>"
                + "</head>"
                + "<body>"
                + "  <div class='wrapper'>"
                + "    <div class='container'>"
                + "      <div class='header'>"
                + "        <h1>SmartInvoice</h1>"
                + "      </div>"
                + "      <div class='content'>"
                + "        <h2>Hello,</h2>"
                + "        <p>You have received a new invoice from <strong>SmartInvoice</strong>.</p>"
                + "        <p>A professional, pixel-perfect PDF copy of your invoice has been generated and is attached directly to this email for your convenience.</p>"
                + "        <div class='badge-container'>"
                + "          <span class='badge'>📄 PDF Invoice Attached</span>"
                + "        </div>"
                + "        <p>If you have any questions or require clarification regarding the billing details, please reply directly to this email or reach out to the sender.</p>"
                + "        <p>Thank you for your business!</p>"
                + "      </div>"
                + "      <div class='footer'>"
                + "        <p>&copy; 2026 SmartInvoice. All Rights Reserved.</p>"
                + "        <p>Effortless Invoicing. Professional Results.</p>"
                + "      </div>"
                + "    </div>"
                + "  </div>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true);
        helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));

        mailSender.send(message);
    }
}
