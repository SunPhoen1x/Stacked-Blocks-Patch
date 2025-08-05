package com.phoen1x.mixin;

import com.axperty.stackedblocks.StackedBlocks;
import com.axperty.stackedblocks.registry.BlockRegistry;
import com.phoen1x.impl.block.StackedBlocksStatePolymerBlock;
import com.phoen1x.impl.item.StackedBlocksPolyBaseItem;
import eu.pb4.factorytools.api.block.model.generic.BlockStateModelManager;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(BlockRegistry.class)
public class SBBlockRegistryMixin {
    @Inject(method = "registerBlock(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;", at = @At("TAIL"))
    private static void polymerify(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, CallbackInfoReturnable<Block> cir) {
        polymerify(path, cir.getReturnValue());
    }

    @Unique
    private static void polymerify(String path, Block block) {
        Identifier blockId = Identifier.of(StackedBlocks.MODID, path);
        BlockStateModelManager.addBlock(blockId, block);
        PolymerBlock overlay;
        overlay = StackedBlocksStatePolymerBlock.of(block, BlockModelType.FULL_BLOCK);
        PolymerBlock.registerOverlay(block, overlay);
    }

    @Inject(method = "registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("TAIL"))
    private static void polymerify(String path, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir) {
        PolymerItem polymerItem = new StackedBlocksPolyBaseItem(cir.getReturnValue());
        PolymerItem.registerOverlay(cir.getReturnValue(), polymerItem);
    }
}