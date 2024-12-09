package com.pakenanya.mindsync.data.local.room

import androidx.room.*
import com.pakenanya.mindsync.data.remote.response.OrganizationData

@Dao
interface OrganizationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganization(organization: OrganizationData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizations(organizations: List<OrganizationData>)

    @Query("SELECT * FROM organizations WHERE id = :organizationId LIMIT 1")
    suspend fun getOrganizationById(organizationId: Int): OrganizationData?

    @Query("SELECT * FROM organizations")
    suspend fun getAllOrganizations(): List<OrganizationData>

    @Update
    suspend fun updateOrganization(organization: OrganizationData)

    @Query("DELETE FROM organizations WHERE id = :organizationId")
    suspend fun deleteOrganizationById(organizationId: Int)

    @Query("DELETE FROM organizations")
    suspend fun clearAllOrganizations()
}