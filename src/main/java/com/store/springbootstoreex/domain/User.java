package com.store.springbootstoreex.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    @Email(message = "this is not email!")
    @Size(min = 1, max = 255, message = "too long email (or empty!)")
    @NotNull
    private String email;

    @Column(name = "FIRST_NAME", nullable = false, length = 100)
    @Size(min = 1, max = 100, message = "firstname can not be empty or >100 characters long!")
    @NotNull
    private String firstname;

    @Column(name = "LAST_NAME", nullable = false, length = 100)
    @Size(min = 1, max = 100, message = "lastname can not be empty or >100 characters long!")
    @NotNull
    private String lastname;

    @Column(name = "PASSWORD", nullable = false)
    @NotNull
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 25)
    @NotNull
    private Role role = Role.USER;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 25)
    @NotNull
    private Status status = Status.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CART_ID", referencedColumnName = "ID", nullable = false)
    @NotNull
    private Cart cart = new Cart();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "author")
    private List<Review> comments;

    public User() {
    }

    public User(String email, String firstname, String lastname, String password, Role role, Status status) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive() {
        return this.getStatus().equals(Status.ACTIVE);
    }

    public List<Review> getComments() {
        return comments;
    }

    public void setComments(List<Review> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
