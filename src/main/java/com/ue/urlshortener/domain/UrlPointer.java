package com.ue.urlshortener.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "url_pointer",
        indexes = {
                @Index(columnList = "target_identifier"),
                @Index(columnList = "target")})
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
    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt; //Can be used for retention Post-MVP if decided

    public UrlPointer(String target, String targetIdentifier) {
        this.target = target;
        this.targetIdentifier = targetIdentifier;
    }
}
