package com.pakenanya.mindsync.data.local.room

import androidx.room.*
import com.pakenanya.mindsync.data.remote.response.MembershipData

@Dao
interface MembershipsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembership(membership: MembershipData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberships(memberships: List<MembershipData>)

    @Query("SELECT * FROM memberships WHERE userId = :userId")
    suspend fun getMembershipsByUserId(userId: Int): List<MembershipData>

    @Query("SELECT * FROM memberships WHERE id = :membershipId LIMIT 1")
    suspend fun getMembershipById(membershipId: Int): MembershipData?

    @Update
    suspend fun updateMembership(membership: MembershipData)

    @Query("DELETE FROM memberships WHERE id = :membershipId")
    suspend fun deleteMembershipById(membershipId: Int)

    @Query("DELETE FROM memberships")
    suspend fun clearAllMemberships()
}