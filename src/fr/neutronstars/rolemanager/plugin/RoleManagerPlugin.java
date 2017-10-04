package fr.neutronstars.rolemanager.plugin;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import fr.neutronstars.rolemanager.RoleManager;
import fr.neutronstars.rolemanager.command.RoleCommand;
import fr.neutronstars.rolemanager.listener.RoleListener;

/**
 * Created by NeutronStars on 21/09/2017
 */
public class RoleManagerPlugin extends NBotPlugin
{
    private RoleManager roleManager = new RoleManager(this);

    public RoleManagerPlugin()
    {
        super("NeutronStars");
    }

    public void onRegisterCommands()
    {
        registerCommand(new RoleCommand(roleManager));
    }

    public void onEnable()
    {
        roleManager.setGuild();
        NBot.getJDA().addEventListener(new RoleListener(roleManager));
    }

    public void onDisable()
    {
        roleManager.save();
    }
}
