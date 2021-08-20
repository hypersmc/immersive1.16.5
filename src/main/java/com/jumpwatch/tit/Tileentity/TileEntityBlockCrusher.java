package com.jumpwatch.tit.Tileentity;

import com.jumpwatch.tit.Registry.TileentityRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TileEntityBlockCrusher extends TileEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 0;
        if (event.getAnimatable().getLevel().isRaining()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("on", true));

        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("off", true));

        }
        return PlayState.CONTINUE;
    }
    public TileEntityBlockCrusher() {
        super(TileentityRegistry.Block_Crusher.get());
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
