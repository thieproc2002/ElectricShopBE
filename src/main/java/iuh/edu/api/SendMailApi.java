package iuh.edu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iuh.edu.repository.UserRepository;
import iuh.edu.service.SendMailService;

@CrossOrigin("*")
@RestController
@RequestMapping("api/send-mail")
public class SendMailApi {

    @Autowired
    SendMailService sendMail;

    @Autowired
    UserRepository Urepo;

    @PostMapping("/otp")
    public ResponseEntity<Integer> sendOpt(@RequestBody String email) {
        int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
//        if (Urepo.existsByEmail(email)) {
//            return ResponseEntity.notFound().build();
//        }
        sendMailOtp(email, random_otp, "Xác nhận tài khoản!");
        return ResponseEntity.ok(random_otp);
    }

    // sendmail
    public void sendMailOtp(String email, int Otp, String title) {
        String body = "<div>\r\n" + "        <h3>Mã OTP của bạn là: <span style=\"color:red; font-weight: bold;\">"
                + Otp + "</span></h3>\r\n" + "    </div>";
        sendMail.queue(email, title, body);
    }

}