package com.viettelpost.api.business.impl;

import com.viettelpost.api.business.CustomerService;
import com.viettelpost.api.business.daos.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Oto on 21/02/2020.
 */
@Service
public class CustomerImpl implements CustomerService {

    @Autowired
    CustomerDAO customerDAO;

    @Override
    public String getReply(String text) {
        return customerDAO.getReply(text);
    }
}
