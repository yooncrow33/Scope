package scope.internal;

import java.util.function.Supplier;

public final class Lazy<T> {
    private T instance;
    private  final Supplier<T> creator;

    public Lazy(Supplier<T> creator) {
        this.creator = creator;
    }

    public T get() {
        if (instance == null) {
            instance = creator.get();
        }
        return instance;
    }
}
