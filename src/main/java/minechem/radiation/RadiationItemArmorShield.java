package minechem.radiation;

import minechem.api.IRadiationShield;
import minechem.gui.CreativeTabMinechem;
import minechem.reference.Textures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class RadiationItemArmorShield extends ItemArmor implements IRadiationShield
{

    private float radiationShieldFactor;
    private String textureFile;

    public RadiationItemArmorShield(int id, int part, float radiationShieldFactor, String texture)
    {
        super(ArmorMaterial.CHAIN, 2, EntityEquipmentSlot.values()[part]);
        this.radiationShieldFactor = radiationShieldFactor;
        this.setUnlocalizedName("itemArmorRadiationShield");
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        textureFile = texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        int percentile = (int) (radiationShieldFactor * 100);
        String info = String.format("%d%% Radiation Shielding", percentile);
        list.add(info);
    }

    @Override
    public float getRadiationReductionFactor(int baseDamage, ItemStack itemstack, EntityPlayer player)
    {
        itemstack.damageItem(baseDamage / 4, player);
        return radiationShieldFactor;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return Textures.Model.HAZMAT;
    }

}
