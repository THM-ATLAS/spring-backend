package com.example.atlasbackend.service

import com.example.atlasbackend.repository.NotificationRepository
import org.springframework.stereotype.Service

@Service
public class NotificationService(var notRepository: NotificationRepository) {
}
