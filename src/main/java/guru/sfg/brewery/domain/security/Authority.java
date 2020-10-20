package guru.sfg.brewery.domain.security;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private String role;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> user  = new HashSet<>();



}
