package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false, unique = true)
    private Long idUser;

    @Column(name = "user_sku", nullable = false, unique = true)
    private Long userSku;

    @Column(name = "user_name", length = 50, nullable = false)
    private String name;
    @Column(name = "user_surname", length = 50, nullable = false)
    private String surname;
    @Column(name = "user_email", length = 100, nullable = false, unique = true)
    private String email;
    @Column(name = "user_password", length = 75, nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_function", referencedColumnName = "id_function")
    private Function function;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Long createSku() {
        long generated = (long) (Math.random() * 9999999L);
        this.userSku = (generated < 0 ? generated * -1 : generated);
        return this.userSku;
    }

    @Builder
    public User(String name, String surname, String email, String password, Function function, List<Booking> bookings) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.function = function;
        this.bookings = bookings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result =1;
        result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
        return result;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.function.getName()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
