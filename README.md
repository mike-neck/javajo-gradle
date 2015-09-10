Java女子部 Gradleふんわり入門
===

[Java女子部 Gradleふんわり入門](https://javajo.doorkeeper.jp/events/31185)の資料です。

主に次のレベルの方を想定してGradleの講義・ハンズオンを行います。

* Java SE8の入門レベルの知識のある方
* ビルドって何のこと？っていう方
* プロジェクトの成果物を作成するために手作業をおこなっている方
* Gradleをプロジェクトで利用しているが、何をどう設定するべきなのかわからない方

既にカスタムタスクを書いている方や、プラグインを作成している方には絶対に物足りない内容になります。

開催概要
===

* 日付
  * 2015年9月27日(日)
* 場所
  * [株式会社ALBERT様](http://www.albert2005.co.jp/access/)
    * 東京都新宿区西新宿1-26-2 新宿野村ビル15F
* 開催時刻
  * 13:00〜17:00

前提知識
===

* Java SE8の入門レベルの知識があること
* お好きなIDEを使いこなせていること
  * 講演者はIntelliJ IDEAのサポートは出来ますが、その他のIDEのサポートはできません。
  * また、講演資料のプロジェクトはIntelliJ IDEAに特化して作成されておりますが、eclipseその他のIDEで認識できるかについては自身がありません。
* WindowsあるいはLinuxあるいはMacのターミナルの基本的なコマンドを知っていること
  * `cd`、`ls -la`(Windowsでは`DIR`)、`mkdir`、`mv`(Windows…忘れた(´・ω・｀))など

事前にしてきてほしいこと
===

* このリポジトリーをzipでダウンロードするか、cloneして、ネットワークのつながった状態にて、ルートディレクトリーで次のコマンドを叩いてきてください。
* この作業はGradle-2.7-rc-2をダウンロードしますが、それなりのトラフィックがあり、時間がかかることが想定されます。

**Windows**

```
> gradlew.bat tasks
```

**Mac/Linux**

```
$ gradlew tasks
```

内容
===

1. Gradleについて概要
1. Javaプロジェクトをいじってみる
1. 自分でタスクを作ってみる
1. Copyタスクを扱えるようになる(首尾よく行けばJarタスクまで)