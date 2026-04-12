# AI Job Assistant

An Android app that uses local LLM (Ollama) to provide AI-powered job suggestions.

## Features

- **Job Match**: Enter your skills and experience, get personalized job title suggestions
- **Job Description Generator**: Create professional job descriptions from requirements
- **Career Path Planner**: Get a detailed career progression plan based on your current role and goals
- **History**: View and manage all your generated suggestions
- **Settings**: Configure Ollama URL, model, temperature, and max tokens

## Requirements

1. **Ollama** running on your computer
2. A pulled model (e.g., `llama3.2`, `mistral`, `phi3`)
3. Android Studio for building the app

## Setup

### 1. Install Ollama

Download from [ollama.ai](https://ollama.ai) and install on your computer.

### 2. Pull a Model

```bash
ollama pull llama3.2
```

### 3. Configure Network Access

For Android emulator, Ollama needs to accept network connections. The default URL `http://10.0.2.2:11434` routes to your computer's localhost from the emulator.

### 4. Build the App

```bash
cd aivoice-jobassistant
./gradlew assembleDebug
```

### 5. Install on Device/Emulator

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Usage

1. Open the app
2. Go to Settings and verify the Ollama URL
3. Select your preferred model from the dropdown
4. Adjust temperature (creativity) and max tokens as needed
5. Save settings
6. Navigate to Job Match, Description, or Career Path
7. Enter your information and tap Generate

## Project Structure

```
app/src/main/java/io/lazar/jobassistant/
├── data/
│   ├── api/          # Ollama API client
│   ├── model/        # Data classes
│   └── repository/   # Data repositories
├── ui/
│   ├── screens/      # Compose screens & ViewModels
│   └── theme/        # App theme
├── util/             # Utility classes (prompt templates)
└── MainActivity.kt
```

## Architecture

- **MVVM** pattern with Jetpack Compose
- **Repository** pattern for data access
- **DataStore** for preferences persistence
- **Retrofit** for HTTP requests
- **Kotlinx Serialization** for JSON parsing

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel
- DataStore
- Retrofit
- OkHttp

## License

MIT
