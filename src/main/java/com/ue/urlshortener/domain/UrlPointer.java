package com.ue.urlshortener.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "url_pointer",
        indexes = {
                @Index(columnList = "target_identifier"),
                @Index(columnList = "target"),
                @Index(name = "ownerTargetIdx", columnList = "target_identifier, owner")})
@Data
@NoArgsConstructor
public final class UrlPointer {
    @GeneratedValue
    @Id
    private long id;
    @Column(nullable = false)
    private String target;
    @Column(unique = true, name = "target_identifier")
    private String targetIdentifier;
    @Column(nullable = false)
    private String owner;
    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt; //Can be used for retention Post-MVP if decided

    public UrlPointer(String target, String targetIdentifier, String owner) {
        this.target = target;
        this.targetIdentifier = targetIdentifier;
        this.owner = owner;
    }

    public Optional<UrlPointer> updatePointer(String newTarget, String targetIdentifier, String owner) {
        if (this.owner.equals(owner)) {
            // To enable auditability records are considered immutable
            // Historical tracking is not required, if needed, a "parent id" field and/or a "child ids" can be added
            return Optional.of(new UrlPointer(newTarget, targetIdentifier, owner));
        } else {
            return Optional.empty();
        }
    }
}
