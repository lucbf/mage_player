package com.github.lucbf;

import com.github.lucbf.ui.ManaUi;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.none.ConditionInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.util.InventoryHelper;

import javax.annotation.Nonnull;
import java.sql.SQLException;

class ObtainIceManaInteraction extends ConditionInteraction {
    public static final BuilderCodec<ObtainIceManaInteraction> CODEC = BuilderCodec.builder(
                    ObtainIceManaInteraction.class, ObtainIceManaInteraction::new, SimpleInteraction.CODEC
            )
            .build();

    ObtainIceManaInteraction() {

    }

    @Override
    protected void tick0(boolean firstRun, float time, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nonnull CooldownHandler cooldownHandler) {
        Ref<EntityStore> player_ref = context.getEntity();
        Player player = player_ref.getStore().getComponent(player_ref, Player.getComponentType());
        InventoryHelper.clearItemInHand(player.getInventory(), (byte)-1);
        try {
            DBManager.DB_MANAGER.alterStatusPlayer(player.getPlayerRef().getUuid().hashCode(), ManaType.ICE);
            player.getHudManager().setCustomHud(player.getPlayerRef(), new ManaUi(player.getPlayerRef(), DBManager.DB_MANAGER.getManaFromPlayer(player.getPlayerRef().getUuid().hashCode())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}