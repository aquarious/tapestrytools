# Install #

NOTE: This plugin currently supports only Eclipse **Indigo** (3.7) and **Juno (4.2)**, not Helios (3.6).

## Option 1: Using Eclipse Marketplace ##

Click `Help -> Eclipse Marketplace...`, enter `tapestrytools`, then search, and you'll see "TapestryTools". Click `Install`.

![http://tapestrytools.googlecode.com/files/marketplace.png](http://tapestrytools.googlecode.com/files/marketplace.png)

## Option 2: Using Update Site URL ##

Within Eclipse **Indigo/Juno** (the **Eclipse-java** or **eclipse-jee** version is recommended), click `Help -> Install` new software, then enter the following update site URL:

`http://tapestrytools.googlecode.com/svn/TapestryTools/`

Check `TapestryToolsFeature`, then click `Next` to start the installation.

![http://tapestrytools.googlecode.com/files/gsoc_2012_install.png](http://tapestrytools.googlecode.com/files/gsoc_2012_install.png)

  * NOTE: If you're using the Eclipse-sdk version, which has no XML editor installed, be sure to check "Contact all update sites during install to find required software".

# Features Available Now #

## 1. TML editor ##

![http://tapestrytools.googlecode.com/files/gsoc_2012_1.png](http://tapestrytools.googlecode.com/files/gsoc_2012_1.png)

## 2. Content assist for Tapestry 5 components ##

![http://tapestrytools.googlecode.com/files/gsoc_2012_2.png](http://tapestrytools.googlecode.com/files/gsoc_2012_2.png)

## 3. Content assist for Tapestry 5 component attributes ##

![http://tapestrytools.googlecode.com/files/gsoc_2012_3.png](http://tapestrytools.googlecode.com/files/gsoc_2012_3.png)

## 4. Tapestry Template/Java file switching (CTRL + R command) ##

![http://tapestrytools.googlecode.com/files/CTRLR.png](http://tapestrytools.googlecode.com/files/CTRLR.png)

## 5. Content assist for Tapestry class attributes/methods within TML editor ##

![http://tapestrytools.googlecode.com/files/tapestryentities.png](http://tapestrytools.googlecode.com/files/tapestryentities.png)

## 6. Content assist for Message support within TML editor ##

When typing `${message:` and then Cmd+space (Mac) or Ctrl+space (Windows), all the available messages for the page will be suggested in the content assist list.

![http://tapestrytools.googlecode.com/files/message_contentassist.png](http://tapestrytools.googlecode.com/files/message_contentassist.png)

## 7. Custom components support ##

With content assist for components, by default only components defined in the following two packages are in the list:

  * org.apache.tapestry5.corelib.components (Tapestry 5's built-in components)
  * `app-package`.components, where `app-package` is the application's root package as defined in the web.xml (Tapestry web application's root components)

In addition, TapestryTools supports custom components with prefixes other than the usual "t:", allowing you to provide auto-completion for custom components from different libraries. A nice editor is included for entering the mapping. For example:

  * x: org.example.components
  * y: org.acme.lib.components

With the above, each prefix is mapped to a component package. Every component from org.example.components package would have x prefix. For example:

```xml

<span t:type="x/MyComponent">

Unknown end tag for &lt;/span&gt;


<span t:type="y/Foo">

Unknown end tag for &lt;/span&gt;


```

### 7.1 Adding custom components support ###

![http://tapestrytools.googlecode.com/files/customsupport_1.png](http://tapestrytools.googlecode.com/files/customsupport_1.png)

### 7.2 Editing the custom component library mapping ###

![http://tapestrytools.googlecode.com/files/customsupport_2.png](http://tapestrytools.googlecode.com/files/customsupport_2.png)