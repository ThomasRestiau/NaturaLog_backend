package be.restiau.interactivesatlasspecies_v3.dl.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode @ToString
@MappedSuperclass
public abstract class BaseEntity<T> {


//    @Setter // à décommenter uniquement pour les tests
    @Id
    @GeneratedValue
    private T id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}