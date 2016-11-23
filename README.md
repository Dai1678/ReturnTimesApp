# ReturnTimesApp
ワンタッチ帰宅連絡アプリ

目的
ボタン一つで現在位置から自宅までの時間を算出して、メールやLINEで連絡する

機能

優先度A:

現在位置取得
自宅の登録　　
現在位置→自宅の経路探索　→　Google Maps API ウェブサービス Directions API(?)
現在位置→自宅の時間算出　→　Google Maps API ウェブサービス Distance Matrix API

優先度B：

自宅場所設定  
ウィジェット対応

優先度C:

LINE連携

進捗
2016/11/22
・プロジェクト作成
・githubリポジトリ作成
・Google Distance Matrix APIを使ったサンプルを拾ったのでコピー。時間算出ボタンを押したときにアプリが落ちる→入力事項がうまく合ってない気がする

ToDo
2016/11/22
・2地点間の移動時間表示
・その他機能の実装方法見積もり

参考
https://developers.google.com/maps/android/?hl=ja Android 向けの Google Maps APIサービス一覧
https://developers.google.com/maps/web-services/?hl=ja Webサービス向けのGoogle Maps APIサービス一覧(Androidでも使える)
https://www.youtube.com/watch?v=tXPEOJaeFm8 Google Distance Matrix APIを使ったサンプルの紹介動画。
https://github.com/vastavch/GoogleMapsDistanceMatrixAPI_Demo ↑の動画のGithub。これを改造していく。
