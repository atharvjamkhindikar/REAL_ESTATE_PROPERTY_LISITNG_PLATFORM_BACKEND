# Image Upload - Quick Reference

## 🚀 New Endpoint

```
POST /api/properties/{propertyId}/images/upload
```

## 📝 Frontend Code

### React + Axios
```javascript
const uploadImage = async (propertyId, file) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('caption', 'My image');

  const response = await axios.post(
    `http://localhost:8080/api/properties/${propertyId}/images/upload`,
    formData,
    { headers: { 'Content-Type': 'multipart/form-data' } }
  );
  
  return response.data.data;
};
```

### React + Fetch
```javascript
const uploadImage = async (propertyId, file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await fetch(
    `http://localhost:8080/api/properties/${propertyId}/images/upload`,
    { method: 'POST', body: formData }
  );
  
  return response.json();
};
```

## ✅ What Works Now

✅ Direct file uploads (multipart/form-data)
✅ File validation (size, type)
✅ Instant response with image data
✅ **No more "Add it later"!**

## 📦 Response

```json
{
  "success": true,
  "message": "Image uploaded and added successfully",
  "data": {
    "id": 1,
    "imageUrl": "uploads/images/xxx.jpg",
    "caption": "My image",
    "isPrimary": true,
    "displayOrder": 0,
    "uploadedAt": "2026-01-28T12:00:00"
  }
}
```

## 🎯 Do This

❌ Remove: "Add Images Later" button  
✅ Add: Direct file upload with FormData  
✅ Test: Upload actual image files  

That's it! The backend handles everything now.
