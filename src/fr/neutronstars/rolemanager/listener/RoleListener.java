package fr.neutronstars.rolemanager.listener;

import fr.neutronstars.rolemanager.RoleManager;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by NeutronStars on 23/09/2017
 */
public class RoleListener extends ListenerAdapter
{
    private final RoleManager roleManager;

    public RoleListener(RoleManager roleManager)
    {
        this.roleManager = roleManager;
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        Role role = getRole(event);
        if(role == null) return;
        roleManager.getGuild().getController().addRolesToMember(roleManager.getGuild().getMember(event.getUser()), role, roleManager.getGuild().getRoleById(280073717642690562L)).queue();
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        Role role = getRole(event);
        if(role == null) return;
        roleManager.getGuild().getController().removeRolesFromMember(roleManager.getGuild().getMember(event.getUser()), role).queue();
    }

    private Role getRole(GenericMessageReactionEvent event)
    {
        roleManager.setGuild();
        if(roleManager.getGuild() == null) return null;

        if(event.getGuild().getIdLong() != roleManager.getGuild().getIdLong()) return null;
        if(!roleManager.hasMessage(event.getChannel().getIdLong())) return null;
        if(roleManager.getMessage(event.getChannel().getIdLong()).getIdLong() != event.getMessageIdLong()) return null;
        if(!roleManager.hasReaction(event.getReactionEmote().getId())) return null;

        return roleManager.getRole(event.getReactionEmote().getId());
    }
}
