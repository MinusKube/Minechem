package minechem.tileentity.leadedchest;

import codechicken.lib.render.item.IItemRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LeadedChestItemRenderer implements IItemRenderer {

    LeadedChestTileEntity leadedChest;

    public LeadedChestItemRenderer()
    {
        this.leadedChest = new LeadedChestTileEntity();
    }

    @Override
    public void renderItem(ItemStack item)
    {
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.leadedChest, 0.0D, 0.0D, 0.0D, 0.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
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
