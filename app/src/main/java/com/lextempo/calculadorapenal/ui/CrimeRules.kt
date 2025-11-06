package com.lextempo.calculadorapenal.ui

/**
 * Regras mínimas de fração com base no manual do projeto.
 * Aqui apenas transformamos a combinação "crime + situação" em frações.
 */
object CrimeRules {

    enum class CrimeType {
        COMUM,
        COMUM_VIOLENTO,
        HEDIONDO,
        HEDIONDO_MORTE
    }

    enum class Situacao {
        PRIMARIO,
        REINCIDENTE
    }

    /**
     * Retorna a fração (0.16 = 16%) a ser cumprida sobre a pena remanescente.
     */
    fun fracaoParaProgressao(crime: CrimeType, situacao: Situacao): Double {
        return when (crime) {
            CrimeType.COMUM -> {
                if (situacao == Situacao.PRIMARIO) 0.16 else 0.20
            }
            CrimeType.COMUM_VIOLENTO -> {
                if (situacao == Situacao.PRIMARIO) 0.25 else 0.30
            }
            CrimeType.HEDIONDO -> {
                if (situacao == Situacao.PRIMARIO) 0.40 else 0.60
            }
            CrimeType.HEDIONDO_MORTE -> {
                if (situacao == Situacao.PRIMARIO) 0.50 else 0.70
            }
        }
    }

    /**
     * Livramento condicional (modelo simplificado, conforme manual):
     * - Comum primário: 1/3
     * - Comum reincidente: 1/2
     * - Hediondo: 2/3
     * - Hediondo com resultado morte: vedado (retorna null)
     */
    fun fracaoParaLivramento(crime: CrimeType, situacao: Situacao): Double? {
        return when (crime) {
            CrimeType.COMUM, CrimeType.COMUM_VIOLENTO -> {
                if (situacao == Situacao.PRIMARIO) (1.0 / 3.0) else 0.5
            }
            CrimeType.HEDIONDO -> {
                2.0 / 3.0
            }
            CrimeType.HEDIONDO_MORTE -> {
                null
            }
        }
    }
}
