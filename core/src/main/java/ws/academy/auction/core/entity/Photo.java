package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @Column(name = "guid", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "path")
    private String path;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_guid")
    private UUID entityGuid;
}
