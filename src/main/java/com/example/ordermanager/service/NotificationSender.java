package com.example.ordermanager.service;

import com.example.ordermanager.entity.Order;

public interface NotificationSender {
    void sendNotification(Order order);
}
