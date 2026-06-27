package com.example.razerstoreapps.data.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.razerstoreapps.BaseActivity
import com.example.razerstoreapps.R

object NotificationHelper {

    // Channel normal (info biasa)
    const val CHANNEL_ID = "produk_hukum_info"
    const val CHANNEL_NAME = "Info Produk Hukum"
    const val CHANNEL_DESC = "Notifikasi informasi tambah/edit data produk hukum"

    // Channel heads-up (pop-up dari atas, dering + getar)
    const val CHANNEL_HEADSUP_ID = "produk_hukum_headsup"
    const val CHANNEL_HEADSUP_NAME = "Pengingat Penting Produk Hukum"
    const val CHANNEL_HEADSUP_DESC = "Pop-up pengingat regulasi dan sosialisasi desa"

    // ─────────────────────────────────────────────────────────────
    //  Buat KEDUA channel sekaligus (dipanggil dari Application/Activity)
    // ─────────────────────────────────────────────────────────────
    fun createAllChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Channel INFO — importance default, tanpa pop-up
            val infoChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }

            // 2. Channel HEADS-UP — importance HIGH supaya muncul pop-up dari atas
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttr = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val headsUpChannel = NotificationChannel(
                CHANNEL_HEADSUP_ID, CHANNEL_HEADSUP_NAME,
                NotificationManager.IMPORTANCE_HIGH          // ← kunci utama heads-up
            ).apply {
                description = CHANNEL_HEADSUP_DESC
                setSound(ringtoneUri, audioAttr)             // dering sistem
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 150, 300)  // pola getar
                enableLights(true)
                lightColor = 0xFF7C3AED.toInt()              // lampu notif ungu
            }

            nm.createNotificationChannel(infoChannel)
            nm.createNotificationChannel(headsUpChannel)
        }
    }

    // Panggil createAllChannels agar backward-compatible
    fun createNotificationChannel(context: Context) = createAllChannels(context)

    // ─────────────────────────────────────────────────────────────
    //  showNotification — notifikasi biasa (bukan pop-up)
    //  Dipakai saat: tambah regulasi baru, tambah sosialisasi baru
    // ─────────────────────────────────────────────────────────────
    fun showNotification(context: Context, title: String, message: String) {
        createAllChannels(context)
        val pendingIntent = buildOpenAppIntent(context, System.currentTimeMillis().toInt())

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_law)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  showHeadsUpNotification — pop-up dari atas + dering + getar
    //  Dipakai saat: alarm pengingat berbunyi
    // ─────────────────────────────────────────────────────────────
    fun showHeadsUpNotification(context: Context, title: String, message: String, notifId: Int = System.currentTimeMillis().toInt()) {
        createAllChannels(context)

        val pendingIntent = buildOpenAppIntent(context, notifId)

        // Vibrate langsung saat broadcast diterima
        triggerVibration(context)

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_HEADSUP_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)      // ← pop-up muncul
            .setCategory(NotificationCompat.CATEGORY_ALARM)     // ← kategori alarm
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // ← tampil di lock screen
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setSound(ringtoneUri)                              // ← dering
            .setVibrate(longArrayOf(0, 300, 150, 300))          // ← getar
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notifId, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  scheduleReminder — jadwal alarm via AlarmManager
    // ─────────────────────────────────────────────────────────────
    fun scheduleReminder(context: Context, docId: String, docName: String, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("doc_id", docId)
            putExtra("doc_name", docName)
        }

        val requestCode = docId.hashCode()
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flag)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────
    private fun buildOpenAppIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, BaseActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(context, requestCode, intent, flag)
    }

    @Suppress("DEPRECATION")
    private fun triggerVibration(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vm.defaultVibrator.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 300, 150, 300), -1)
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                v.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 300, 150, 300), -1))
            } else {
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                v.vibrate(longArrayOf(0, 300, 150, 300), -1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
