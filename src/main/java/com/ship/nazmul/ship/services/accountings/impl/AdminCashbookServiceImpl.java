package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.accountings.AdminCashbook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.repositories.accountings.AdminCashbookRepository;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AdminCashbookServiceImpl implements AdminCashbookService {
    private final AdminCashbookRepository adminCashbookRepository;

    @Autowired
    public AdminCashbookServiceImpl(AdminCashbookRepository adminCashbookRepository) {
        this.adminCashbookRepository = adminCashbookRepository;
    }

    @Override
    public AdminCashbook save(AdminCashbook adminCashbook) {
        return this.adminCashbookRepository.save(adminCashbook);
    }

    @Override
    public AdminCashbook getAdminCashbook(Long id) {
        return this.adminCashbookRepository.getOne(id);
    }

    @Override
    public List<AdminCashbook> getAllAdminCashbook() {
        return this.adminCashbookRepository.findAll();
    }

    @Override
    public Page<AdminCashbook> getAllAdminCashbook(int page) {
        return this.adminCashbookRepository.findAll(PageAttr.getPageRequestAsc(page));
    }

    @Override
    public boolean isExists(Long id) {
        return this.adminCashbookRepository.exists(id);
    }

    @Override
    public AdminCashbook updateBalanceAndSave(AdminCashbook adminCashbook) {
//        System.out.println("D1 : " + adminCashbook.toString());
//        System.out.println("D2 : " +  this.adminCashbookRepository.findFirstByOrderByIdDesc().toString());
        AdminCashbook lastAdminCashbook = this.adminCashbookRepository.findFirstByOrderByIdDesc();
        int lastBalance = lastAdminCashbook == null ? 0 : lastAdminCashbook.getBalance();
        adminCashbook.setBalance(lastBalance + adminCashbook.getDebit() - adminCashbook.getCredit());
        adminCashbook = this.save(adminCashbook);
        return adminCashbook;
    }

    @Override
    public AdminCashbook addAdminCashbookEntry(Integer debit, Integer credit, String explanation) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.hasRole(Role.ERole.ROLE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");
        AdminCashbook lastCashbook = this.adminCashbookRepository.findFirstByOrderByIdDesc();
        AdminCashbook adminCashbook = new AdminCashbook(LocalDateTime.now(), explanation, debit, credit);
        int lastBalance = lastCashbook == null ? 0 : lastCashbook.getBalance();
        adminCashbook.setBalance(lastBalance + adminCashbook.getDebit() - adminCashbook.getCredit());
        adminCashbook.setApproved(true);
        adminCashbook = this.save(adminCashbook);
        return adminCashbook;
    }
}