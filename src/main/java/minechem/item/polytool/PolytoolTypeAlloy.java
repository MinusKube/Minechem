package minechem.item.polytool;

import minechem.item.element.ElementAlloyEnum;
import minechem.item.element.ElementEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Iterator;

public class PolytoolTypeAlloy extends PolytoolUpgradeType
{

    private ElementAlloyEnum alloy;

    public PolytoolTypeAlloy(ElementAlloyEnum alloy)
    {
        this.alloy = alloy;
    }

    public PolytoolTypeAlloy(ElementAlloyEnum alloy, float power)
    {
        this.power = power;
        this.alloy = alloy;
    }

    public float getStrOre()
    {
        return this.alloy.pickaxe * this.power;
    }

    public float getStrStone()
    {
        return this.alloy.stone * this.power;
    }

    public float getStrAxe()
    {
        return this.alloy.axe * this.power;
    }

    public float getStrSword()
    {
        return this.alloy.sword * this.power;
    }

    public float getStrShovel()
    {
        return this.alloy.shovel * this.power;
    }

    @Override
    public float getStrVsBlock(ItemStack itemStack, Block block, int meta)
    {
        // There must be a better way to do this
        if (isToolEffective(new ItemStack(Items.DIAMOND_PICKAXE), block, meta))
        {
            for (int id : OreDictionary.getOreIDs(new ItemStack(block, 1, meta)))
                if (OreDictionary.getOreName(id).contains("stone")) return this.getStrStone();
            if (block == Blocks.STONE || block == Blocks.COBBLESTONE)
            {
                return this.getStrStone();
            }
            return this.getStrOre();
        } else if (isToolEffective(new ItemStack(Items.DIAMOND_SHOVEL), block, meta))
        {
            return this.getStrShovel();
        } else if (isToolEffective(new ItemStack(Items.DIAMOND_SWORD), block, meta))
        {
            return this.getStrSword();
        } else if (isToolEffective(new ItemStack(Items.DIAMOND_AXE), block, meta))
        {
            return this.getStrAxe();
        }
        return 0;
    }

    public boolean isToolEffective(ItemStack itemStack, Block block, int meta) {
        IBlockState state = block.getStateFromMeta(meta);
        Iterator var4 = itemStack.getItem().getToolClasses(itemStack).iterator();

        String type;
        do {
            if(!var4.hasNext()) {
                return false;
            }

            type = (String)var4.next();
        } while(!state.getBlock().isToolEffective(type, state));

        return true;
    }

    @Override
    public float getDamageModifier()
    {
        return getStrSword();
    }


    @Override
    public ElementEnum getElement()
    {
        return alloy.element;
    }

    @Override
    public String getDescription()
    {

        String result = "";

        result += "Ore: " + this.getStrOre() + " ";
        result += "Stone: " + this.getStrStone() + " ";
        result += "Sword: " + this.getStrSword() + " ";
        result += "Axe: " + this.getStrAxe() + " ";
        result += "Shovel: " + this.getStrShovel() + " ";

        return result;
    }
}
