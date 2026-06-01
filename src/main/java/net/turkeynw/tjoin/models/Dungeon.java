package net.turkeynw.tjoin.models;

import org.bukkit.Location;

public class Dungeon {

    private final String id;
    private String displayName;
    private RequirementType requirementType;


    private double moneyCost;
    private String keyId;

    private Location location;
    private int cooldownSeconds;

    public Dungeon(String id) {
        this.id = id;
        this.requirementType = RequirementType.FREE;
        this.moneyCost = 0.0;
        this.cooldownSeconds = 0;
    }


    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public RequirementType getRequirementType() { return requirementType; }
    public double getMoneyCost() { return moneyCost; }
    public String getKeyId() { return keyId; }
    public Location getLocation() { return location; }
    public int getCooldownSeconds() { return cooldownSeconds; }


    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setRequirementType(RequirementType requirementType) { this.requirementType = requirementType; }
    public void setMoneyCost(double moneyCost) { this.moneyCost = moneyCost; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    public void setLocation(Location location) { this.location = location; }
    public void setCooldownSeconds(int cooldownSeconds) { this.cooldownSeconds = cooldownSeconds; }
}