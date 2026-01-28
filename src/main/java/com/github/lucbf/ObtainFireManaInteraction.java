package com.github.lucbf;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;

import com.hypixel.hytale.protocol.*;

import com.hypixel.hytale.server.core.entity.InteractionContext;

import com.hypixel.hytale.server.core.entity.entities.Player;

import com.hypixel.hytale.server.core.modules.interaction.InteractionModule;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.ConditionInteraction;

import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.util.InventoryHelper;
import com.github.lucbf.ui.ManaUi;

import javax.annotation.Nonnull;
import java.sql.SQLException;

class ObtainFireManaInteraction extends ConditionInteraction {
    public static final BuilderCodec<ObtainFireManaInteraction> CODEC = BuilderCodec.builder(
                    ObtainFireManaInteraction.class, ObtainFireManaInteraction::new, SimpleInteraction.CODEC
            )
            .build();

    ObtainFireManaInteraction() {

    }

    @Override
    protected void tick0(boolean firstRun, float time, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> player_ref = context.getEntity();
        Player player = player_ref.getStore().getComponent(player_ref, Player.getComponentType());
        InventoryHelper.clearItemInHand(player.getInventory(), (byte) -1);
    }
}