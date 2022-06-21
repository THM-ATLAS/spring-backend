package com.example.atlasbackend.repository

import com.example.atlasbackend.classes.Asset
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository: CrudRepository<Asset, Int>