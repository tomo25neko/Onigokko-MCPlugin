package com.github.onigokko.score;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager extends ScoreboardManager{

    private Team oni;
    private Team nige;
    private Scoreboard scoreboard = super.getScoreboard();

    public void createOniTeams(String teamName) {
        // 鬼チームの作成
        if (scoreboard.getTeam("oni") == null) {
            oni = scoreboard.registerNewTeam("oni");
            oni.setPrefix(ChatColor.RED + "[" + teamName + "] ");

            //チームの詳細設定
            oni.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS); // 自チーム以外には攻撃可能
            oni.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM); //自チームにネームタグ常時表示
            oni.setCanSeeFriendlyInvisibles(true); // 不可視状態の味方は視認可能

            //スコア表示
            setTeamSizeToScoreboard(oni);
        } else {
            oni = scoreboard.getTeam("oni");
        }
    }

    public void createNigeTeams(String teamName) {
        // 逃げチームの作成
        if (scoreboard.getTeam("nige") == null) {
            nige = scoreboard.registerNewTeam("nige");
            nige.setPrefix(ChatColor.BLUE + "[" + teamName + "] ");

            //チームの詳細設定
            nige.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER); // チーム内のフレンドリーファイア無効
            nige.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER); // ネームタグ常時非表示
            nige.setCanSeeFriendlyInvisibles(false); // 不可視状態の味方は視認不可

            //スコア表示
            setTeamSizeToScoreboard(nige);
        } else {
            nige = scoreboard.getTeam("nige");
        }
    }

    //プレイヤーを指定されたチームに追加
    public void addPlayerToTeam(Team team, String  player) {
        removePlayerAllTeam(player);
        team.addEntry(player);
        setTeamSizeToScoreboard(team);
    }

    //指定されたプレイヤーを全てのチーム(鬼と逃げ)から削除
    public void removePlayerAllTeam(String player) {
        for(Team team : scoreboard.getTeams()) {
            if(team.hasEntry(player)) {
                team.removeEntry(player);
                setTeamSizeToScoreboard(team);
            }
        }
    }
    //新たなチームサイズの反映処理。
    private void setTeamSizeToScoreboard(Team team) {
        //oniチームなら７で、nigeチームなら8の順番で表示
        super.setScore(team.getDisplayName(), team.getSize(), (team.getName().equals("nige") ? 8 : 7) );
    }

    //全てのチームを削除
    public void removeTeams() {
        for (Team team : scoreboard.getTeams()) {
            super.removeScore(team.getDisplayName());
            team.unregister();
        }
    }

    public Team getOni() {
        return oni;
    }

    public Team getNige() {
        return nige;
    }
}
