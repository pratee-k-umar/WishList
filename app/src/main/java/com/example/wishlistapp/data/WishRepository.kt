package com.example.wishlistapp.data

import kotlinx.coroutines.flow.Flow

class WishRepository(private val wishDao: WishDao) {
    suspend fun addWish(wish: Wish) {
        wishDao.addWish(wish)
    }

    fun getWishes(): Flow<List<Wish>> = wishDao.getWishes()

    fun getWishById(id: Long): Flow<Wish> {
        return wishDao.getWishById(id)
    }

    suspend fun updateWish(wish: Wish) {
        wishDao.updateWish(wish)
    }

    suspend fun deleteWish(wish: Wish) {
        return wishDao.deleteWish(wish)
    }
}