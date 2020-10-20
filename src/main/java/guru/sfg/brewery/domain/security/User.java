package guru.sfg.brewery.domain.security;


import guru.sfg.brewery.repositories.security.AuthorityRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_authority",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID",referencedColumnName = "ID")}
    )
    private Set<Authority> authorities = new HashSet<>();
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialNonExpired;
    private Boolean enabled;



}
