package me.zeroX150.atomic.mixin.game.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("jumpingCooldown")
    int getJumpingCooldown();

    @Accessor("jumpingCooldown")
    void setJumpingCooldown(int v);
}
