package iuh.edu.service;

import iuh.edu.entity.Product;
import iuh.edu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository pRepository;

    public void capNhatTenKhongDau() {
        List<Product> danhSach = pRepository.findAll();
        for (Product sp : danhSach) {
            String khongDau = removeVietnameseAccent(sp.getName());
            sp.setNormalizedName(khongDau);
        }
        pRepository.saveAll(danhSach);
    }

    public String removeVietnameseAccent(String input) {
        String[] a = { "à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă", "ắ", "ằ", "ắ", "ặ", "ẳ", "ẵ", "a" };
        String[] d = { "đ", "d" };
        String[] e = { "è", "é", "ẹ", "ẻ", "ẽ", "ê", "ề", "ế", "ệ", "ể", "ễ", "e" };
        String[] i = { "ì", "í", "ị", "ỉ", "ĩ", "i" };
        String[] y = { "ỳ", "ý", "ỵ", "ỷ", "ỹ", "y" };
        String[] o = { "ò", "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ", "ờ", "ớ", "ợ", "ở", "ỡ", "o" };
        String[] u = { "ù", "ú", "ụ", "ủ", "ũ", "ừ", "ứ", "ự", "ử", "ữ", "u", "ư" };
        // thay thế ký tự đặc biệt theo ý muốn
//        input = input.replace(" ", "-");
        input = input.replace("#", "sharp");
        input = input.replace("$", "dola");
        // khai báo mảng ký tự đặt biệt
        String[] specialchars = { ")", "(", "*", "[", "]", "}", "{", ">", "<", "=", ":", ",", "'", "\"", "/", "\\", "&",
                "?", ";", ".", "@", "^" };
        // chuyển qua chữ thường
        input = input.toLowerCase();
        // thay thế ký tự có dấu thành không dấu
        for (int k = 0; k < specialchars.length; k++)
            input = input.replace(specialchars[k], "");
        for (int k = 0; k < a.length; k++)
            input = input.replace(a[k], "a");
        for (int k = 0; k < d.length; k++)
            input = input.replace(d[k], "d");
        for (int k = 0; k < e.length; k++)
            input = input.replace(e[k], "e");
        for (int k = 0; k < i.length; k++)
            input = input.replace(i[k], "i");
        for (int k = 0; k < y.length; k++)
            input = input.replace(y[k], "y");
        for (int k = 0; k < o.length; k++)
            input = input.replace(o[k], "o");
        for (int k = 0; k < u.length; k++)
            input = input.replace(u[k], "u");
        return input;
    }
}

