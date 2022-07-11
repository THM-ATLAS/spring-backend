package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Asset
import com.example.atlasbackend.classes.AtlasUser
import com.example.atlasbackend.classes.ModuleAssetRef
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository: CrudRepository<Asset, Int> {
    @Query("SELECT * FROM module_asset WHERE asset_id = :asset_id")
    fun getReferralsFromAsset(@Param("asset_id") assetID: Int): List<ModuleAssetRef>

    @Query("SELECT \"user\".* FROM submission_file\n" +
            " JOIN user_exercise_submission ON submission_file.submission_id = user_exercise_submission.submission_id\n" +
            " JOIN \"user\" ON \"user\".user_id = user_exercise_submission.user_id\n" +
            " WHERE submission_file.file = :asset_id")
    fun getUserFromSubmissionAsset(@Param("asset_id") assetID: Int): AtlasUser?
}