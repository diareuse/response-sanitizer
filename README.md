# response-sanitizer
Sanitizes Retrofit response from null(s) forcing deserializer using Kotlin's default value.

### What does it do?

It's pretty simple actually :), it transforms this:

```json
{ "name": "John Doe", "picture": null, "phone_number": null }
```

to this:

```json
{ "name": "John Doe" }
```

### Why?

Because Kotlin needs missing properties in order to create objects with default values. Hence this will be a welcomed addition to you project I reckon :)

### Pitfalls?

Takes a bit of time until it reiterates through all of the entries in your json.

## Usage:

Preferably as OkHttp interceptor:

```kotlin
OkHttpClient.Builder()
  .addInterceptor(...)
  .addInterceptor(ResponseSanitizer())
  .addInterceptor(...)
  .build()
```

or standalone:

```kotlin
val result = Sanitizer { someString.removeNulls() }
```

In your *root* `build.gradle`

```groovy
allprojects {
  repositories {
    //...
    maven { url 'https://jitpack.io' }
  }
}
```

Latest version: [![](https://jitpack.io/v/diareuse/response-sanitizer.svg)](https://jitpack.io/#diareuse/response-sanitizer)

In your module `build.gradle`:

```groovy
dependencies {
  implementation "com.github.diareuse:response-sanitizer:latest.version"
  //...or
  implementation group: "com.github.diareuse", name: "response-sanitizer", version: "latest.version"
}
```

OR in your module `build.gradle.kts`:

```kotlin
dependencies {
  implementation("com.github.diareuse:response-sanitizer:latest.version")
  //...or
  implementation("com.github.diareuse", "response-sanitizer", "latest.version")
}
```
