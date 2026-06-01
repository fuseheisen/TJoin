package net.turkeynw.tjoin.database;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StorageProvider {

    void connect();


    void disconnect();


    CompletableFuture<Long> getCooldown(UUID uuid, String dungeonId);


    CompletableFuture<Void> setCooldown(UUID uuid, String dungeonId, long expireTime);
}