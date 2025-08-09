package com.example.insurance.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "UnderWriter")
@Getter
@Setter
@NoArgsConstructor
public class UnderWriter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long underwriterId;

    @NotBlank
    private String name;

    @Past
    private LocalDate dob;

    private LocalDate joiningDate;

    @NotBlank
    private String password;

    @Column(unique = true)
    @NotBlank
    private String userName;

    @NotBlank
    private String role = "USER"; // USER or ADMIN
}