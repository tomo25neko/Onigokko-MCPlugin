package com.github.onigokko.score;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TeamManager extends ScoreboardManager{

    private Team oni;
    private Team nige;

    public void createOniTeams(String teamName) {
        // 鬼チームの作成
        if (getScoreboard().getTeam("oni") == null) {
            oni = getScoreboard().registerNewTeam("oni");
            oni.setPrefix(ChatColor.RED + "[" + teamName + "] ");

            //チームの詳細設定
            oni.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER); // チーム内のフレンドリーファイア無効
            oni.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM); //自チームにネームタグ常時表示
            oni.setCanSeeFriendlyInvisibles(false); // 不可視状態の味方は視認不可
        } else {
            oni = getScoreboard().getTeam("oni");
        }
    }

    public void createNigeTeams(String teamName) {
        // 逃げチームの作成
        if (getScoreboard().getTeam("nige") == null) {
            nige = getScoreboard().registerNewTeam("nige");
            nige.setPrefix(ChatColor.BLUE + "[" + teamName + "] ");

            //チームの詳細設定
            nige.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER); // チーム内のフレンドリーファイア無効
            nige.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER); // ネームタグ常時非表示
            nige.setCanSeeFriendlyInvisibles(false); // 不可視状態の味方は視認不可
        } else {
            nige = getScoreboard().getTeam("nige");
        }
    }

    public void addPlayerToTeam(Team team, Player player) {
        removePlayerAllTeam(player);
        team.addEntry(player.getName());
    }
    //指定されたプレイヤーを全てのチーム(鬼と逃げ)から削除
    public void removePlayerAllTeam(Player player) {
        for(Team team : getScoreboard().getTeams()) {
            if(team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    public void removeTeams() {
        for (Team team : getScoreboard().getTeams()) {
            team.unregister();
        }
    }
}
