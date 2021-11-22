package com.example.shopme.common.model.entity.role;

import com.example.shopme.common.model.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 36, nullable = false, unique = true)
    private String roleUUID;

    // @name of role
    @Column(length = 40, nullable = false, unique = true)
    @NotBlank
    private String name;

    // @description of role
    @Column(length = 150, nullable = false)
    @NotBlank
    private String description;

    // @user of role
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
            ,fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();


    // @NoArgConstructor4Role
    public Role() {
    }


    // @ArgsConstructor
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }


    //Getter

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public String getRoleUUID() {
        return roleUUID;
    }

    //Setter

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setRoleUUID(String uuid) {
        this.roleUUID = uuid;
    }

    //toString

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
