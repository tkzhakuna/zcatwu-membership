package com.sintaks.mushandi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@RequiredArgsConstructor
@Setter
@Table(name="site")

public class Site {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NotNull @NotEmpty(message = "Site name is required")
    @Column(name="site_name",unique = true)
    private String siteName;
}
