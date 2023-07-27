package com.example.autopartscommon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "currency")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double rub;
    private double usd;
    private Date lastUpdatedDate;

}
