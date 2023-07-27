package com.example.autopartsrest.service;


import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Currency;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;

import java.util.List;

public interface CurrencyService {

    List<Currency> findAll();
}
