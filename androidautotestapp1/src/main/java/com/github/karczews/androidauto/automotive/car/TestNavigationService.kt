package com.github.karczews.androidauto.automotive.car

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.car.app.CarAppService
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.model.*
import androidx.car.app.notification.CarAppExtender
import androidx.car.app.validation.HostValidator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.github.karczews.androidauto.automotive.R

private val NOTIFICATION_CHANNEL_HIGH_ID = "channel_01"
private val NOTIFICATION_CHANNEL_HIGH_NAME = "High Channel"

class TestNavigationService : CarAppService() {

    override fun createHostValidator(): HostValidator = HostValidator.ALLOW_ALL_HOSTS_VALIDATOR

    override fun onCreateSession(): Session = object : Session() {
        override fun onCreateScreen(intent: Intent): Screen = TestScreen(carContext)
    }
}

class TestScreen(carContext: CarContext) : Screen(carContext) {
    private val icon = IconCompat.createWithResource(getCarContext(),
        R.drawable.ic_baseline_bug_report_24)

    override fun onGetTemplate(): Template {
        val list = ItemList.Builder()
            .addItem(
                GridItem.Builder()
                    .setImage(CarIcon.Builder(icon).build())
                    .setTitle("Show HUN")
                    .setOnClickListener { showTestHUN() }
                    .build()
            )
            .build()
        return GridTemplate.Builder()
            .setTitle("Notification issue")
            .setSingleList(list)
            .setHeaderAction(Action.APP_ICON)
            .build()
    }

    private fun showTestHUN() {
        val notificationManagerCompat = NotificationManagerCompat.from(carContext)
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_HIGH_ID,
                NOTIFICATION_CHANNEL_HIGH_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(carContext, NOTIFICATION_CHANNEL_HIGH_ID)
            .setCategory(NotificationCompat.CATEGORY_NAVIGATION)
            .setContentTitle("bing bong")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_bug_report_24)
            .setNotificationSilent()
            .setSound(null)
            .extend(
                CarAppExtender.Builder()
                    .setImportance(NotificationManager.IMPORTANCE_HIGH)
                    .build()
            )
            .build()

        notificationManagerCompat.notify(0, notification)

    }
}