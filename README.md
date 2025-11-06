# LexTempo – Calculadora Penal
## Matheus Santos Feitosa // 20.00628-4

Aplicativo Android que auxilia profissionais do Direito na projeção de marcos da execução penal (progressão de regime e livramento condicional) usando percentuais simplificados da Lei de Execução Penal. O projeto foi desenvolvido em Kotlin com foco em uma experiência direta e responsiva para consultas rápidas em audiências ou atendimentos.

## 1. Descrição Geral
- Automatiza o cálculo dos prazos de progressão do regime fechado → semiaberto → aberto e livramento condicional.
- Permite informar pena aplicada, detração, tipo de crime e situação do apenado para gerar os marcos em dias e datas estimadas.
- Pensado para uso interno do escritório LexTempo, reduzindo tempo gasto em planilhas e cálculos manuais.

## 2. Arquitetura do Sistema
- **Aplicativo Android (Frontend)**: única camada do projeto, desenvolvida em Kotlin com `ViewBinding` e componentes do Material Design.
- **Camada de Regras**: `CrimeRules.kt` concentra enums e percentuais de progressão/livramento, servindo como motor de cálculo reutilizável.

## 3. Estrutura do código
```
app/src/main/java/com/lextempo/calculadorapenal/ui/
├── HomeActivity.kt      # Tela de boas-vindas com atalhos externos
├── MainActivity.kt      # Fluxo principal de cálculo da pena
└── CrimeRules.kt        # Enumerações e percentuais de progressão/livramento

app/src/main/res/
├── layout/              # activity_home.xml, activity_main.xml (layouts principais)
├── values/              # strings.xml, arrays.xml, colors.xml, styles.xml
└── drawable/            # Gradientes, ícones vetoriais e assets do app
```

## 4. Fluxo do usuário
1. Usuário acessa a tela inicial e abre a calculadora.
2. Informa a pena (anos, meses, dias) e, opcionalmente, a detração.
3. Seleciona o tipo de crime e a situação (primário ou reincidente).
4. (Opcional) Escolhe a data de início do cumprimento da pena.
5. Ao tocar em “Calcular benefícios” o app mostra:
   - Dias e data estimada para progressão ao semiaberto.
   - Dias e data estimada para progressão ao aberto.
   - Dias e data estimada para livramento condicional, quando permitido.

## 5. Funcionalidades Principais
- Coleta guiada de dados da pena com `TextInputLayout` e validação básica.
- Seletores automáticos (`AutoCompleteTextView`) para crime e situação.
- Date picker integrado (`DatePickerDialog`) para a data inicial da pena.
- Exibição de resultados formatados com datas ou contagem de dias.
- Atalhos para WhatsApp institucional e link do patrocinador na tela inicial.

## 6. Tecnologias Utilizadas
- **Frontend (App Android)**
  - Kotlin + AndroidX (`AppCompat`, `Core KTX`).
  - Material Components & ConstraintLayout.
  - ViewBinding e java.time (`LocalDate`).
  - Gradle Kotlin DSL.

## 7. Infraestrutura
- Desenvolvimento local com Android Studio Koala (ou superior).
- Emulador Android ou dispositivo físico com Android 7.0 (API 24) ou mais recente.
- Gradle Wrapper incluso no projeto (`./gradlew`).

## 8. Instalação e Execução
**Requisitos**
- Android Studio instalado (com JDK 17 configurado automaticamente).
- Android SDK API 35 (compile/target) e API 24+ para testes.

**Passos**
1. Clone o repositório e abra a pasta `lextempo-android` no Android Studio.
2. Aguarde a sincronização automática do Gradle.
3. Conecte um dispositivo ou inicie um emulador.
4. Execute o app via botão **Run** ou pela linha de comando:
   ```bash
   # Linux / macOS
   ./gradlew assembleDebug

   # Windows
   .\gradlew.bat assembleDebug
   ```
5. O APK gerado estará em `app/build/outputs/apk/debug/`.

## 9. Regras de Cálculo
O cálculo parte da pena remanescente (total em dias − detração). A converter:
- 1 ano = 365 dias.
- 1 mês = 30 dias.

| Crime | Situação | Progressão de regime | Livramento condicional |
|-------|----------|----------------------|------------------------|
| Comum | Primário | 16%                  | 1/3                    |
| Comum | Reincidente | 20%               | 1/2                    |
| Comum com violência/grave ameaça | Primário | 25% | 1/3 |
| Comum com violência/grave ameaça | Reincidente | 30% | 1/2 |
| Hediondo ou equiparado | Primário | 40% | 2/3 |
| Hediondo ou equiparado | Reincidente | 60% | 2/3 |
| Hediondo com resultado morte | Primário | 50% | Vedado |
| Hediondo com resultado morte | Reincidente | 70% | Vedado |

- As datas projetadas são obtidas com `LocalDate.plusDays`.
- Quando a data inicial não é informada, o app apresenta apenas a contagem de dias restantes.

## 10. Equipe e Autoria
- **Desenvolvedor:** Matheus Santos Feitosa
- **Empresa:** Cespedes Lourenço Advocacia
- **Contato:** contato@cespedeslourencoadvocacia.com.br

## 11. Futuras Melhorias (Roadmap)
- Adicionar validação de entrada mais robusta (ex.: limites máximos de pena, checagem de detração maior que pena total) .
- Persistir histórico de cálculos.
- Exportar resultados em PDF e permitir compartilhamento.
- Testes unitários para `CrimeRules` e fluxos da `MainActivity`.

