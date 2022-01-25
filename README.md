# Об осторожности использования сопрограмм (kotlin coroutine)
Здесь речь пойдёт о том, что если Вы где-то влепите наблюдатель с
```kotlin
GlobalScope.launch {
    TestListener.value.collect { value ->
        // Что-то там делаем
    }
}
```
А `TestListener` это объект, с жизненным циклом, равным жизненному циклу приложения
```kotlin
object TestListener {
    private val sharedValue = MutableStateFlow<Int>(0)
    val value get() = sharedValue.asStateFlow()
    
    fun changeValue(newValue: Int) {
        sharedValue.tryEmit(newValue)
    }
}
```

То ваш объект просто не уничтожится до конца жизненного цикла приложения. Соответственно, если объект с наблюдателем пересоздаётся каждый раз при повороте экрана, уходе приложения в фоновый режим и т.д., начнётся гонка создания объектов, содержащих наблюдатели.

Как только у вас появляется кто-то с глобальным временем жизни, используйте то, что содержит в себе необходимый жизненный цикл! Например, 

```kotlin
lifecycle.coroutineScope.launch {
    TestListener.value.collect { value ->
        // Что-то там делаем
    }
}
```