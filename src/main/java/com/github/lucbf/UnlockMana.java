package com.github.lucbf;



import com.github.lucbf.ui.ManaUi;
import com.hypixel.hytale.builtin.ambience.systems.ForcedMusicSystems;
import com.hypixel.hytale.logger.HytaleLogger;

import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.itemanimation.ItemPlayerAnimationsPacketGenerator;
import com.hypixel.hytale.server.core.asset.type.itemanimation.config.ItemPlayerAnimations;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.effect.ActiveEntityEffect;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.inventory.transaction.ActionType;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackTransaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.npc.asset.builder.InstructionContextHelper;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


import javax.annotation.Nonnull;
import java.awt.*;
import java.sql.SQLException;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class UnlockMana extends JavaPlugin {
    private final HytaleLogger.Api LOGGER;

    public static final SimpleItemContainer fireItemContainer = new SimpleItemContainer((short) 1);
    public static final SimpleItemContainer iceItemContainer = new SimpleItemContainer((short) 1);



    public UnlockMana(@NonNullDecl JavaPluginInit init) throws SQLException {
        super(init);
        LOGGER = this.getLogger().atInfo();
    }

    public void setup() {
        LOGGER.log("Initializing...");
        this.getCodecRegistry(Interaction.CODEC).register("ObtainFireMana", ObtainFireManaInteraction.class, ObtainFireManaInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register("ObtainIceMana", ObtainIceManaInteraction.class, ObtainIceManaInteraction.CODEC);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, this::onPlayerAddToWorld);
        this.getEventRegistry().registerGlobal(LivingEntityInventoryChangeEvent.class, this::onChangeInventory);
        LOGGER.log("Done!");
    }


    private void onPlayerAddToWorld(@Nonnull PlayerReadyEvent event) {
        Player player = event.getPlayer();
        try {
            if (DBManager.DB_MANAGER.getManaFromPlayer(player.getPlayerRef().getUuid().hashCode()) != null){
                player.getHudManager().setCustomHud(player.getPlayerRef(), new ManaUi(player.getPlayerRef(), DBManager.DB_MANAGER.getManaFromPlayer(player.getPlayerRef().getUuid().hashCode())));
                /*EffectControllerComponent ec = event.getPlayerRef().getStore().getComponent(event.getPlayerRef(), EffectControllerComponent.getComponentType());
                EntityEffect ef = new EntityEffect("Teleport_Infinite");
                assert ec != null;
                ec.addInfiniteEffect(event.getPlayerRef(), EntityEffect.getAssetMap().getIndex(ef.getId()), ef, event.getPlayerRef().getStore());*/
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    private void onChangeInventory (@Nonnull LivingEntityInventoryChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            ManaType mt;
            try {
                mt = DBManager.DB_MANAGER.getManaFromPlayer(player.getPlayerRef().getUuid().hashCode());
            } catch (SQLException e) {
                mt = null;
            }

            if (mt == null) {
                return;
            }

            String item_name;
            if (mt != ManaType.ANY) {
                item_name = (mt == ManaType.FIRE) ? "Ingredient_Fire_Essence" : "Ingredient_Ice_Essence";

            } else {
                if (player.getInventory().getActiveHotbarItem() != null && player.getInventory().getActiveHotbarItem().getItem().getId().equals("Weapon_Staff_Crystal_Ice")) {
                    item_name = "Ingredient_Ice_Essence";
                } else if (player.getInventory().getActiveHotbarItem().getItem().getId().equals("Weapon_Staff_Crystal_Flame")) {
                    item_name = "Ingredient_Fire_Essence";
                } else {
                    return;
                }
            }

            if (event.getTransaction() instanceof ItemStackTransaction transaction) {
                if (transaction.getQuery().getItem().getId().equals(item_name) && transaction.getAction() == ActionType.REMOVE && transaction.succeeded()) {
                    LOGGER.log(event.toString());
                    event.getItemContainer().addItemStack(new ItemStack(item_name, 1));
                }
            }
        }
    }
}

