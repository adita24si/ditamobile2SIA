package com.example.razerstoreapps.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val docName = intent.getStringExtra("doc_name") ?: "Produk Hukum Desa"
        val docId   = intent.getStringExtra("doc_id")   ?: "0"

        val title   = "⏰ Waktunya Membaca Regulasi!"
        val message = "Jangan lupa baca dokumen \"$docName\". " +
                      "Pelajari peraturan desa demi kemajuan bersama!"

        // Gunakan Heads-Up → pop-up dari atas + dering + getar
        NotificationHelper.showHeadsUpNotification(
            context  = context,
            title    = title,
            message  = message,
            notifId  = docId.hashCode()
        )
    }
}
