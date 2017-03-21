package minechem.item.chemistjournal;

import minechem.Minechem;
import minechem.gui.CreativeTabMinechem;
import minechem.gui.GuiHandler;
import minechem.utils.MinechemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ChemistJournalItem extends Item
{

    public static final String ITEMS_TAG_NAME = "discoveredItems";
    private static final String ACTIVE_ITEMSTACK_TAG = "activeItemStack";
    private static final String JOURNAL_OWNER_TAG = "owner";

    public ChemistJournalItem()
    {
        setUnlocalizedName("itemChemistJournal");
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer, EnumHand hand)
    {
        // Opens the GUI for the chemists journal.
        if (!world.isRemote)
        {
            entityPlayer.openGui(Minechem.INSTANCE, GuiHandler.GUI_ID_JOURNAL, world, entityPlayer.chunkCoordX, entityPlayer.chunkCoordY, entityPlayer.chunkCoordY);

            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null)
            {
                tagCompound = new NBTTagCompound();
            }

            // Save the players username onto the book for owned by purposes.
            tagCompound.setString(JOURNAL_OWNER_TAG, entityPlayer.getDisplayName().getFormattedText());

            itemStack.setTagCompound(tagCompound);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        NBTTagCompound stackTag = itemStack.getTagCompound();
        if (stackTag != null)
        {
            // Load the active set recipe in the book.
            NBTTagCompound activeTag = (NBTTagCompound) stackTag.getTag(ACTIVE_ITEMSTACK_TAG);
            if (activeTag != null)
            {
                ItemStack activeItemStack = ItemStack.loadItemStackFromNBT(activeTag);
                list.add("Active: " + activeItemStack.getDisplayName());
            }

            // Load owned by tag from the book which should be last user to use the book.
            String owner = stackTag.getString(JOURNAL_OWNER_TAG);
            String ownerTag = MinechemUtil.getLocalString("minechem.owner.tag");
            if (ownerTag.isEmpty() || ownerTag.equals("minechem.owner.tag"))
            {
                list.add("Owner: " + owner);
            } else
            {
                list.add(ownerTag + ": " + owner);
            }
        }
    }

    // ** Sets the currently active stack in the chemist journal. Used by Synthesis machine to auto-load recipe. */
    public void setActiveStack(ItemStack itemstack, ItemStack journalStack)
    {
        NBTTagCompound journalTag = journalStack.getTagCompound();
        if (journalTag == null)
        {
            journalTag = new NBTTagCompound();
        }

        NBTTagCompound stackTag = itemstack.writeToNBT(new NBTTagCompound());
        journalTag.setTag(ACTIVE_ITEMSTACK_TAG, stackTag);
        journalStack.setTagCompound(journalTag);
    }

    // ** Returns last selected recipe in chemists journal. */
    public ItemStack getActiveStack(ItemStack journalStack)
    {
        NBTTagCompound journalTag = journalStack.getTagCompound();
        if (journalTag != null)
        {
            NBTTagCompound stackTag = (NBTTagCompound) journalTag.getTag(ACTIVE_ITEMSTACK_TAG);
            if (stackTag != null)
            {
                return ItemStack.loadItemStackFromNBT(stackTag);
            }
        }
        return null;
    }

    // ** Returns a list of itemstacks that are the currently known recipes by the player. */
    public List<ItemStack> getItemList(ItemStack journal)
    {
        NBTTagCompound tag = journal.getTagCompound();
        if (tag != null)
        {
            NBTTagList taglist = tag.getTagList(ITEMS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
            if (taglist != null)
            {
                return MinechemUtil.readTagListToItemStackList(taglist);
            }
        }
        return null;
    }

    // ** Adds newly discovered recipes into your chemist journal. */
    public void addItemStackToJournal(ItemStack itemstack, ItemStack journal, World world)
    {
        NBTTagCompound tagCompound = journal.getTagCompound();
        if (tagCompound == null)
        {
            tagCompound = new NBTTagCompound();
        }

        NBTTagList taglist = tagCompound.getTagList(ITEMS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
        if (taglist == null)
        {
            taglist = new NBTTagList();
        }

        ArrayList<ItemStack> itemArrayList = MinechemUtil.readTagListToItemStackList(taglist);
        if (!hasDiscovered(itemArrayList, itemstack))
        {
            taglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            tagCompound.setTag(ITEMS_TAG_NAME, taglist);
            journal.setTagCompound(tagCompound);
        }
    }

    // ** Returns true if the player has already discovered this recipe. */
    private boolean hasDiscovered(ArrayList<ItemStack> list, ItemStack itemstack)
    {
        for (ItemStack itemstack2 : list)
        {
            if (itemstack.isItemEqual(itemstack2))
            {
                return true;
            }
        }
        return false;
    }

}
