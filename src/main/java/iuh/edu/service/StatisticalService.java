package iuh.edu.service;

import iuh.edu.dto.Statistical;
import iuh.edu.repository.OrderRepository;
import iuh.edu.repository.StatisticalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticalService {

    @Autowired
    private StatisticalRepository statisticalRepository;

    public List<Statistical> getMonthlyRevenueByYear(int year) {
        List<Object[]> results = statisticalRepository.findMonthlyRevenueByYear(year);
        List<Statistical> statistics = new ArrayList<>();
        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();
            Date date = (Date) row[1];
            Double amount = (Double) row[2];
            int count = ((Number) row[3]).intValue();
            statistics.add(new Statistical(month, date, amount, count));
        }
        return statistics;
    }
}
