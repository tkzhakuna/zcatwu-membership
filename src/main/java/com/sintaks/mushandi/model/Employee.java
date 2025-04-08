package com.sintaks.mushandi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name="employees")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="trade_union_id")
    @NotNull(message = "Trade union is required")
    private TradeUnion tradeUnion;
    @Column(name="employee_name")
    @NotNull(message = "Full name is required")
    private String fullname;
    @Email(message="Invalid email")
    @NotNull(message = "Email is required")
    private String email;
    private String phone;
}
