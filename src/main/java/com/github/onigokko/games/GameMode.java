package com.github.onigokko.games;

import java.util.ArrayList;
import java.util.List;

public enum GameMode {
    // 定数（他のゲームモードが追加される場合を考慮）
    FUEONI("増やし鬼");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<String> getModeList() {
        List<String> modeList = new ArrayList<>();
        for (GameMode mode : GameMode.values()) {
            modeList.add(mode.name().toLowerCase());
        }
        return modeList;

    }

    // ユーザー入力から該当するゲームモードを取得する静的メソッド
    public static GameMode fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String normalizedInput = input.trim().toUpperCase();
        for (GameMode mode : GameMode.values()) {
            // Enum名 または 表示名と比較
            if (mode.name().equalsIgnoreCase(normalizedInput) ||
                    mode.getDisplayName().equalsIgnoreCase(input.trim())) {
                return mode;
            }
        }
        return null; // 該当なし
    }
}
