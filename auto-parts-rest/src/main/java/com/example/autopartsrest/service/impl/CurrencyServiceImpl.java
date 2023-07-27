package com.example.autopartsrest.service.impl;

import com.example.autopartscommon.entity.Cart;
import com.example.autopartscommon.entity.Currency;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.CartRepository;
import com.example.autopartscommon.repository.CurrencyRepository;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartscommon.repository.UserRepository;
import com.example.autopartsrest.service.CartService;
import com.example.autopartsrest.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
  private final CurrencyRepository currencyRepository;


    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }
}
