package com.lextempo.calculadorapenal.ui

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lextempo.calculadorapenal.R
import com.lextempo.calculadorapenal.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private var dataInicio: LocalDate? = null

    private lateinit var crimeOpcoes: Array<String>
    private lateinit var situacaoOpcoes: Array<String>
    private var crimeSelecionado = 0
    private var situacaoSelecionada = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarDropdowns()
        configurarData()
        configurarBotao()
    }

    private fun configurarDropdowns() {
        crimeOpcoes = resources.getStringArray(R.array.crime_options)
        situacaoOpcoes = resources.getStringArray(R.array.situacao_options)

        val crimeAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, crimeOpcoes)
        binding.spCrime.setAdapter(crimeAdapter)
        binding.spCrime.setText(crimeOpcoes.getOrElse(crimeSelecionado) { "" }, false)
        binding.spCrime.setOnItemClickListener { _, _, position, _ ->
            crimeSelecionado = position
        }
        binding.spCrime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.spCrime.showDropDown()
        }
        binding.spCrime.setOnClickListener {
            binding.spCrime.showDropDown()
        }

        val situacaoAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, situacaoOpcoes)
        binding.spSituacao.setAdapter(situacaoAdapter)
        binding.spSituacao.setText(situacaoOpcoes.getOrElse(situacaoSelecionada) { "" }, false)
        binding.spSituacao.setOnItemClickListener { _, _, position, _ ->
            situacaoSelecionada = position
        }
        binding.spSituacao.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.spSituacao.showDropDown()
        }
        binding.spSituacao.setOnClickListener {
            binding.spSituacao.showDropDown()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarData() {
        binding.etDataInicio.setOnClickListener {
            val hoje = LocalDate.now()
            val dlg = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    dataInicio = LocalDate.of(year, month + 1, dayOfMonth)
                    binding.etDataInicio.setText(dataInicio!!.format(formatter))
                },
                hoje.year,
                hoje.monthValue - 1,
                hoje.dayOfMonth
            )
            dlg.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurarBotao() {
        binding.btnCalcular.setOnClickListener {
            val anos = binding.etAnos.text.toString().toIntOrNull() ?: 0
            val meses = binding.etMeses.text.toString().toIntOrNull() ?: 0
            val dias = binding.etDias.text.toString().toIntOrNull() ?: 0

            if (anos == 0 && meses == 0 && dias == 0) {
                Toast.makeText(this, getString(R.string.toast_missing_sentence), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val totalDias = anos * 365 + meses * 30 + dias
            val detracao = binding.etDetracao.text.toString().toIntOrNull() ?: 0
            val remanescente = max(0, totalDias - detracao)

            val crime = when (crimeSelecionado) {
                0 -> CrimeRules.CrimeType.COMUM
                1 -> CrimeRules.CrimeType.COMUM_VIOLENTO
                2 -> CrimeRules.CrimeType.HEDIONDO
                3 -> CrimeRules.CrimeType.HEDIONDO_MORTE
                else -> CrimeRules.CrimeType.COMUM
            }

            val situacao = when (situacaoSelecionada) {
                0 -> CrimeRules.Situacao.PRIMARIO
                else -> CrimeRules.Situacao.REINCIDENTE
            }

            val fracaoProg = CrimeRules.fracaoParaProgressao(crime, situacao)
            val diasParaProgressao = ceil(remanescente * fracaoProg).toInt()

            val fracaoLc = CrimeRules.fracaoParaLivramento(crime, situacao)
            val diasParaLc = fracaoLc?.let { ceil(remanescente * it).toInt() }

            val diasParaAberto = diasParaProgressao + ceil(remanescente * 0.16).toInt()

            if (dataInicio == null) {
                binding.tvProgressao.text =
                    "Progressão ao semiaberto: faltam $diasParaProgressao dias (selecione a data para ver a data exata)"
                binding.tvAberto.text =
                    "Progressão ao aberto: faltam $diasParaAberto dias"
                binding.tvLivramento.text =
                    diasParaLc?.let { "Livramento condicional: faltam $it dias" }
                        ?: getString(R.string.result_livramento_vedado)
            } else {
                val inicio = dataInicio!!
                val dataProg = inicio.plusDays(diasParaProgressao.toLong())
                val dataAberto = inicio.plusDays(diasParaAberto.toLong())
                val dataLc = diasParaLc?.let { inicio.plusDays(it.toLong()) }

                binding.tvProgressao.text =
                    "Progressão ao semiaberto: ${dataProg.format(formatter)} (${diasParaProgressao}d)"
                binding.tvAberto.text =
                    "Progressão ao aberto: ${dataAberto.format(formatter)} (${diasParaAberto}d)"
                binding.tvLivramento.text =
                    dataLc?.let { "Livramento condicional: ${it.format(formatter)} (${diasParaLc}d)" }
                        ?: getString(R.string.result_livramento_vedado)
            }
        }
    }
}
