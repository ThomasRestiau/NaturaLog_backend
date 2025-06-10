package be.restiau.interactivesatlasspecies_v3.dl.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true) @ToString
@Getter @Setter
@Cacheable
@Entity
public class Species extends BaseEntity<Long>{

    @Column(unique = true, nullable = false)
    private String gbifId;

    @Column(unique = true, nullable = false)
    private String scientificName;

    @Column(nullable = false)
    private String vernacularName;

    @Column(nullable = false)
    private String canonicalName;

}
