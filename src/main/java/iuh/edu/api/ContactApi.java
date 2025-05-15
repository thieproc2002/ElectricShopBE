package iuh.edu.api;

import java.util.List;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import iuh.edu.entity.Contact;
import iuh.edu.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*") // Cho phép CORS từ tất cả các origin
@RestController
@RequestMapping("/api/contacts")
public class ContactApi {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        try {
            // Lưu contact vào cơ sở dữ liệu
            Contact savedContact = contactRepository.save(contact);
            return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(contactRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable("id") Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody Contact contact, @RequestParam String replyMessage) {
        try {
            // Lưu contact vào cơ sở dữ liệu
            Contact savedContact = contactRepository.save(contact);

            // Gửi email trả lời
            sendEmailNotification(savedContact, replyMessage);

            // Cập nhật trạng thái của contact sau khi gửi email thành công
            savedContact.setStatus(2);
            contactRepository.save(savedContact);

            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }

    private void sendEmailNotification(Contact contact, String message) {
        try {
            // Tạo đối tượng MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Thiết lập thông tin email
            helper.setTo(contact.getEmail());
            helper.setSubject(contact.getSubject());
            helper.setText(message, true); // true để enable HTML

            // Gửi email
            javaMailSender.send(mimeMessage);

            System.out.println("Email sent successfully to " + contact.getEmail());
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

}