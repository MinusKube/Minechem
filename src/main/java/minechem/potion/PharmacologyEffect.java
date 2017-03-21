package minechem.potion;

import minechem.utils.Constants;
import minechem.utils.EnumColour;
import minechem.utils.MinechemUtil;
import minechem.utils.PotionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;

public abstract class PharmacologyEffect
{
    private String colour;

    PharmacologyEffect(EnumColour colour)
    {
        this.colour = colour.toString();
    }

    public abstract void applyEffect(EntityLivingBase entityLivingBase);

    public String getColour()
    {
        return colour;
    }

    public static class Food extends PharmacologyEffect
    {
        private int level;
        private float saturation;

        public Food(int level, float saturation)
        {
            super(EnumColour.DARK_GREEN);
            this.level = level;
            this.saturation = saturation;
        }

        @Override
        public void applyEffect(EntityLivingBase entityLivingBase)
        {
            if (entityLivingBase instanceof EntityPlayer)
            {
                ((EntityPlayer) entityLivingBase).getFoodStats().addStats(level, saturation);
            }
        }

        @Override
        public String toString()
        {
            return "Food Effect: " + level + ", Saturation: " + saturation;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Food)
            {
                Food other = (Food) obj;
                if (other.level == this.level && other.saturation == this.saturation)
                {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Burn extends PharmacologyEffect
    {
        private int duration;

        public Burn(int duration)
        {
            super(EnumColour.RED);
            this.duration = duration;
        }

        @Override
        public void applyEffect(EntityLivingBase entityLivingBase)
        {
            entityLivingBase.setFire(duration);
        }

        @Override
        public String toString()
        {
            return "Burn Effect: " + duration + " s";
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Burn)
            {
                Burn other = (Burn) obj;
                if (other.duration == this.duration)
                {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Cure extends PharmacologyEffect
    {
        private net.minecraft.potion.Potion potion;

        public Cure()
        {
            this((net.minecraft.potion.Potion) null);
        }

        public Cure(net.minecraft.potion.Potion potion)
        {
            super(EnumColour.AQUA);
            this.potion = potion;
        }

        public Cure(String potionName)
        {
            this(PotionHelper.getPotionByName(potionName));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void applyEffect(EntityLivingBase entityLivingBase)
        {
            if (potion == null)
            {
                for (PotionEffect potionEffect : new ArrayList<PotionEffect>(entityLivingBase.getActivePotionEffects()))
                {
                    if (potionEffect.getCurativeItems().size() > 0)
                        entityLivingBase.removePotionEffect(potionEffect.getPotion());
                }
            } else
            {
                entityLivingBase.removePotionEffect(potion);
            }
        }

        @Override
        public String toString()
        {
            return "Cure Effect: " + (potion == null ? "all" : MinechemUtil.getLocalString(potion.getName()));
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Cure)
            {
                Cure other = (Cure) obj;
                if (other.potion == this.potion)
                {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Damage extends PharmacologyEffect
    {
        private float damage;

        public Damage(float damage)
        {
            super(EnumColour.ORANGE);
            this.damage = damage;
        }

        @Override
        public void applyEffect(EntityLivingBase entityLivingBase)
        {
            entityLivingBase.attackEntityFrom(DamageSource.generic, damage);
        }

        @Override
        public String toString()
        {
            float print = damage / 2;
            return "Damage Effect: " + print + " heart" + (print == 1 ? "" : "s");
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Damage)
            {
                Damage other = (Damage) obj;
                if (other.damage == this.damage)
                {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Potion extends PharmacologyEffect
    {
        private int duration;
        private int power;
        private net.minecraft.potion.Potion potion;

        public Potion(String potionName, int power, int duration)
        {
            this(PotionHelper.getPotionByName(potionName), power, duration);
        }

        public Potion(String potionName, int duration)
        {
            this(potionName, 0, duration);
        }

        public Potion(net.minecraft.potion.Potion potion, int duration)
        {
            this(potion, 0, duration);
        }

        public Potion(net.minecraft.potion.Potion potion, int power, int duration)
        {
            super(EnumColour.PURPLE);
            this.duration = duration;
            this.potion = potion;
            this.power = power;
        }

        @Override
        public void applyEffect(EntityLivingBase entityLivingBase)
        {
            entityLivingBase.addPotionEffect(new PotionEffect(potion, duration * Constants.TICKS_PER_SECOND, power));
        }

        @Override
        public String toString()
        {
            return "Potion Effect: " + MinechemUtil.getLocalString(potion.getName()) + ", Duration: " + duration + ", Power: " + power;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Potion)
            {
                Potion other = (Potion) obj;
                if (other.duration == this.duration && other.potion == this.potion && other.power == this.power)
                {
                    return true;
                }
            }
            return false;
        }
    }
}
