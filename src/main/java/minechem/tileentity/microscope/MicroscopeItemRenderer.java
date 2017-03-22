package minechem.tileentity.microscope;

import codechicken.lib.render.item.IItemRenderer;
import minechem.reference.Resources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

public class MicroscopeItemRenderer implements IItemRenderer {

    private MicroscopeModel microscopeModel;

    public MicroscopeItemRenderer()
    {
        microscopeModel = new MicroscopeModel();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type.ordinal())
        {
            case 0:
            {
                return true;
            }

            case 1:
            {
                return true;
            }

            case 2:
            {
                return true;
            }
            case 3:
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return helper == ItemRendererHelper.INVENTORY_BLOCK || helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        GL11.glPushMatrix();
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.Model.MICROSCOPE);
        switch (type.ordinal())
        {
            case 0:
            {

            }
            case 1:
            {

                GL11.glRotatef(2.0F, 0F, 0.0F, 0.0F);
                GL11.glTranslatef(0F, -0.5F, 0.5F);
            }
            case 2:
            {
                GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                GL11.glRotatef(-4.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(2.0F, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(1F, 1F, 1F);
            }
            case 3:
            {
                GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180f, 0f, 0f, 1f);
            }

        }

        microscopeModel.render(0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemStack item) {

    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState iBlockState, @Nullable EnumFacing enumFacing, long l) {
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
