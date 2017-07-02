package minechem.item.polytool;

import minechem.item.element.ElementEnum;
import minechem.tileentity.decomposer.DecomposerRecipe;
import minechem.tileentity.decomposer.DecomposerRecipeHandler;
import minechem.utils.MinechemUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PolytoolEventHandler
{

    private static final Random random = new Random();

    public void addDrops(LivingDropsEvent event, ItemStack dropStack)
    {
        EntityItem entityitem = new EntityItem(event.getEntityLiving().worldObj, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, dropStack);
        entityitem.setPickupDelay(10);
        event.getDrops().add(entityitem);
    }

    @SubscribeEvent
    public void onDrop(LivingDropsEvent event)
    {

        // Large page of the beheading code based off TiC code
        // Thanks to mDiyo
        if ("player".equals(event.getSource().damageType))
        {

            EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
            ItemStack stack = player.getActiveItemStack();
            if (stack != null && stack.getItem() instanceof PolytoolItem)
            {
                float powerSilicon = PolytoolItem.getPowerOfType(stack, ElementEnum.Si);
                if (powerSilicon > 0)
                {
                    int amount = (int)Math.ceil(random.nextDouble() * powerSilicon);
                    Iterator iter = event.getDrops().iterator();
                    if (random.nextInt(16) < 1 + powerSilicon)
                    {
                        ArrayList<EntityItem> trueResult = new ArrayList<EntityItem>();
                        while (iter.hasNext())
                        {
                            EntityItem entityItem = (EntityItem)iter.next();
                            ItemStack item = entityItem.getEntityItem();
                            while (item.stackSize > 0)
                            {
                                // Always avoid chances
                                DecomposerRecipe recipe = DecomposerRecipeHandler.instance.getRecipe(item);

                                if (recipe != null)
                                {
                                    ArrayList<ItemStack> items = MinechemUtil.convertChemicalsIntoItemStacks(recipe.getOutput());
                                    for (ItemStack itemStack : items)
                                    {
                                        trueResult.add(new EntityItem(entityItem.worldObj, entityItem.posX, entityItem.posY, entityItem.posZ, itemStack));
                                    }
                                } else
                                {
                                    trueResult.add(entityItem);
                                    break;
                                }
                                item.stackSize--;
                            }

                        }
                        event.getDrops().clear();
                        event.getDrops().addAll(trueResult);
                    }
                }
            }
            if (event.getEntityLiving() instanceof EntitySkeleton || event.getEntityLiving() instanceof EntityZombie || event.getEntityLiving() instanceof EntityPlayer)
            {

                EntityLivingBase enemy = event.getEntityLiving();

                if (stack != null && stack.getItem() instanceof PolytoolItem)
                {
                    // Nitrogen preservation
                    if (enemy instanceof EntityZombie)
                    {

                        float power = PolytoolItem.getPowerOfType(stack, ElementEnum.N);
                        if (power > 0)
                        {
                            int amount = (int)Math.ceil(random.nextDouble() * power);
                            addDrops(event, new ItemStack(Items.COOKED_BEEF, amount, 0));
                            Iterator iter = event.getDrops().iterator();
                            while (iter.hasNext())
                            {
                                EntityItem entityItem = (EntityItem)iter.next();
                                if (entityItem.getEntityItem().getItem() == Items.ROTTEN_FLESH)
                                {
                                    iter.remove();
                                }
                            }
                        }
                    }
                    // Calcium bonus
                    if (enemy instanceof EntitySkeleton)
                    {
                        float power = PolytoolItem.getPowerOfType(stack, ElementEnum.Ca);
                        if (power > 0)
                        {
                            int amount = (int)Math.ceil(random.nextDouble() * power);
                            Iterator iter = event.getDrops().iterator();
                            while (iter.hasNext())
                            {
                                EntityItem entityItem = (EntityItem)iter.next();
                                if (entityItem.getEntityItem().getItem() == Items.BONE)
                                {
                                    entityItem.getEntityItem().stackSize += amount;
                                }
                            }
                        }
                    }
                    // Beryllium beheading
                    float beheading = PolytoolItem.getPowerOfType(stack, ElementEnum.Be);
                    while (beheading > 5)
                    {
                        if (beheading > 0 && random.nextInt(5) < beheading * 10)
                        {
                            if (event.getEntityLiving() instanceof EntitySkeleton)
                            {
                                EntitySkeleton skeleton = (EntitySkeleton)enemy;
                                addDrops(event, new ItemStack(Items.SKULL, 1, skeleton.getSkeletonType().getId()));
                            } else if (event.getEntityLiving() instanceof EntityZombie)
                            {
                                addDrops(event, new ItemStack(Items.SKULL, 1, 2));
                            } else if (event.getEntityLiving() instanceof EntityPlayer)
                            {
                                ItemStack dropStack = new ItemStack(Items.SKULL, 1, 3);
                                NBTTagCompound nametag = new NBTTagCompound();
                                nametag.setString("SkullOwner", player.getDisplayName().getFormattedText());
                                addDrops(event, dropStack);
                            }
                        }

                        // More head drops if level>5
                        beheading--;
                    }
                }
            }
        }
    }
}
