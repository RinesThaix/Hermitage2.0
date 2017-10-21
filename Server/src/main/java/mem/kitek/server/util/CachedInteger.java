package mem.kitek.server.util;

import java.util.function.Supplier;

/**
 * Created by RINES on 21.10.17.
 */
public class CachedInteger {

    private final long refreshDelay;
    private final Supplier<Integer> generator;

    public CachedInteger(long refreshDelay, Supplier<Integer> generator) {
        this.refreshDelay = refreshDelay;
        this.generator = generator;
    }

    private volatile int value;
    private volatile long lastChange;

    public int get() {
        long current = System.currentTimeMillis();
        if(current - this.lastChange < this.refreshDelay)
            return this.value;
        this.lastChange = current;
        return this.value = this.generator.get();
    }

}
