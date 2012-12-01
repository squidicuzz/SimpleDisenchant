package me.shock.disenchant;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	
	Boolean usevault;
	double cost;
	
    PluginManager pm;
	FileConfiguration newConfig;
    private Logger log;
    public static Economy econ = null;
  
  public void onEnable()
  {  
	  this.pm = getServer().getPluginManager();
		
	  if (!setupEconomy() ) 
	  {
          log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
          getServer().getPluginManager().disablePlugin(this);
          return;
      }
		
	  loadConfig();
  }

  public void onDisable()
  {
	  
  }
  
  private boolean setupEconomy() 
  {
      if (getServer().getPluginManager().getPlugin("Vault") == null) {
          return false;
      }
      RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
      if (rsp == null) {
          return false;
      }
      econ = rsp.getProvider();
      return econ != null;
  }
  
  public void loadConfig()
  {
	  
	  try {
			reloadConfig();
			this.newConfig = getConfig();
			this.newConfig.options().copyDefaults(true);
			
			this.usevault = Boolean.valueOf(this.newConfig.getBoolean("use_vault", false));
			this.cost = Double.valueOf(this.newConfig.getDouble("cost"));
			saveConfig();
			this.log.info("[SimpleDisenchant] config loaded");
		      }
		    catch (Exception e) 
		  {
			  this.log.severe("[SimpleDisenchant] Failed to load config");
			  e.printStackTrace();
			  // Hopefully never happens :o
		  }
	  
  }
  
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((sender instanceof Player))
    {
      if (cmd.getName().equalsIgnoreCase("disenchant") && sender.hasPermission("disenchant.use"))
      {
    	  String player = sender.getName();
    	  double balance = econ.getBalance(sender.getName());
    	  EconomyResponse r = econ.withdrawPlayer(player, cost);
    	  
    	  if (balance >= cost)
    	  {
    		  
    		  // removing all enchantments
    		  ItemStack item = ((Player)sender).getItemInHand();
   	          item.removeEnchantment(Enchantment.ARROW_DAMAGE);
  	          item.removeEnchantment(Enchantment.ARROW_FIRE);
  	          item.removeEnchantment(Enchantment.ARROW_INFINITE);
  	          item.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
  	          item.removeEnchantment(Enchantment.DAMAGE_ALL);
  	          item.removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
  	          item.removeEnchantment(Enchantment.DAMAGE_UNDEAD);
  	          item.removeEnchantment(Enchantment.DIG_SPEED);
  	          item.removeEnchantment(Enchantment.DURABILITY);
  	          item.removeEnchantment(Enchantment.FIRE_ASPECT);
  	          item.removeEnchantment(Enchantment.KNOCKBACK);
  	          item.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
  	          item.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
  	          item.removeEnchantment(Enchantment.OXYGEN);
  	          item.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
  	          item.removeEnchantment(Enchantment.PROTECTION_EXPLOSIONS);
  	          item.removeEnchantment(Enchantment.PROTECTION_FALL);
  	          item.removeEnchantment(Enchantment.PROTECTION_FIRE);
  	          item.removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
  	          item.removeEnchantment(Enchantment.SILK_TOUCH);
  	          item.removeEnchantment(Enchantment.WATER_WORKER);
  	        
            sender.sendMessage(ChatColor.GREEN + "Your enchantments are gone :o");
            
    	  }
    	  else
    	  {
    		  // When the sender doesn't have enough money.
    		  sender.sendMessage(ChatColor.RED + "Insufficient funds. Trying walking your neighbors dog.");
    	  }
      }
      else
      {
    	  // When permission check fails.
    	  sender.sendMessage(ChatColor.RED + "You don't have permission for this.");
      }
    }
    // If the sender isn't a player.
    return false;
  }

}