# 1.1 For Eclipse Indigo #

Note that Java 6 is recommended for using WTP at a whole, even though subsets of WTP and other Eclipse Projects might run with other JRE levels. See planned target environments for more information.

### 1.1.1 Install TapestryTools in Eclipse Indigo JavaEE ###

Please [Download Eclipse JavaEE](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/indigor), upzip it on your disk.

Download all the plugins in the folder http://tapestrytools.googlecode.com/svn/TapestryTools_UpdateSite/plugins/ (you can use SVN client to download the whole folder), copy all the jar files in the plugins into Eclipse's plugins folder.

Then you can have a trial of TapestryTools, I have tested it in MacOS and Win 7, it works well both.
![http://tapestrytools.googlecode.com/files/tapestrytools_in_MacOS.png](http://tapestrytools.googlecode.com/files/tapestrytools_in_MacOS.png)

### 1.1.2 Install TapestryTools in Eclipse Indigo Classic ###

Before you install TapestryTools, you should get the following basic tools:

### Must download plugins ###
  * Eclipse SDK v3.7 (Platform, PDE, JDT)

Note: please just use [Eclipse Classic](http://www.eclipse.org/downloads/packages/eclipse-classic-37/indigor), and do **not** use Eclipse Indigo for JavaEE developer, or may be you can not install TapestryTools :-)


  * EMF and XSD SDK Combined EMF and EMF-XSD SDK
  * GEF SDK 3.7 (GEF Code and Source)
  * DTP sdk v1.9 (Code and Source)

### Optional ###
  * EMF Transaction v1.5.0 (Required for JPA Diagram Editor)
  * EMF Validation v1.5.0 (Required for JPA Diagram Editor)
  * Graphiti v0.8.0 (Required for JPA Diagram Editor)

Get [more details (download urls etc) here](http://download.eclipse.org/webtools/downloads/drops/R3.3.0/R-3.3.0-20110607160810/)

You can [download](http://tapestrytools.googlecode.com/files/TapestryTools.rar) video guide about how to install TapestryTools in Eclipse Indigo environment.

# 1.2 For Eclipse Helios #

TapestryTools is built on Eclipse WTP project.Note that WTP requires Java 5 or higher (and, for some things, actually requires a JDK rather than only a JRE) even though many other Eclipse Projects can run with other JRE levels.

Before you install TapestryTools, you should get the following basic tools:

  * Eclipse SDK (Platform, JDT)       [Download](http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.6.2-201102101200/eclipse-SDK-3.6.2-win32.zip)
  * 1> EMF and XSD SDK Combined EMF and EMF-XSD SDK.            [Download](http://www.eclipse.org/downloads/download.php?file=/modeling/emf/emf/downloads/drops/2.6.x/R201009141218/emf-xsd-SDK-2.6.1.zip)
  * 2> GEF SDK 3.6.2 GEF Code and Source      [Download](http://www.eclipse.org/downloads/download.php?file=/tools/gef/downloads/drops/3.6.2/R201102251600/GEF-SDK-3.6.2.zip)
  * 3> DTP sdk v1.8.2 Code and Source      [Download](http://www.eclipse.org/downloads/download.php?file=/datatools/downloads/1.8/dtp-sdk_1.8.2.zip)

Unzip 1, 2 and 3, copy the folders and plugins into Eclipse folder, just override it to install these 3 plugins.


# 2. Install TapestryTools #

Then you can use TapestryTools update site to install TapestryTools. Its update site address is http://tapestrytools.googlecode.com/svn/TapestryTools_UpdateSite/.

TapestryTools for Eclipse Helios is:
http://tapestrytools.googlecode.com/svn/tags/Helios/TapestryTools_UpdateSite/.

# 3. Have a trial of TapestryTools #

  * Tapestry project,page and component creation wizard function  [Guide](http://code.google.com/p/tapestrytools/wiki/Project_Page_Component_Creation_Wizard)

  * Have a trial of TapestryTools components drag-and-drop features[here](http://code.google.com/p/tapestrytools/wiki/Tapestry_support_in_Web_Page_Editor), just like basic html components and jsp components, TapestryTools supply property view for Tapestry components.

  * TapestryTools help you find aim file quickly and help you add Tapestry components quickly by content assist features, check details [here](http://code.google.com/p/tapestrytools/wiki/Partner_location_and_content_assist_helper)