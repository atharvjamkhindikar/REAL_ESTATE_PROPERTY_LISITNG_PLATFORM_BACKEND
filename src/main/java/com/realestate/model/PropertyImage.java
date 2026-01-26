package com.realestate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "property_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Image URL is required")
    @Column(nullable = false)
    private String imageUrl;
    
    @Column(length = 500)
    private String caption;
    
    @Column(nullable = false)
    private Boolean isPrimary = false;
    
    @Column(nullable = false)
    private Integer displayOrder = 0;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    // Many-to-One relationship: Many images belong to one property
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
