package init;

import java.util.concurrent.ExecutorService;

/**
 * Created by 10441 on 2016/10/9.
 */
public class Context {

    private ExecutorService cachedThreadPool;


    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }

    public void setCachedThreadPool(ExecutorService cachedThreadPool) {
        this.cachedThreadPool = cachedThreadPool;
    }



}
