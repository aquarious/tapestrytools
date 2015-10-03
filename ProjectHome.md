# NOTE #

[Light-weight TapestryTools Install Guide](https://code.google.com/p/tapestrytools/wiki/Install_Guide_Lightweight_TapestryTools)

If you are interested in Eclipse WTP based TapestryTools, you can get the relative info [here](http://code.google.com/p/tapestrytools/wiki/Eclipse_WTP_based_TapestryTools), but we will not update this old version TapestryTools.

# Introduction to Lightweight TapestryTools #



# Introduction #


TapestryTools adds lightweight support for the [Apache Tapestry 5](http://tapestry.apache.org/) web
framework. It builds on the Eclipse XML editor, adding context
sensitive content completion ("content assist") for Tapestry
components, custom components, component parameters, and component
properties, as well as easy switching between component templates and
their Java classes.

# Detail Description #

We build an Eclipse WTP based Tapestry 5 visual editor, called it TapestryTools (http://code.google.com/p/tapestrytools/). Now, it can works well on Eclipse Helios/Indigo. But there are still some problems:

  * 1. It is built on Eclipse WTP, too heavyweight, hard to install, users must install all the WTP plugins to run TapestryTools. Many tapestry developers want to use TapestryTools, but failed in its install process. Or some other users just do not want to install such a heavy tool, they need a more smart, more simpler and more efficient tool.

  * 2. Hard to maintain and improve, once Eclipse WTP version changes, we must do some corresponding job to keep synchronous with Eclipse WTP's release version. It is repeated and boring. Eclipse Juno will release soon, it will be huge project if we want to migrate it to the new version.

  * 3. Hard to attract more contributors. There were some users want to improve TapestryTools with me together, but after they know that must start with WTP source code, they gave it up. Eclipse WTP is powerful, meanwhile, its source code is too complex to extend.

After Tapestry committer&PMC Igor Drobiazko suggested, we discussed this problem with Tapestry developers and users in mail list. They indicate that they prefer a brand new simple tool which supplies some special practical and facile Tapestry 5 features, more important, can run without heavyweight complex plugins such as Eclipse WTP.

## New tool features ##

### 1. Easy to install and Easy to upgrade ###

It will be very easy to install the brand new TapestryTools in different Eclipse version, supply good compatibility between different versions. Tapestry developers who use Eclipse Java version can install it without any other plugins.

We will supply Eclipse Marketplace support as well as individual update center. Eclipse with Marketplace client(Such as Eclipse IDE for Java developer) users can install this lightweight TapestryTools from menu Help -> Eclipse Marketplace very easily.

### 2. Tapestry wizards ###

It will still supply Tapestry 5 page and component creation wizard.
![http://tapestrytools.googlecode.com/files/wizard1.png](http://tapestrytools.googlecode.com/files/wizard1.png)

### 3. Convenient switch ###

Tapestry developers really like this feature. They can use command to switch between tml file and corresponding java, It shoule be possible both by menu and short keys, we use "CTRL + R" in PC and "COMMAND + R" in Mac.
![http://tapestrytools.googlecode.com/files/42.png](http://tapestrytools.googlecode.com/files/42.png)

### 4. XML editor based .tml editor ###

As our lightweight TapestryTools does not need Eclipse WTP, we can not use WTP's Web Page Editor any more and have to build another editor for Tapestry's .tml file. In fact, tml file is one special format XML, and many tapestry developers are accustomed to edit .tml file by XML editor, meanwhile XML editor is included in Eclipse Java release. So, we will extend XML editor to supply a brand new TML editor for Tapestry.
![http://tapestrytools.googlecode.com/files/tmleditor.png](http://tapestrytools.googlecode.com/files/tmleditor.png)

### 5. Autocompletion features ###

This light weight TapestryTools will still supply autocompletion features, includes three mainly aspects:

  * Autocomplete of properties from the .java page when editing the .tml file

For example, there is a parameter bound using the @Component annotation inside the class:
```
@Property
private String name;
```

Or such a method in Test.java:
```
public String getName(){
 return "Gavin Lei";
}
```
Then, in Test.tml, once you typed ` ${ `, you can get "name" and "getName" in content assist list.
![http://tapestrytools.googlecode.com/files/autocomplete1.png](http://tapestrytools.googlecode.com/files/autocomplete1.png)
![http://tapestrytools.googlecode.com/files/autocomplete2.png](http://tapestrytools.googlecode.com/files/autocomplete2.png)

  * Tapestry components and corresponding attributes autocomplete feature

If we want to add actionlink component, such as:
```
<t:actionlink t:id="logout">Log out</t:actionlink> 
```
In blank space, pop up content assist, it is "ALT + /" in my Eclipse, all the Tapestry 5 components will be list in content assist, just select "ActionLink", it will insert corresponding code for you.
![http://tapestrytools.googlecode.com/files/autocomplete4.png](http://tapestrytools.googlecode.com/files/autocomplete4.png)


Or when you type
```
<t: 
```
the content assist dialog comes up, you can choose one from the list and then completes it with the required parameters listed.

  * Components attribute value autocomplete feature

Suppose there is an ActionLink component in current .tml file
`<t:actionlink  >delete</t:actionlink>`
We want to add some attribute for this component, place the curson here

```
<t:actionlink
```

Pop up content assist, it will come up with a list of component attributes that we can use here, we can select attribute names in the list.
![http://tapestrytools.googlecode.com/files/autocomplete3.png](http://tapestrytools.googlecode.com/files/autocomplete3.png)

Suppose that we define a pagelink component in Java class file:
```
@Component(parameters = { "page=start" }) 
private PageLink goToStart; 
```

and we want to use it in .tml file:
```
<span t:id="goToStart">Log out</span> 
```
So when we type
```
<span t:id=" 
```
it will also come up with a list of components that we can use here, we can select goToStart component name in the list.

### 6. Custom component support in autocompletion feature ###

Besides Tapestry build in component, custom components will also be in the auto-completion list. You can find the  [design details here](http://code.google.com/p/tapestrytools/wiki/Custom_Components_support_in_TapestryTools).