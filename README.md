# Canva Text Render Service

A Spring Boot application that renders text to PNG images using custom fonts downloaded from HTTP URLs.

## Features

- Download fonts from HTTP URLs
- Render text to PNG images with transparent backgrounds
- Automatic image dimension calculation
- In-memory font caching
- Comprehensive error handling

## Usage

### As a Service

```java
@Autowired
private TextRenderService textRenderService;

// Render text to PNG bytes
byte[] pngBytes = textRenderService.renderText(
    "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf",
    "Hello World"
);
```

### Command Line Usage

```bash
# Build the application
./gradlew build

# Run with arguments: fontUrl text outputPath
java -cp build/libs/canva-0.0.1-SNAPSHOT.jar com.interview.canva.TextRenderMain \
  "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf" \
  "Hello World" \
  "output.png"
```

## API Endpoints

- `GET /hello` - Basic hello world endpoint
- `POST /api/render` - Render text to PNG image

### Render Endpoint

**POST** `/api/render`

**Request Body:**

```json
{
  "fontUrl": "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf",
  "text": "Hello World"
}
```

**Validation:**

- `fontUrl` and `text` are required and cannot be null or empty
- Uses Java Bean Validation with `@NotNull` and `@NotBlank` annotations

**Response:**

- **Success (200)**: PNG image bytes with `image/png` content type
- **Error (400)**: Error message as plain text for validation or service errors
- **Error (500)**: Unexpected error message as plain text

**Example using curl:**

```bash
curl -X POST http://localhost:8080/api/render \
  -H "Content-Type: application/json" \
  -d '{"fontUrl": "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf", "text": "Hello World"}' \
  --output output.png
```

## Running the Application

```bash
./gradlew bootRun
```

The application will start on port 8080.

## Testing

```bash
./gradlew test
```

## Implementation Details

- Uses Java AWT for text rendering
- Built-in HTTP client for font downloading
- PNG output with transparency support
- 16pt black text by default
- No padding around text
- Automatic image sizing based on text content
