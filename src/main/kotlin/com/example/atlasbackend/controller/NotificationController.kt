package com.example.atlasbackend.controller

import com.example.atlasbackend.service.NotificationService
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(var notificationService: NotificationService) {
}