/*
 * This file is part of the atomic client distribution.
 * Copyright (c) 2021-2021 0x150.
 */

package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.notifications.Notification;
import me.zeroX150.atomic.feature.gui.notifications.NotificationRenderer;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Objects;

public class GodBridge extends Module {

    final BooleanValue courseCorrect = (BooleanValue) this.config.create("Course correct", true).description("Prevent you from falling off the track by accident");
    final float        mOffset       = 0.20f;
    final Direction[]  allowedSides  = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    Notification isReady = null;

    public GodBridge() {
        super("God Bridge", "YOOOO MF HAS SKILL!!!!", ModuleType.WORLD);
    }

    boolean isReady() {
        return Objects.requireNonNull(Atomic.client.player).getPitch() > 82;
    }

    @Override public void tick() {
        // Notification.create(5000, "GodBridge", "Look down, as you would normally while godbridging to start");
        if (!isReady()) {
            if (isReady == null) {
                isReady = Notification.create(-1, "GodBridge", true, "Look down, as you would normally while godbridging to start");
            }
        } else {
            if (isReady != null) {
                isReady.duration = 0;
            }
        }
        if (!NotificationRenderer.topBarNotifications.contains(isReady)) {
            isReady = null;
        }
    }

    @Override public void enable() {
    }

    @Override public void disable() {
        if (isReady != null) {
            isReady.duration = 0;
        }
    }

    @Override public String getContext() {
        return isReady() ? "Ready" : "Not ready";
    }

    @Override public void onWorldRender(MatrixStack matrices) {

    }

    @Override public void onHudRender() {

    }

    @Override public void onFastTick() {
        if (!isReady()) {
            return;
        }
        Objects.requireNonNull(Atomic.client.player).setYaw(Atomic.client.player.getMovementDirection().asRotation());
        if (Atomic.client.player.getPitch() > 83) {
            Atomic.client.player.setPitch(82.5f);
        }
        HitResult hr = Atomic.client.crosshairTarget;
        if (Objects.requireNonNull(hr).getType() == HitResult.Type.BLOCK && hr instanceof BlockHitResult result) {
            if (Arrays.stream(allowedSides).anyMatch(direction -> direction == result.getSide())) {
                Atomic.client.player.swingHand(Hand.MAIN_HAND);
                Objects.requireNonNull(Atomic.client.interactionManager).interactBlock(Atomic.client.player, Atomic.client.world, Hand.MAIN_HAND, result);
            }
        }
        if (!courseCorrect.getValue()) {
            return;
        }
        Vec3d ppos = Atomic.client.player.getPos();
        Vec3d isolated = new Vec3d(ppos.x - Math.floor(ppos.x), 0, ppos.z - Math.floor(ppos.z));
        double toCheck = 0;
        switch (Atomic.client.player.getMovementDirection()) {
            case NORTH, SOUTH -> toCheck = isolated.x;
            case EAST, WEST -> toCheck = isolated.z;
        }
        Atomic.client.options.keySneak.setPressed(toCheck > 0.5 + mOffset || toCheck < 0.5 - mOffset);
    }

}

