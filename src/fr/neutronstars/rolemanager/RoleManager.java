package fr.neutronstars.rolemanager;

import fr.neutronstars.nbot.NBot;
import fr.neutronstars.nbot.command.Command;
import fr.neutronstars.nbot.command.CommandManager;
import fr.neutronstars.nbot.command.SimpleCommand;
import fr.neutronstars.nbot.entity.Channel;
import fr.neutronstars.nbot.entity.Guild;
import fr.neutronstars.nbot.entity.Message;
import fr.neutronstars.nbot.entity.User;
import fr.neutronstars.nbot.plugin.NBotPlugin;
import fr.neutronstars.nbot.util.JSONReader;
import fr.neutronstars.nbot.util.JSONWriter;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NeutronStars on 21/09/2017
 */
public class RoleManager extends ListenerAdapter
{
    private final NBotPlugin plugin;
    private final Map<Long, Message> messageMap = new HashMap<>();
    private Map<String, Long> roleReact;
    private Guild guild;

    public RoleManager(NBotPlugin plugin)
    {
        this.plugin = plugin;
    }

    public Guild getGuild()
    {
        return guild;
    }

    public boolean hasMessage(Long id)
    {
        return messageMap.containsKey(id);
    }

    public Message getMessage(Long id)
    {
        return messageMap.get(id);
    }

    public boolean hasReaction(String id)
    {
        return roleReact.containsKey(id);
    }

    public Role getRole(String id)
    {
        return guild.getRoleById(roleReact.get(id));
    }

    public Collection<String> getEmotesId()
    {
        return new ArrayList<>(roleReact.keySet());
    }

    public void setMessage(long id, Message message)
    {
        messageMap.put(id, message);
    }

    public void addRoleEmote(String emoteId, long roleId)
    {
        roleReact.put(emoteId, roleId);
    }

    public void removeEmote(String id)
    {
        roleReact.remove(id);
    }

    public void setGuild()
    {
        if(guild != null) return;

        guild = NBot.getGuild(NBot.getJDA().getGuildById(238975753969074177L));
        if(guild == null) return;

        File file = new File(guild.getFolder(), "rolemanager.json");
        if(!file.exists())
        {
            roleReact = new HashMap<>();
            return;
        }
        roleReact = JSONReader.toMap(file);
    }

    public void save()
    {
        if(guild == null) return;

        JSONObject object = new JSONObject();
        for(String key : roleReact.keySet()) object.put(key, roleReact.get(key));

        try(JSONWriter writer = new JSONWriter(new File(guild.getFolder(), "rolemanager.json"))){
            writer.write(object);
            writer.flush();
        }catch(IOException e){
            plugin.getLogger().logThrowable(e);
        }
    }


}
