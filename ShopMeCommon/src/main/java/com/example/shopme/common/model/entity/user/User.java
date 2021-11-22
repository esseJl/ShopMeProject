package com.example.shopme.common.model.entity.user;

import com.example.shopme.common.annotation.valid.password.ValidPassword;
import com.example.shopme.common.model.entity.media.image.Image;
import com.example.shopme.common.model.entity.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    // @Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(length = 36, nullable = false, unique = true)
    private String userUUID;

    //@email
    @Email
    @NotBlank
    @Column(length = 160, nullable = false, unique = true)
    private String email;

    //@Password
    @ValidPassword
    @Column(length = 64, nullable = false)
    @JsonIgnore
    private String password;

    //@ConfirmPassword
    @Transient
    @JsonIgnore
    private String matchingPassword;


    //@firstName
    @NotBlank
    @Size(min = 4, max = 16)
    @Column(length = 50, nullable = false)
    private String firstName;

    //@lastName
    @NotBlank
    @Size(min = 4, max = 16)
    @Column(length = 50, nullable = false)
    private String lastName;

    //@UserImage
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;


    //@Roles of user
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    //@isUserEnabled
    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    @Transient
    private String captcha;

    @Transient
    private String hiddenCaptcha;

    @Transient
    private String realCaptcha;

    //NoArgConstructor
    public User() {
    }

    //@tools
    //add Role to the user
    public void addRole(Role role) {
        this.roles.add(role);
    }

    //@tools
    //remove Role from the user
    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    //Setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUserUUID(String uuid) {
        this.userUUID = uuid;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setHiddenCaptcha(String hiddenCaptcha) {
        this.hiddenCaptcha = hiddenCaptcha;
    }

    public void setRealCaptcha(String realCaptcha) {
        this.realCaptcha = realCaptcha;
    }

    //Getter

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Image getImage() {
        return image;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getHiddenCaptcha() {
        return hiddenCaptcha;
    }

    public String getRealCaptcha() {
        return realCaptcha;
    }

    //toString

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uuid='" + userUUID + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", image=" + image +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                '}';
    }


    //hash

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return userUUID.equals(user.userUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUUID);
    }
}
