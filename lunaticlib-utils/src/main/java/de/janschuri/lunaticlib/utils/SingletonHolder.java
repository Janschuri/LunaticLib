package de.janschuri.lunaticlib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class SingletonHolder<T> {
    private final AtomicReference<T> ref = new AtomicReference<>();

    private String ownerFunctionName = "initialize";

    public void initialize(T instance) {
        Objects.requireNonNull(instance, "instance must not be null");
        if (!ref.compareAndSet(null, instance)) {
            throw new IllegalStateException(ownerClassName() + " is already initialized");
        }
    }

    public SingletonHolder<T> ownerFunctionName(String ownerFunctionName) {
        this.ownerFunctionName = ownerFunctionName;
        return this;
    }

    public void shutdown() {
        if (ref.getAndSet(null) == null) {
            throw new IllegalStateException(ownerClassName() + " is not initialized");
        }
    }

    public T get() {
        T v = ref.get();
        if (v == null) {
            String owner = ownerClassName();
            String type = resolveTypeArgumentOrFallback();
            throw new IllegalStateException(
                    owner + " is not initialized. Do " + owner + "."+ ownerFunctionName +"(" + type + " adapter) first"
            );
        }
        return v;
    }

    public boolean isInitialized() {
        return ref.get() != null;
    }

    private static Class<?> callerClass() {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(2).findFirst()
                        .map(StackWalker.StackFrame::getDeclaringClass)
                        .orElseThrow(() -> new IllegalStateException("Cannot determine caller class")));
    }

    private String ownerClassName() {
        return callerClass().getSimpleName();
    }

    private String resolveTypeArgumentOrFallback() {
        Class<?> owner = callerClass();
        try {
            for (Field f : owner.getDeclaredFields()) {
                if (SingletonHolder.class.equals(f.getType())) {
                    // Only static fields can hold our shared instance in your pattern.
                    if ((f.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0) continue;
                    f.setAccessible(true);
                    Object value = f.get(null);
                    if (value == this) {
                        Type g = f.getGenericType();
                        if (g instanceof ParameterizedType p) {
                            Type arg = p.getActualTypeArguments()[0];
                            return arg.getClass().getSimpleName();
                        }
                    }
                }
            }
        } catch (IllegalAccessException ignored) {

        }
        return "T";
    }
}