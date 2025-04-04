package com.project.crawlerservice.entity.embeddable;

import com.project.crawlerservice.enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AssetEmbeddableId {
    @Column(nullable = false, length = 100)
    private String username;
    @Column(name = "CODE", nullable = false, length =  20)
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length =  10)
    private Type type;
}
