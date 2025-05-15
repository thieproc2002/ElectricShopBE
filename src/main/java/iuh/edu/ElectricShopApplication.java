package iuh.edu;

import iuh.edu.entity.Product;
import iuh.edu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElectricShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectricShopApplication.class, args);
    }

//    @Autowired
//    private ProductService pService;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void runAfterStartup() {
//        pService.capNhatTenKhongDau();
//    }
}
