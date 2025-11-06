package com.lextempo.calculadorapenal.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.lextempo.calculadorapenal.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAbrirCalculadora.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnWhatsapp.setOnClickListener {
            abrirWhatsapp()
        }

        binding.tvSponsored.setOnClickListener {
            abrirLinkPatrocinador()
        }
    }

    private fun abrirWhatsapp() {
        val uri = Uri.parse("https://wa.me/5511930083998")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Snackbar.make(
                binding.root,
                "Não foi possível abrir o WhatsApp. Verifique se o app está instalado.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun abrirLinkPatrocinador() {
        val uri = Uri.parse("https://cespedeslourencoadvogados.com.br/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Snackbar.make(
                binding.root,
                "Não foi possível abrir o site agora.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}
