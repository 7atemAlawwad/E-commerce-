package com.example.ecommerce.Service;

import com.example.ecommerce.Model.Merchant;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class MerchantService {
    ArrayList<Merchant> merchants = new ArrayList<>();


    public ArrayList<Merchant> getAllMerchants() {
        return merchants;
    }

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    public boolean updateMerchant(Merchant merchant, String id) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.set(i, merchant);
                return true;
            }

        }
        return false;
    }

    public boolean removeMerchant(String id) {
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(id)) {
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }

    public int setVacation(String merchantId, LocalDate from, LocalDate to) {

        Merchant m = null;
        int idx = -1;
        for (int i = 0; i < merchants.size(); i++) {
            if (merchants.get(i).getId().equals(merchantId)) {
                m = merchants.get(i);
                idx = i;
                break;
            }
        }
        if (m == null) return 0;

        if ((from == null && to != null) || (from != null && to == null)) return -1;

        if (from != null && to != null && to.isBefore(from)) return -2;

        m.setVacationFrom(from);
        m.setVacationTo(to);
        merchants.set(idx, m);
        return 1;
    }

    public boolean isOnVacation(String merchantId) {
        for (int i = 0; i < merchants.size(); i++) {
            Merchant m = merchants.get(i);
            if (m.getId().equals(merchantId)) {
                if (m.getVacationFrom() == null || m.getVacationTo() == null) return false;
                LocalDate today = LocalDate.now();
                return (!today.isBefore(m.getVacationFrom()) && !today.isAfter(m.getVacationTo()));
            }
        }
        return false;
    }

}
