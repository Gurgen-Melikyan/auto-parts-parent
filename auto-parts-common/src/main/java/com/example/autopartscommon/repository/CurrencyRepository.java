package com.example.autopartscommon.repository;


import com.example.autopartscommon.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {



}
