# Onigokko - Minecraft Spigot Plugin

Minecraftサーバーで鬼ごっこゲームができるSpigotプラグインです。

## 概要

このプラグインは、Minecraftサーバー上で鬼ごっこゲームを提供します。以下のゲームモードをサポートしています：

- **鬼ごっこ**: 捕まったプレイヤーが鬼になり、最後まで逃げ切ったプレイヤーが勝利
- **増やし鬼**: 捕まったプレイヤーが鬼チームに加わり、逃げチームが全滅したら鬼チームの勝利

## 機能

- 複数のゲームモード（鬼ごっこ、増やし鬼）
- ゲーム時間の設定
- スタート地点の設定
- チーム管理（鬼・逃げ）
- ランダムな鬼選出（マグマブロック上のプレイヤーから抽選）
- スコアボードによる情報表示
- ボスバーによる残り時間表示
- 鬼プレイヤーの識別（赤いパーティクル、装備）
- ゲーム開始準備状況チェックコマンド
- スタート地点へのテレポート機能（はまりスポット脱出用）

## インストール方法

1. [Releases](https://github.com/tomo25neko/Onigokko-MCPlugin/releases) から最新のJARファイルをダウンロード
2. ダウンロードしたJARファイルをサーバーの `plugins` フォルダに配置
3. サーバーを再起動または `/reload` を実行

## 使い方

### ゲーム開始までの流れ

1. `/setgamemode <モード>` - ゲームモードを設定
2. `/setgametime <秒数>` - ゲーム時間を設定
3. `/setstart` - スタート地点を設定（プレイヤーが立っている位置）
4. `/setteam <oni|nige> <プレイヤー名>` - チームにプレイヤーを追加
   - または `/setteam random <人数>` - マグマブロック上のプレイヤーからランダムに鬼を選出
5. `/help` - 準備状況を確認
6. `/startgame` - ゲーム開始

### コマンド一覧

| コマンド | 説明 | 使用方法 | 権限 |
|---------|------|---------|------|
| `/setgamemode` | ゲームモードを設定 | `/setgamemode <fueoni\|onigo>` | OPのみ |
| `/setgametime` | ゲーム時間を設定 | `/setgametime <秒数>` | OPのみ |
| `/setstart` | スタート地点を設定 | `/setstart` | OPのみ |
| `/setteam` | チームにプレイヤーを追加 | `/setteam <oni\|nige> <プレイヤー名>`<br>`/setteam random <人数>` | OPのみ |
| `/startgame` | ゲームを開始 | `/startgame` | OPのみ |
| `/stopgame` | ゲームを強制終了 | `/stopgame` | OPのみ |
| `/help` | ゲーム開始準備状況を確認 | `/help` | 全員 |
| `/returntostart` | スタート地点にテレポート | `/returntostart` | ゲーム中は鬼のみ、未開始時は全員 |

### ゲームモード詳細

#### 鬼ごっこ (onigo)
- 捕まったプレイヤーが鬼になる
- 鬼になったプレイヤーは5秒間スピード効果を付与
- タッチ返し防止（5秒間同じプレイヤーをタッチできない）
- 最後まで逃げ切ったプレイヤーが勝利

#### 増やし鬼 (fueoni)
- 捕まったプレイヤーが鬼チームに加わる
- 鬼はスタート地点にテレポート
- 逃げチームが全滅したら鬼チームの勝利
- 逃げチームが生存していれば逃げチームの勝利

## 開発

### ビルド方法

```bash
./gradlew build
```

### 技術スタック

- Java 17
- Spigot API 1.20.4
- Gradle

### プロジェクト構造

```
src/main/java/com/github/onigokko/
├── Onigokko.java              # メインクラス
├── Event/                     # イベントリスナー
├── commands/                  # コマンド実装
├── games/                     # ゲーム管理
│   ├── mode/                  # ゲームモード実装
│   └── GameModeFactory.java   # ゲームモード生成ファクトリー
└── score/                     # スコア・タイマー管理
```

## ライセンス

現在ライセンス未設定です

## 貢献

バグ報告や機能要望は、[Issues](https://github.com/tomo25neko/Onigokko-MCPlugin/issues) までお願いします。

## 作者

[tomo25neko](https://github.com/tomo25neko)

##謝辞

改善案やフィードバックは、GitHubまたは[とも猫]のいるDiscordまでお寄せください。
