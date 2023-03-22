package com.guoran.service.impl;

import com.guoran.dao.AdminMapper;
import com.guoran.domain.Admin;
import com.guoran.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(Admin admin) {
        //Admin loginAdmin = adminMapper.login(admin);
        return null;
    }
}
