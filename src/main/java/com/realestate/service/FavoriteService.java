package com.realestate.service;

import com.realestate.exception.DuplicateResourceException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.model.Favorite;
import com.realestate.model.Property;
import com.realestate.model.User;
import com.realestate.repository.FavoriteRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PropertyRepository propertyRepository;
    
    public Favorite addFavorite(Long userId, Long propertyId, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", propertyId));

        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            throw new DuplicateResourceException("Favorite", "userId and propertyId", userId + "," + propertyId);
        }
        
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProperty(property);
        favorite.setNotes(notes);
        
        return favoriteRepository.save(favorite);
    }
    
    public Favorite addFavorite(Long userId, Long propertyId) {
        return addFavorite(userId, propertyId, null);
    }
    
    public void removeFavorite(Long userId, Long propertyId) {
        Favorite favorite = favoriteRepository.findByUserIdAndPropertyId(userId, propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found for user " + userId + " and property " + propertyId));
        favoriteRepository.delete(favorite);
    }
    
    public void removeFavoriteById(Long favoriteId) {
        if (!favoriteRepository.existsById(favoriteId)) {
            throw new ResourceNotFoundException("Favorite", "id", favoriteId);
        }
        favoriteRepository.deleteById(favoriteId);
    }
    
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
    
    public List<Property> getUserFavoriteProperties(Long userId) {
        return favoriteRepository.findFavoritePropertiesByUserId(userId);
    }
    
    public boolean isFavorited(Long userId, Long propertyId) {
        return favoriteRepository.existsByUserIdAndPropertyId(userId, propertyId);
    }
    
    public Long getFavoriteCount(Long propertyId) {
        return favoriteRepository.countByPropertyId(propertyId);
    }
    
    public Optional<Favorite> getFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId);
    }
    
    public Favorite updateFavoriteNotes(Long favoriteId, String notes) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "id", favoriteId));
        favorite.setNotes(notes);
        return favoriteRepository.save(favorite);
    }
    
    public Favorite toggleFavorite(Long userId, Long propertyId) {
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndPropertyId(userId, propertyId);
        
        if (existingFavorite.isPresent()) {
            favoriteRepository.delete(existingFavorite.get());
            return null; // Removed
        } else {
            return addFavorite(userId, propertyId);
        }
    }
}
