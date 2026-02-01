# 共通家計簿アプリ
## Android Studioの使用方法
### 設定
File→settings(ctrl+alt+s)

### Android端末の操作
#### 接続方法
https://developer.android.com/studio/run/device?hl=ja
1. スマホを開発者モードにする  
   デバイス情報→ビルド番号を5回クリック  
2. Android StudioのデバイスマネージャーからPhysicalを選択しPair using Wi-Fiを選択
3. スマホの開発者向けオプション→ワイヤレスデバッグをONにし、「QRコードによるデバイスのペア設定」をクリックし、QRコードを読み込む  

***トラブルシューティング***  
PCのネットワークプロファイルがパブリックではなく、プライベートになっていることを確認。

コマンドで接続する場合は以下  
https://laplace-daemon.com/android-device-not-detected-in-android-studio/

#### ファイル
・DB
data/data/com.example.commonaccountsystem/databases

#### アプリのデプロイ
スマホを指定して実行すると同じ名前のアプリがあるためエラーになる。  
その後、自動的にアプリをアンインストールし、インストールしてくれる。  

## DBのテーブル定義を変更した際のエラー解消法
エラー例
```agsl
java.lang.IllegalStateException: Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: 65b8f1ed4f8d44779a7725785a56ca04, found: ac46c404bde654dfe3fbbc58be6a674d
```
### 手順
1. DBのバックアップから`room_master_table`を消去する。(データではなく、テーブルを削除)
2. デバイスのフォルダからDBを削除する。必要ならcacheも削除しておく。
3. src/main/assets 配下に新規DBを配置する。
4. 何度か試行するときにcacheが効いてしまうことがあるので、`AppDatabase`の26~28行目をアンコメントし、コードのテストをする。

## MPAndroidChartの参考サイト
https://qiita.com/c60evaporator/items/14e63d22d860b73e6f22
