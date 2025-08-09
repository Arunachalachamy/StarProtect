package com.example.insurance.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Vehicle")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true)
    @NotBlank
    private String vehicleNo;

    @NotBlank
    private String vehicleType; // 2-wheeler or 4-wheeler

    @NotBlank
    @Size(max = 50)
    private String customerName;

    private Integer engineNo;

    private Integer chasisNo;

    @Size(min = 10, max = 10)
    private String phoneNo;

    @NotBlank
    private String type; // Full Insurance/ThirdParty

    private java.time.LocalDate fromDate;

    private java.time.LocalDate toDate;

    private Double premiumAmnt;

    @NotNull
    private Long underwriterId;

    private String policyId; // generated unique policy id
}