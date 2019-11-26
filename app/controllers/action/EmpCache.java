package controllers.action;

import play.cache.SyncCacheApi;

import javax.inject.Inject;
import java.util.Optional;

public class EmpCache {
    private SyncCacheApi syncCacheApi;

    @Inject
    public EmpCache(SyncCacheApi syncCacheApi) {
        this.syncCacheApi = syncCacheApi;
    }

    public Optional<String> getSyncCacheApi() {
        return this.syncCacheApi.getOptional("email");
    }

    public void setSyncCacheApi(String cacheValue) {
        this.syncCacheApi.set("email", cacheValue, 300000);
    }

    public void removeCache() {
        this.syncCacheApi.remove("email");
    }
}
