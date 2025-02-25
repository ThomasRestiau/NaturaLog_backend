package be.restiau.interactivespeciesatlas_v3.dl.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true) @ToString
@Getter @Setter
@Entity
public class Species extends BaseEntity<Long>{

    @Column(length = 50, unique = true, nullable = false)
    private String speciesKey;

    @Column(nullable = false, length = 50)
    private String vernacularName;

}
