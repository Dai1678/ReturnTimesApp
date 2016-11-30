# ReturnTimesApp
ワンタッチ帰宅連絡アプリ  

目的  
ボタン一つで現在位置から自宅までの時間を算出して、メールやLINEで連絡する  

機能  

優先度A:  

- 自宅場所設定
- 現在位置取得 →　GPSで現在地取得   
- 現在位置→自宅の時間算出　→　Google Maps API ウェブサービス Distance Matrix API  

優先度B：  

- メール連携  
- 自宅非登録時の例外処理実装  
- 自宅情報保存(SharedPreference)  
- 現在位置→自宅の帰宅方法(車,電車など) → Distance Matrix APIのmodeを変更

優先度C:  

- ウィジェット対応  
- LINE連携  

進捗  
  
2016/11/22  

- プロジェクト作成
- githubリポジトリ作成
- Google Distance Matrix APIを使ったサンプルを拾ったのでコピー。時間算出ボタンを押したときにアプリが落ちる→入力事項がうまく合ってない気がする  

2016/11/23  

- 2点間の距離と所要時間の表示完了(座標、文字列を変数に入れて両方確認済み)  
- 現在地の緯度経度、取得時間、Provider、ネットワーク状態の取得・表示完了　→ <https://github.com/Dai1678/gpsGetter>  

2016/11/24  

- ボタンを押してもエラーがでる → ?

2016/11/28  

- GPSで現在位置の緯度経度取得が完了(LocationLisner)  
- 自宅の緯度経度を入力する設定画面を作成。APIに入れて正常動作を確認。
- xmlの更新  

2016/11/30  

- 自宅非登録時の例外処理実装完了  

ToDo  
  
2016/11/22  

- 2地点間の移動時間表示  
- その他機能の実装方法見積もり  

2016/11/23  

- GPS取得した現在地の緯度経度をGoogle Distance Matrix APIとつなげる。  
- 設定画面作成  
- その他機能の実装方法見積もり  

2016/11/24  

- MainActivityとGpsGetterの同期  
- str_fromにGpsGetterで取得した緯度経度を代入して、APIで正常に処理させる  
- 設定画面作成  
- その他機能の実装方法見積もり  

2016/11/28  

- 自宅非登録時の例外処理実装
- xmlテキストの日本語化
- 自宅情報の入力を住所で行い、アプリ側で緯度経度に変えてAPIに入れる → Google Maps Geocoding API  

2016/11/30  

- パーミッションの処理  

参考  
https://developers.google.com/maps/android/?hl=ja Android 向けの Google Maps APIサービス一覧  　
https://developers.google.com/maps/web-services/?hl=ja Webサービス向けのGoogle Maps APIサービス一覧(Androidでも使える)
https://www.youtube.com/watch?v=tXPEOJaeFm8 Google Distance Matrix APIを使ったサンプルの紹介動画。  
https://github.com/vastavch/GoogleMapsDistanceMatrixAPI_Demo ↑の動画のGithub。これを改造していく。    
<http://tech.admax.ninja/2014/09/16/how-to-get-location-by-android/>　GPS取得の参考サイト    
<http://havoc0214.hatenablog.com/entry/2015/09/07/041956>　↑のサイトの詳細説明    
