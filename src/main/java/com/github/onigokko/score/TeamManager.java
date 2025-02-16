package com.github.onigokko.score;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {

    private Team oni;
    private Team nige;
    private final ScoreboardManager sbManager;
    private final Scoreboard scoreboard;

    public TeamManager(ScoreboardManager scoreboardManager) {
        this.sbManager = scoreboardManager;
        this.scoreboard = sbManager.getScoreboard();
        createOniTeams();
        createNigeTeams();
    }

    private void createOniTeams() {
        // 鬼チームの作成
        if (scoreboard.getTeam("oni") == null) {
            oni = scoreboard.registerNewTeam("oni");
            oni.setPrefix(ChatColor.RED + "[鬼] ");//プレイヤーの名前の前につくチーム名の変更
            oni.setDisplayName(ChatColor.RED + "[鬼]" + ChatColor.GREEN + "チーム：    %d人");//表示名変更

            //チームの詳細設定
            setOniAttack(false); //攻撃不可
            oni.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM); //自チームにネームタグ常時表示
            oni.setCanSeeFriendlyInvisibles(true); // 不可視状態の味方は視認可能

            //スコア表示
            setTeamSizeToScoreboard(oni);
        }
    }
    /*
     *鬼が攻撃可能か設定
     * @param b == true 攻撃可能
     */
    public void setOniAttack(Boolean b) {
        if(b) {
            oni.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM); // 自チーム以外には攻撃可能
        } else {
            oni.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER); // 攻撃不可
        }
    }

    private void createNigeTeams() {
        // 逃げチームの作成
        if (scoreboard.getTeam("nige") == null) {
            nige = scoreboard.registerNewTeam("nige");
            nige.setPrefix(ChatColor.DARK_AQUA + "[逃げ] ");
            nige.setDisplayName(ChatColor.DARK_AQUA + "[逃げ]" + ChatColor.GREEN + "チーム： %d人");

            //チームの詳細設定
            nige.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER); // チーム内のフレンドリーファイア無効
            nige.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER); // ネームタグ常時非表示
            nige.setCanSeeFriendlyInvisibles(false); // 不可視状態の味方は視認不可

            //スコア表示
            setTeamSizeToScoreboard(nige);
        }
    }

    //チームの名前を変更
    public void setTeamName(Team team,String teamName) {
        if(team == nige) {
            team.setDisplayName(ChatColor.DARK_AQUA + "[" + teamName + "]" + ChatColor.GREEN + "チーム： %d人");//逃げ
        } else {
            team.setDisplayName(ChatColor.RED + "[" + teamName + "]" + ChatColor.GREEN + "チーム：    %d人");//鬼
        }

    }

    //プレイヤーを指定されたチームに追加
    public void addPlayerToTeam(Team team, String player) {
        removePlayerAllTeam(player);
        team.addEntry(player);
        setTeamSizeToScoreboard(team);
    }

    //指定されたプレイヤーを全てのチーム(鬼と逃げ)から削除
    public void removePlayerAllTeam(String player) {
        for (Team team : scoreboard.getTeams()) {
            //プレイヤーをチームから削除できたら、スコア更新
            if (team.removeEntry(player)) setTeamSizeToScoreboard(team);
        }
    }

    //新たなチームサイズの反映処理。
    private void setTeamSizeToScoreboard(Team team) {
        //oniチームなら3で、nigeチームなら4の順番で表示
        sbManager.setScore(team.getDisplayName(), team.getSize(), (team.getName().equals("nige") ? 4 : 3));
    }

    //全てのチームを削除
    public void removeTeams() {
        for (Team team : scoreboard.getTeams()) {
            sbManager.removeScore(team.getName());
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
