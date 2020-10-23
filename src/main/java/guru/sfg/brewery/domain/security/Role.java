package guru.sfg.brewery.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "role_authrority"
    , joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name ="AUTHORITY_ID", referencedColumnName = "ID")}
    )
    private Set<Authority> authorities = new HashSet<>();

}
