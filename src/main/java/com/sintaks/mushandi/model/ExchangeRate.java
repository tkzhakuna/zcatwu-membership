package com.sintaks.mushandi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name="exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;
    private Double rate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CAT")
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    @PrePersist
    private void init(){
        date=LocalDateTime.now();
    }
}
