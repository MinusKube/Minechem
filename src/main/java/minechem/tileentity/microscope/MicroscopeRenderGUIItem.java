package minechem.tileentity.microscope;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class MicroscopeRenderGUIItem extends RenderItem
{
    public MicroscopeContainer microscopeContainer;
    public InventoryPlayer inventoryPlayer;
    public MicroscopeGui microscopeGui;

    public MicroscopeRenderGUIItem(MicroscopeGui microscopeGui)
    {
        super(
                Minecraft.getMinecraft().getTextureManager(),
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager(),
                Minecraft.getMinecraft().getItemColors()
        );

        this.microscopeGui = microscopeGui;
        microscopeContainer = (MicroscopeContainer) microscopeGui.inventorySlots;
        inventoryPlayer = microscopeGui.inventoryPlayer;
    }

    private void setScissor(float x, float y, float w, float h)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        int scale = scaledRes.getScaleFactor();
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;
        float guiScaledWidth = (microscopeGui.guiWidth * scale);
        float guiScaledHeight = (microscopeGui.guiHeight * scale);
        float guiLeft = ((mc.displayWidth / 2) - guiScaledWidth / 2);
        float guiTop = ((mc.displayHeight / 2) + guiScaledHeight / 2);
        int scissorX = Math.round((guiLeft + x));
        int scissorY = Math.round(((guiTop - h) - y));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, (int) w, (int) h);
    }

    private void stopScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void renderItemAndEffectIntoGUI(ItemStack itemStack, int x, int y)
    {
        if (itemStack == null)
        {
            return;
        }

        RenderHelper.enableGUIStandardItemLighting();

        Slot slot = microscopeContainer.getSlot(0);
        if (slot.getStack() != null)
        {
            GL11.glPushMatrix();
            setScissor(microscopeGui.eyepieceX, microscopeGui.eyepieceY, 52, 52);
            int renderX = microscopeGui.getGuiLeft() + slot.xDisplayPosition;
            int renderY = microscopeGui.getGuiTop() + slot.yDisplayPosition;
            GL11.glTranslatef(renderX, renderY, 0.0F);
            GL11.glScalef(3.0F, 3.0F, 1.0F);
            GL11.glTranslatef(-renderX - 5.3F, -renderY - 5.3F, 2.0F);
            super.renderItemAndEffectIntoGUI(slot.getStack(), renderX, renderY);
            stopScissor();
            GL11.glPopMatrix();
        }

        if (itemStack == inventoryPlayer.getItemStack() && microscopeGui.isMouseInMicroscope())
        {
            GL11.glPushMatrix();
            setScissor(microscopeGui.eyepieceX, microscopeGui.eyepieceY, 52, 52);
            int renderX = x;
            int renderY = y;
            GL11.glTranslatef(renderX, renderY, 0.0F);
            GL11.glScalef(3.0F, 3.0F, 1.0F);
            GL11.glTranslatef(-renderX - 5.3F, -renderY - 5.3F, 540F);
            super.renderItemAndEffectIntoGUI(itemStack, renderX, renderY);
            stopScissor();
            GL11.glPopMatrix();
        }
    }
}
