package com.example.insurance.bean;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "InsuranceRenewal")
@Getter
@Setter
@NoArgsConstructor
public class InsuranceRenewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String policyId;

    private LocalDate renewedOn;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Double premiumAmnt;

    private Boolean claimStatus;
}