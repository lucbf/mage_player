package com.github.lucbf.ui;
import com.github.lucbf.ManaType;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.PatchStyle;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.lang.reflect.Field;


public class ManaUi extends CustomUIHud {
    private ManaType mt;

    public ManaUi(@NonNullDecl PlayerRef playerRef, ManaType mt) {
        this.mt = mt;
        super(playerRef);
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("Pages/Mana.ui");

        String path = "Pages/";
        switch (mt) {
            case ManaType.ICE: {
                path += "IceMana.png";
                break;
            }
            case ManaType.FIRE: {
                path += "FireMana.png";
                break;
            }
            case ManaType.ANY: {
                path += "AnyMana.png";
                break;
            }
            default:
                throw new IllegalArgumentException("ManaType probably is null");
        }
        uiCommandBuilder.set("#ImageGrouped559173.Background", path);
    }
}