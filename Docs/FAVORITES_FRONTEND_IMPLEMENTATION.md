# ✅ FAVORITES DEFAULT STATE FIX - FRONTEND IMPLEMENTATION

## Issue
Properties show as "Favorited" by default. They should show as "Not Favorited" until the user explicitly adds them to favorites.

## Solution Overview

The frontend needs to:
1. ✅ Load each property
2. ✅ Check if user has favorited it (call `/api/favorites/check`)
3. ✅ Display unfilled heart ❤️ by default (not favorited)
4. ✅ Only show filled heart ❤️ if user added it to favorites
5. ✅ Toggle on click

---

## Backend Endpoints Available

### 1. Check if Property is Favorited
```
GET /api/favorites/check?userId=1&propertyId=1

Response:
{
  "success": true,
  "data": {
    "isFavorited": false  // or true
  }
}
```

### 2. Toggle Favorite
```
POST /api/favorites/toggle?userId=1&propertyId=1

Response if added:
{
  "success": true,
  "message": "Property added to favorites",
  "data": { ... }
}

Response if removed:
{
  "success": true,
  "message": "Property removed from favorites",
  "data": null
}
```

### 3. Add Favorite
```
POST /api/favorites?userId=1&propertyId=1

Response:
{
  "success": true,
  "message": "Property added to favorites",
  "data": { ... }
}
```

### 4. Remove Favorite
```
DELETE /api/favorites?userId=1&propertyId=1

Response:
{
  "success": true,
  "message": "Property removed from favorites",
  "data": null
}
```

---

## React Implementation Example

### 1. Create FavoriteButton Component

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const FavoriteButton = ({ propertyId, userId }) => {
  const [isFavorited, setIsFavorited] = useState(false);
  const [loading, setLoading] = useState(true);

  // Check if property is favorited on mount
  useEffect(() => {
    checkFavoriteStatus();
  }, [userId, propertyId]);

  const checkFavoriteStatus = async () => {
    if (!userId) {
      setLoading(false);
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/api/favorites/check`,
        {
          params: {
            userId,
            propertyId
          }
        }
      );
      
      // By default should be false unless user added it
      setIsFavorited(response.data.data.isFavorited);
    } catch (error) {
      console.error('Error checking favorite status:', error);
      setIsFavorited(false); // Default to not favorited on error
    } finally {
      setLoading(false);
    }
  };

  const handleToggleFavorite = async (e) => {
    e.preventDefault();
    e.stopPropagation();

    if (!userId) {
      alert('Please login to add favorites');
      return;
    }

    try {
      const response = await axios.post(
        `http://localhost:8080/api/favorites/toggle`,
        {},
        {
          params: {
            userId,
            propertyId
          }
        }
      );

      // Toggle state based on response
      if (response.data.data === null) {
        // Removed from favorites
        setIsFavorited(false);
      } else {
        // Added to favorites
        setIsFavorited(true);
      }
    } catch (error) {
      console.error('Error toggling favorite:', error);
    }
  };

  if (loading) {
    return <span>...</span>;
  }

  return (
    <button
      onClick={handleToggleFavorite}
      className={`favorite-btn ${isFavorited ? 'favorited' : 'not-favorited'}`}
      title={isFavorited ? 'Remove from favorites' : 'Add to favorites'}
    >
      {isFavorited ? '❤️ Favorited' : '🤍 Add to Favorites'}
    </button>
  );
};

export default FavoriteButton;
```

### 2. Use in Property Card

```jsx
const PropertyCard = ({ property, userId }) => {
  return (
    <div className="property-card">
      <img src={property.imageUrl} alt={property.title} />
      <h3>{property.title}</h3>
      <p>{property.address}</p>
      <p className="price">${property.price}</p>
      
      {/* Add favorite button */}
      <FavoriteButton 
        propertyId={property.id} 
        userId={userId}
      />
    </div>
  );
};
```

### 3. CSS Styling

```css
.favorite-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.favorite-btn.not-favorited {
  background-color: #f0f0f0;
  color: #333;
}

.favorite-btn.not-favorited:hover {
  background-color: #ffcccc;
  color: #d32f2f;
}

.favorite-btn.favorited {
  background-color: #ffcccc;
  color: #d32f2f;
}

.favorite-btn.favorited:hover {
  background-color: #ff9999;
}
```

---

## Vue.js Implementation Example

```vue
<template>
  <button
    @click="toggleFavorite"
    :class="['favorite-btn', isFavorited ? 'favorited' : 'not-favorited']"
    :disabled="loading"
  >
    {{ isFavorited ? '❤️ Favorited' : '🤍 Add to Favorites' }}
  </button>
</template>

<script>
import axios from 'axios';

export default {
  props: ['propertyId', 'userId'],
  data() {
    return {
      isFavorited: false,
      loading: true
    };
  },
  mounted() {
    this.checkFavoriteStatus();
  },
  methods: {
    async checkFavoriteStatus() {
      if (!this.userId) {
        this.loading = false;
        return;
      }

      try {
        const response = await axios.get(
          'http://localhost:8080/api/favorites/check',
          {
            params: {
              userId: this.userId,
              propertyId: this.propertyId
            }
          }
        );
        this.isFavorited = response.data.data.isFavorited;
      } catch (error) {
        console.error('Error checking favorite:', error);
        this.isFavorited = false;
      } finally {
        this.loading = false;
      }
    },

    async toggleFavorite() {
      if (!this.userId) {
        alert('Please login to add favorites');
        return;
      }

      try {
        const response = await axios.post(
          'http://localhost:8080/api/favorites/toggle',
          {},
          {
            params: {
              userId: this.userId,
              propertyId: this.propertyId
            }
          }
        );

        this.isFavorited = response.data.data !== null;
      } catch (error) {
        console.error('Error toggling favorite:', error);
      }
    }
  }
};
</script>

<style scoped>
.favorite-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.favorite-btn.not-favorited {
  background-color: #f0f0f0;
  color: #333;
}

.favorite-btn.not-favorited:hover {
  background-color: #ffcccc;
}

.favorite-btn.favorited {
  background-color: #ffcccc;
  color: #d32f2f;
}
</style>
```

---

## Key Points

✅ **Default State**: By default, `isFavorited = false`  
✅ **Load on Mount**: Check favorite status when component loads  
✅ **Visual Feedback**: Show unfilled heart 🤍 for not favorited, filled ❤️ for favorited  
✅ **Toggle on Click**: Call toggle endpoint and update state  
✅ **Persist on Refresh**: Check status again on page reload  
✅ **User Check**: Only allow favorites when user is logged in  

---

## Testing

1. Load property page - should show "🤍 Add to Favorites" (not favorited)
2. Click button - should change to "❤️ Favorited"
3. Refresh page - should still show "❤️ Favorited" ✅
4. Click again - should change back to "🤍 Add to Favorites"
5. Refresh page - should show "🤍 Add to Favorites" ✅

---

## Summary

The backend is correct - it returns `false` by default. The frontend needs to:

1. Call `/api/favorites/check` on component mount
2. Display heart based on the returned value
3. Update state when user clicks the button
4. Toggle favorite using `/api/favorites/toggle` endpoint

This ensures:
- ✅ Properties are NOT favorited by default
- ✅ Only favorited when user explicitly clicks
- ✅ Persistence across page refreshes
- ✅ Correct UI state at all times

---

**Backend Status**: ✅ READY  
**Frontend Implementation**: 🔄 NEEDED  
**Data Persistence**: ✅ WORKING
