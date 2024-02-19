package com.sintaks.mushandi.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Username needs to be an email")
    @NotBlank(message = "username is required")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "Please enter your full name")
    @Column(name="full_name")
    private String fullName;
    //@JsonIgnore
    @NotBlank(message = "Password field is required")
    private String password;
    //@JsonIgnore
    @Transient
    private String confirmPassword;
    private Date create_At;
    private Date update_At;
    @Column(name="status")
	private Integer status=0;
    @OneToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
   
    @Transient
   private Set<String> strRoles=new HashSet<>();

    @PrePersist
    protected void onCreate(){
        this.create_At = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.update_At = new Date();
    }

    /*
    UserDetails interface methods
     */
    @Transient
    private Collection<? extends GrantedAuthority> authorities;
    public User(Long id, String username, String fullName, String password,Employee employee,
			Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
		this.password = password;
		this.employee=employee;
		this.authorities = authorities;
	}

    
    public  User build(User user) {
    	List<GrantedAuthority> authorities = getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new User(
				getId(), 
				getUsername(), 
				getFullName(),
				getPassword(), 
				getEmployee(),
				authorities);
	}
    
    
    
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
    
   }
