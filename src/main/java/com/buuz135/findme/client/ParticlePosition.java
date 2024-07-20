package com.buuz135.findme.client;


import btw.client.fx.particles.WhiteSmokeFX;
import com.buuz135.findme.FindMeConfig;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class ParticlePosition extends WhiteSmokeFX {

    public ParticlePosition(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn, 0, 0, 0);
        this.noClip = true;
        this.motionX = 0.00;
        this.motionY = 0.00;
        this.motionZ = 0.00;
        this.particleMaxAge = FindMeConfig.MAX_PARTICLE_AGE;
        this.particleScale *= 1.3f;
        this.particleGreen = this.particleBlue = this.particleRed = 0.9f;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float f, float g, float h, float i, float j, float k) {
        super.renderParticle(tessellator, f, g, h, i, j, k);
        //stop drawing, we want to render in a custom way
        tessellator.draw();
        //disable gl depth test cap so it renders in front of everything
        GL11.glDisable(2929);
        //start drawing again
        tessellator.startDrawingQuads();
        //render the particle for real
        tessellator.setColorRGBA(255, 255 ,255, 255);
        super.renderParticle(tessellator, f, g, h, i, j, k);
        //finish this render pass
        tessellator.draw();
        //re enable gl depth test cap
        GL11.glEnable(2929);
        //start drawing quads again so everyone is happy
        tessellator.startDrawingQuads();

    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.setParticleTextureIndex(7  - this.particleAge * 8 / this.particleMaxAge);
    }

    /*
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.draw();

        GL11.glDepthMask(false);
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
//        GlStateManager.disableDepth();
//        GlStateManager.color(1f, 1f, 1f, 0.5f);
//        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        super.renderParticle(tessellator, f, g, h, i, j, k);
//        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        tessellator.draw();

//        GlStateManager.enableDepth();
        GL11.glDepthMask(true);

//        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }*/
}
