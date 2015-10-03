

# Abstract #

[Apache Tapestry](http://tapestry.apache.org/) supplies powerful component structure, and it offered a number of other powerful features that proved to be critical in our work. It allowed a clean separation between Java and HTML, make it possible for the design work on the application to continue well after the code had been completed, and becoming more and more popular today. This project aims to build a lightweight Tapestry visual editor on Eclipse XML editor.
# Detail Description #

We build an Eclipse WTP based Tapestry 5 visual editor as GSoC 2011 project, called it TapestryTools (http://code.google.com/p/tapestrytools/). Now, it can works well on Eclipse Helios/Indigo. But there are still some problems:

  * 1. It is built on Eclipse WTP, too heavyweight, hard to install, users must install all the WTP plugins to run TapestryTools. Many tapestry developers want to use TapestryTools, but failed in its install process. Or some other users just do not want to install such a heavy tool, they need a more smart, more simpler and more efficient tool.

  * 2. Hard to maintain and improve, once Eclipse WTP version changes, we must do some corresponding job to keep synchronous with Eclipse WTP's release version. It is repeated and boring. Eclipse Juno will release soon, it will be huge project if we want to migrate it to the new version.

  * 3. Hard to attract more contributors. There were some users want to improve TapestryTools with me together, but after they know that must start with WTP source code, they gave it up. Eclipse WTP is powerful, meanwhile, its source code is too complex to extend.

After my GSoC mentor Igor Drobiazko suggested, we discussed this problem with Tapestry developers and users in mail list. They indicate that they prefer a brand new simple tool which supplies some special practical and facile Tapestry 5 features, more important, can run without heavyweight complex plugins such as Eclipse WTP. This is the reason why this project proposal was born.

Here, thanks for all the help and advises from Tapestry community, users, developers and Tapestry committers even PMCs.

_Link 1:http://mail-archives.apache.org/mod_mbox/tapestry-users/201202.mbox/browser_

_Link 2:http://mail-archives.apache.org/mod_mbox/tapestry-dev/201202.mbox/browser_

# New Lightweight TapestryTool Design #

The brand new lightweight TapestryTools will be just a normal Eclipse plug-in, does not need many special dependencies, more focus, easy to install, easy to use. And also, we can supply practical and welcomed features:

## Project Feature List ##

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

## Optional Features ##

If we have enough time in GSoC period, we also plan to supply these features:

1. Add another Page/Component class content tab in TML editor, users can use shortcut to switch between class tab and template tab.

2. Import Tapestry jar of special version to project

3. Validation function in Tapestry .tml file source view
> Supplies semantic validation of the Tapestry standard tag libraries for both EL and non-EL attribute values in .tml file

4. Hyperlink function in Tapestry .tml file source view
> Hyperlink to the Java editor from property and method referenced in the Expression Language(EL) of a tag-attribute, click (CTRL + property or method ), it will jump to the corresponding Java Classfile.

# Project Current Status #

## Development progress and source code ##
We have started development job already, and finished basic auto-complete feature for TML editor(see Must Have feature 5 above) such as Tapestry built in components, component attrubites and host code in [Google code http://code.google.com/p/tapestrytools/](http://code.google.com/p/tapestrytools/)
If you are interested, you can check out the source code, and have a trial of it.

  * Development Environment: Eclipse Indigo for Java developer(without any other plugins)

## Install trial version ##
I have finished some basic features and create Eclipse update site for this lightweight TapestryTools, you can install it to have a trial following [install guide here](http://code.google.com/p/tapestrytools/wiki/Install_Guide_Lightweight_TapestryTools).

![http://tapestrytools.googlecode.com/files/gsoc_2012_install.png](http://tapestrytools.googlecode.com/files/gsoc_2012_install.png)

# Additional Info #

## Time Schedule ##
  * Before March 26: Discuss and gather demand, features in Tapestry Dev & User mail list. Read XML editor's source code and start survey and some basic coding job to confirm which features we can reach in the coming 3-4 three months.
  * March 26 - April 6: Submitting the project proposal
  * April 24 - May 22:Community Bonding Period (April 26 - May 22): Get to know mentors, read documentation, and prepare development environment.
  * May 23 - Jun 8: Finish Tapestry page/component creation wizard function
  * Jun 9 - Jun 16: Finish change-over function between Tapestry page's .tml file and .java file
  * Jun 17 - Jul 2: Finish basic extend job for XML editor based TML editor
  * Jul 3 - Jul 31: Finish all the autocompletion features in TML editor
  * Aug 1 - Aug 14: Finish custom component support in autocompletion feature
  * Aug 15 - Aug 25: Develop time for optional features. Scrub code, write tests, improve documentation and submit all my work

## Something about me ##

I have been working with both Eclipse and Tapestry for years, have much Eclipse plug-in development experience. Since last year, i discussed with Tapestry community much, this project proposal comes from Tapestry community's demand. I gather feedback and advises from mail list since February, keep improving feature list then get this final proposal. Thanks for self-giving open source developers and open source spirit.

My name is Gavin Lei, master student of University of Science and Technology Beijing. My major is computer scienece and technology, I am familar with Eclipse,Java and some other open source projects,such as struts,spring,jstl,dojo and so on and have several years of Java development experience.I am familiar with many project in Apache Foundation,Eclipse Foundation as well as Dojo Foundation.