Xiliary   [![Build Status](https://travis-ci.org/fappel/xiliary.svg?branch=master)](https://travis-ci.org/fappel/xiliary)
=======

<a href="http://with-eclipse.github.io/" target="_blank">
<img alt="with-Eclipse logo" src="http://with-eclipse.github.io/with-eclipse-0.jpg" />
</a>

####Introduction
Auxiliary libraries for Eclipse, JUnit and Java development in general.
It includes the *FlatScrollBar* and more...

 * Released components are available from this p2 software repository: http://fappel.github.io/xiliary
 * Builds from development branch are also available: http://fappel.github.io/xiliary/development


####FlatScrollBar
<img src="http://www.codeaffine.com/wp-content/uploads/2015/01/style-scrollbar.png" width="425" height="207"/>
<br/>
Drop in replacement for all your Eclipse 4.4+ based RCP apps table, tree, and styled text **scrollbars** ...
as easy as this in two tiny steps:
 * add bundles **com.codeaffine.eclipse.swt.jar** and **com.codeaffine.eclipse.ui.swt.theme.jar** to your target platform and to your products feature.xml
 * merge this snippet with your RCP apps CSS:
```
Tree {
  flat-scroll-bar: true;
  flat-scroll-bar-background : #ababab;
  flat-scroll-bar-thumb: #cdcdcd;
  flat-scroll-bar-page-increment: #f8f8f8;
  flat-scroll-bar-thumb-top-level: #454545;
  flat-scroll-bar-page-increment-top-level: #f8f8f8;
  flat-scroll-bar-increment-length: 7;
  adapter-demeanor: expand-on-mouse-over; /* fixed-width */
}

Table {
  flat-scroll-bar: true;
  flat-scroll-bar-background : #ababab;
  flat-scroll-bar-thumb: #cdcdcd;
  flat-scroll-bar-page-increment: #f8f8f8;
  flat-scroll-bar-thumb-top-level: #454545;
  flat-scroll-bar-page-increment-top-level: #f8f8f8;
  flat-scroll-bar-increment-length: 7;
  adapter-demeanor: expand-on-mouse-over; /* fixed-width */
}

StyledText {
  flat-scroll-bar: true;
  flat-scroll-bar-thumb: #ababab;
  flat-scroll-bar-page-increment: #cdcdcd;
  adapter-background-top-level: #f8f8f8;
  flat-scroll-bar-thumb-top-level: #454545;
  flat-scroll-bar-page-increment-top-level: #f8f8f8;
  flat-scroll-bar-increment-length: 7;
  adapter-demeanor: expand-on-mouse-over; /* fixed-width */
}
```
