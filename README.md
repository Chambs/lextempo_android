# LexTempo Android

Aplicativo Android dedicado ao cálculo rápido de marcos de execução penal (progressão de regime e livramento condicional) de acordo com percentuais simplificados. O projeto foi desenvolvido em Kotlin, utilizando Material Design e ViewBinding para entregar uma interface amigável para advogados e profissionais da área jurídica.

## Visão geral
- **Propósito**: permitir que o usuário informe a pena aplicada, reduções (detração), tipo de crime e situação do apenado para estimar automaticamente os prazos de progressão ao semiaberto, aberto e livramento condicional.
- **Plataforma**: Android nativo, `minSdk 24`, `targetSdk 35`.
- **Tecnologias principais**: Kotlin, AndroidX AppCompat, Material Components, ConstraintLayout, ViewBinding.

## Principais funcionalidades
- Tela inicial (`HomeActivity`) com acesso rápido à calculadora e links externos (WhatsApp institucional e patrocinador).
- Calculadora penal (`MainActivity`) com:
  - Campos numéricos para anos, meses, dias e detração.
  - Seleção assistida do tipo de crime e situação (primário/reincidente) via `AutoCompleteTextView`.
  - Date picker para definir a data inicial do cumprimento de pena.
  - Cálculo automático das frações necessárias para progressão e livramento, exibindo data estimada e dias restantes.
  - Mensagens padrão e feedback em caso de dados incompletos.
- Motor de regras isolado em `CrimeRules.kt`, centralizando percentuais e cenários suportados.

## Estrutura do código
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

- As `Activity*Binding` são geradas a partir dos layouts e utilizadas para acessar views sem `findViewById`.
- Os recursos de texto em `strings.xml` concentram mensagens e rótulos, permitindo futura tradução.
- `arrays.xml` define as listas exibidas nos seletores de crime e situação.

## Regras de cálculo implementadas
O aplicativo opera sobre a pena remanescente (pena total − detração). A quantidade de dias é normalizada usando `1 ano = 365 dias` e `1 mês = 30 dias`. A partir dessa base:
___________________________________________________________________________________
| Crime                        | Situação    | Progressão | Livramento condicional|
|------------------------------|-------------|------------|-----------------------|
| Comum                        | Primário    | 16%        | 1/3                   |
| Comum                        | Reincidente | 20%        | 1/2                   |
| Comum violento               | Primário    | 25%        | 1/3                   |
| Comum violento               | Reincidente | 30%        | 1/2                   |
| Hediondo/equiparado          | Primário    | 40%        | 2/3                   |
| Hediondo/equiparado          | Reincidente | 60%        | 2/3                   |
| Hediondo com resultado morte | Primário    | 50%        | Vedado                |
| Hediondo com resultado morte | Reincidente | 70%        | Vedado                |

- O cálculo das datas estimadas é feito adicionando a quantidade de dias calculada à data de início informada (utilizando `java.time.LocalDate`).
- Quando a data não é informada, o app mostra apenas o número de dias necessários.

## Fluxo do usuário
1. Usuário acessa a tela inicial e abre a calculadora.
2. Informa a pena (anos, meses, dias) e, opcionalmente, a detração.
3. Seleciona o tipo de crime e a situação (primário ou reincidente).
4. (Opcional) Escolhe a data de início do cumprimento da pena.
5. Ao tocar em “Calcular benefícios” o app mostra:
   - Dias e data estimada para progressão ao semiaberto.
   - Dias e data estimada para progressão ao aberto.
   - Dias e data estimada para livramento condicional, quando permitido.

## Configuração do ambiente
- Android Studio Koala (ou mais recente).
- JDK 17 (configurado automaticamente pelo Android Studio).
- Plug-ins Gradle:
  - `com.android.application` 8.6.1
  - `org.jetbrains.kotlin.android` 2.0.20

### Preparando o projeto
1. Clone o repositório e abra em Android Studio.
2. Aguarde a sincronização do Gradle.
3. Conecte (ou inicie) um dispositivo com Android 7.0 (API 24) ou superior.
4. Rode a aplicação via botão **Run** ou através da `Run Configuration` gerada automaticamente.

### Build via linha de comando
```bash
# Linux/macOS
./gradlew assembleDebug

# Windows (PowerShell ou Prompt)
.\gradlew.bat assembleDebug
```
O APK resultante fica em `app/build/outputs/apk/debug/`.

## Testes
- Testes unitários podem ser executados com `./gradlew test`.
- Testes instrumentados (Android) podem ser disparados com `./gradlew connectedAndroidTest` quando houver dispositivos/emuladores disponíveis.
- No momento não há casos de teste implementados; recomenda-se adicionar testes para validar regras de `CrimeRules` e fluxos de cálculo.

## Estilo e componentes de UI
- Layouts baseados em `MaterialCardView`, `TextInputLayout` e `AutoCompleteTextView`, agrupados em `NestedScrollView` para garantir acessibilidade em telas menores.
- `activity_home.xml` utiliza `ConstraintLayout` com gradiente (`bg_home_gradient.xml`) e botões temáticos.
- Paleta definida em `colors.xml` e tema customizado em `styles.xml` / `themes.xml`.
- Ícones personalizados armazenados nas pastas `mipmap-*` e `drawable/`.

## Limitações conhecidas
- Conversão de meses para dias usa fator fixo de 30 dias; considerar ajustes futuros para calendários reais.
- Texto de recursos apresenta caracteres corrompidos caso o arquivo seja aberto com encoding incorreto; idealmente migrar para UTF-8 explícito.
- Regras mostram visão simplificada da legislação; verifique requisitos adicionais (ex.: faltas disciplinares, requisitos subjetivos) antes de utilizar em casos reais.

## Próximos passos sugeridos
1. Implementar testes automatizados das frações e cenários permitidos.
2. Persistir o histórico de cálculos localmente (Room ou DataStore) para consultas posteriores.
3. Internacionalizar recursos e corrigir encoding dos textos para UTF-8.
4. Adicionar validação de entrada mais robusta (ex.: limites máximos de pena, checagem de detração maior que pena total).

## Licença
A licença ainda não foi definida. Adicione um arquivo `LICENSE` caso deseje distribuir o aplicativo publicamente.

