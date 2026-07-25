package com.example.khabarkhabar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var pairId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val code = findViewById<EditText>(R.id.pairCodeInput)

        findViewById<Button>(R.id.connectButton).setOnClickListener {
            pairId = code.text.toString().trim()
            Toast.makeText(this,
                if (pairId.length == 6) "کد اتصال ثبت شد 🔗" else "کد ۶ رقمی وارد کن",
                Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.sleepButton).setOnClickListener { updateStatus("asleep") }
        findViewById<Button>(R.id.awakeButton).setOnClickListener { updateStatus("awake") }
    }

    private fun updateStatus(status: String) {
        if (pairId.isEmpty()) {
            Toast.makeText(this, "اول کد اتصال را وارد کن", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            db.collection("pairs").document(pairId).set(
                mapOf(
                    "status" to status,
                    "partnerFcmToken" to token,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "وضعیت ثبت شد ✅", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "خطا در اتصال به Firebase", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
