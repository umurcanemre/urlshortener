package com.ue.urlshortener.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "url_pointer",
        indexes = {
                @Index(columnList = "target_identifier"),
                @Index(columnList = "target")})
@Data
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
public final class UrlPointer {
    @GeneratedValue
    @Id
    private long id;
    @Column(unique = true, nullable = false) // at the moment, unique constraint will result rigidness and will have monetary & performance implications
    private String target;                   // open to discussion but current constraints can be lifted for a "mostly unique" approach that would result in a more flexible system in the face of inconsistencies but would need some changes in repository and application layers
    @Column(unique = true, nullable = false, name = "target_identifier")
    private String targetIdentifier;
    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt; //Can be used for retention Post-MVP if decided

    public UrlPointer(String target, String targetIdentifier) {
        this.target = target;
        this.targetIdentifier = targetIdentifier;
    }
}
