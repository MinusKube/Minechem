package minechem.potion;

import minechem.item.molecule.MoleculeEnum;
import minechem.utils.MinechemUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Iterator;

public class PotionEnchantmentCoated extends Enchantment
{

    private MoleculeEnum chemical;
    public static HashMap<MoleculeEnum, PotionEnchantmentCoated> chemLookup = new HashMap();

    protected PotionEnchantmentCoated(MoleculeEnum chem, int id)
    {
        super(Rarity.COMMON, EnumEnchantmentType.WEAPON, EntityEquipmentSlot.values());
        this.chemical = chem;
        this.setName(chem.getUnlocalizedName() + ".coated");
        PotionEnchantmentCoated.chemLookup.put(chem, this);
    }

    public void applyEffect(EntityLivingBase entity)
    {
        PharmacologyEffectRegistry.applyEffect(this.chemical, entity);
    }

    /**
     * Returns the minimum level that the enchantment can have.
     */
    @Override
    public int getMinLevel()
    {
        return 1;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel()
    {
        return 10;
    }

    @Override
    public boolean canApply(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * This applies specifically to applying at the enchanting table. The other method {@link #canApply(ItemStack)} applies for <i>all possible</i> enchantments.
     *
     * @param stack
     * @return
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return false;
    }

    @Override
    public String getTranslatedName(int level)
    {
        String enchantedName = I18n.format("enchantment.level." + level);
        if (I18n.hasKey("minechem.enchantment.coated"))
        {
            return MinechemUtil.getLocalString(chemical.getUnlocalizedName()) + " " + I18n.format("minechem.enchantment.coated", enchantedName);
        } else
        {
            return MinechemUtil.getLocalString(chemical.getUnlocalizedName()) + " " + enchantedName + " Coated";
        }
    }

    public static void registerCoatings()
    {
        for (MoleculeEnum molecule : MoleculeEnum.molecules.values())
        {
            if (molecule != null && PharmacologyEffectRegistry.hasEffect(molecule))
            {
                for (Iterator<Enchantment> iter = Enchantment.REGISTRY.iterator(); iter.hasNext(); )
                {
                    Enchantment ench = iter.next();

                    if(ench != null) {
                        new PotionEnchantmentCoated(molecule, Enchantment.getEnchantmentID(ench));
                        break;
                    }
                }
            }
        }

    }
}
