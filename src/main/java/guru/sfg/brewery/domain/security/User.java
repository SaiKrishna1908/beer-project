package guru.sfg.brewery.domain.security;


import guru.sfg.brewery.repositories.security.AuthorityRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User  {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private String username;
    private String password;

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST} ,fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",referencedColumnName = "ID")}
    )
    private Set<Role> roles ;

    @Transient
    private Set<Authority> authorities;

    public Set<Authority> getAuthorities(){
        return this.roles.stream().map(Role::getAuthorities).flatMap(Set::stream).collect(Collectors.toSet());
    }

    @Builder.Default
    private Boolean accountNonExpired = true;
    @Builder.Default
    private Boolean accountNonLocked = true;
    @Builder.Default
    private Boolean credentialNonExpired = true;
    @Builder.Default
    private Boolean enabled = true;



}
