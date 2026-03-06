# On-Device LLM Trip Q&A — Feature Plan

## Vision

Allow users to ask natural language questions about their past trips and get intelligent answers powered by an on-device language model. No cloud dependency, no data leaves the device.

## Example Questions & Answers

| User Question | Expected Answer |
|---|---|
| "What was my longest trip this month?" | "Your longest trip was 'Morning Commute' on Feb 15 — 12.3 km in 28 minutes." |
| "How much time did I spend walking last week?" | "You spent 2 hours 15 minutes walking across 4 trips last week." |
| "Where did I stop the most on my road trip?" | "You had 5 stops on 'Road Trip to Austin'. The longest stop was 45 minutes near Waco, TX." |
| "What was my average speed while cycling?" | "Your average cycling speed across all trips is 18.2 km/h." |
| "Compare my commute today vs yesterday" | "Today's commute was 2 minutes faster (23 vs 25 min) but 0.3 km longer due to a detour near Main St." |
| "When do I usually start my morning runs?" | "Based on 12 running trips, you typically start between 6:30-7:00 AM." |
| "Did I go faster on my bike yesterday or last Tuesday?" | "Yesterday: avg 21.3 km/h, max 35.1 km/h. Last Tuesday: avg 19.8 km/h, max 32.4 km/h. Yesterday was faster." |
| "Summarize my trip to the park" | "You walked 2.1 km in 25 minutes to Central Park, spent 40 minutes there, then walked back. Total distance: 4.3 km." |

## Technical Approach

### Option A: Gemini Nano (Recommended for Android)

- **What**: Google's on-device LLM, available via Android AICore on Pixel 8+ and Samsung S24+
- **Pros**: Native Android integration, no model download needed, hardware-optimized, Google-maintained
- **Cons**: Limited device availability (Pixel 8+, Samsung S24+), limited context window
- **API**: `com.google.android.gms:play-services-ai-generativeai`
- **How**: Feed structured trip data as context, ask the model to answer questions

### Option B: MediaPipe LLM Inference API

- **What**: Google's cross-platform ML framework with LLM support
- **Pros**: Works on more devices, supports multiple models (Gemma 2B, Phi-2)
- **Cons**: Requires model download (~1-2 GB), more setup
- **API**: `com.google.mediapipe:tasks-genai`
- **How**: Download a small LLM (Gemma 2B), run inference locally

### Option C: ONNX Runtime + Phi-3 Mini

- **What**: Microsoft's Phi-3 Mini (3.8B params) running via ONNX Runtime Mobile
- **Pros**: Good reasoning capability, works offline after download
- **Cons**: Large model size (~2 GB), slower inference on older devices
- **API**: `com.microsoft.onnxruntime:onnxruntime-android`

### Option D: llama.cpp / MLC-LLM

- **What**: Community ports of Llama/Gemma/Phi models for mobile
- **Pros**: Maximum flexibility, many model choices
- **Cons**: Complex integration, battery-intensive, variable quality

### Recommendation

Start with **Option A (Gemini Nano)** for supported devices, with **Option B (MediaPipe + Gemma 2B)** as fallback. This covers most modern Android phones.

## Architecture

### Data Pipeline

```
User asks question
  → TripQueryEngine receives question string
  → ContextBuilder gathers relevant trip data from Room
    → Recent trips (last 30 days)
    → Trip matching keywords in the question
    → Aggregate stats
  → ContextBuilder formats data as structured text prompt
  → LLM processes prompt + question
  → Response parsed and displayed to user
```

### Context Building Strategy

The LLM's context window is limited (~4K-8K tokens). We can't dump all trip data. Instead:

1. **Query Analysis**: Simple keyword extraction to identify what data is relevant
   - Time references: "today", "yesterday", "last week", "this month"
   - Trip references: trip name, transport mode, location names
   - Metric references: "speed", "distance", "time", "elevation"
   
2. **Data Retrieval**: Fetch only relevant data from Room
   - `TripRepository` queries filtered by time range, mode, name
   - Aggregate calculations done in SQL, not by the LLM
   
3. **Context Formatting**: Structured prompt template
   ```
   You are a trip analysis assistant for a GPS tracking app called Traq.
   Answer the user's question based on the following trip data.
   Be concise and specific. Use metric units.
   
   ## Trip Data
   [Structured trip summaries]
   
   ## User Question
   [The actual question]
   ```

### Module Structure

```
:core:ai
  └── llm/
      ├── TripQueryEngine.kt      — Orchestrates Q&A flow
      ├── ContextBuilder.kt        — Builds LLM prompts from trip data
      ├── QueryAnalyzer.kt         — Extracts time/topic from question
      ├── LlmProvider.kt           — Interface for LLM backends
      ├── GeminiNanoProvider.kt    — Gemini Nano implementation
      └── MediaPipeLlmProvider.kt  — MediaPipe fallback

:feature:trip-qa (new module)
  ├── ui/
  │   └── TripQAScreen.kt         — Chat-like Q&A interface
  ├── viewmodel/
  │   └── TripQAViewModel.kt
  └── model/
      └── QAUiState.kt
```

### UI Design

**Entry point**: Button on dashboard or trip detail ("Ask about this trip" / "Ask Traq")

**Q&A Screen**:
- Chat-like interface with user bubbles and AI response bubbles
- Text input at the bottom with send button
- Suggested questions as chips above the input
- Loading indicator while model processes
- Responses can include formatted metrics, comparisons, mini-charts

**Suggested Questions** (context-aware):
- On dashboard: "What's my total distance this week?", "Compare my last two trips"
- On trip detail: "Summarize this trip", "What was my fastest segment?", "Where did I stop?"

## Privacy & Performance

- **All processing on-device** — no network calls, no data leaves the phone
- **Response time target**: < 3 seconds for simple queries
- **Model size**: Gemini Nano = 0 (preinstalled), Gemma 2B = ~1.5 GB download
- **Battery**: Inference is GPU/NPU-accelerated on modern chips
- **Graceful degradation**: If no LLM is available, show pre-computed stats instead

## Implementation Phases

### Phase 1: Foundation
- Create TripQueryEngine with ContextBuilder
- Implement QueryAnalyzer for time/topic extraction
- Create LlmProvider interface
- Build basic Q&A screen with text input

### Phase 2: Gemini Nano Integration
- Implement GeminiNanoProvider
- Device capability detection
- Prompt engineering and testing
- Response formatting

### Phase 3: MediaPipe Fallback
- Implement MediaPipeLlmProvider with Gemma 2B
- Model download manager with progress UI
- Storage management (delete model option in settings)

### Phase 4: Smart Context & UX Polish
- Improve QueryAnalyzer with better keyword extraction
- Add suggested questions (context-aware)
- Add conversation history (multi-turn)
- Mini-charts in responses
- "Ask about this trip" quick action on trip detail

### Phase 5: Advanced Queries
- Cross-trip comparisons
- Pattern detection ("you usually...")
- Route analysis ("your fastest segment was...")
- Integration with Trip Notes

## Open Questions

- Minimum Android version / device requirements for Gemini Nano?
- Should we support multi-turn conversations or single Q&A?
- How to handle questions the model can't answer from available data?
- Should auto-generated insights be shown proactively (without asking)?
- Model download: Wi-Fi only or allow cellular?
- Privacy disclosure needed even though data stays on-device?
- Should we cache/save interesting Q&A exchanges?

## Risks

- **Device fragmentation**: Gemini Nano only on recent Pixel/Samsung devices
- **Model quality**: Small on-device models may give incorrect answers
- **Context window**: Complex queries about many trips may exceed token limits
- **User expectations**: Users may expect ChatGPT-level quality from a 2B model
- **Battery drain**: Repeated LLM inference could impact battery life

## Success Criteria

- [ ] User can ask natural language questions about their trips
- [ ] Answers are accurate for simple factual queries (distance, speed, time)
- [ ] Response time < 3 seconds on supported devices
- [ ] Works fully offline
- [ ] Graceful fallback when LLM is not available
- [ ] No trip data leaves the device
