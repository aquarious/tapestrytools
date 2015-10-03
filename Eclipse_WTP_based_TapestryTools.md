# Support Eclipse Version #

> ## Helios ##
  * SVN workspace: https://tapestrytools.googlecode.com/svn/tags/Helios

  * Update center URL: https://tapestrytools.googlecode.com/svn/tags/Helios/TapestryTools_UpdateSite

> We will focus on Indigo, and will not update TapestryTools for Eclipse Helios in the future.

> ## Indigo ##

  * SVN workspace: https://tapestrytools.googlecode.com/svn/trunk
  * SVN update center URL: http://tapestrytools.googlecode.com/svn/TapestryTools_UpdateSite


You can find the video guide about how to use TapestryTools [here](http://tapestrytools.googlecode.com/files/tapestrytools.mov)

> # Features #
  * 1. Tapertry application creation wizard

in this wizard, it will ask the user to provide the root package for the application when he clicks "finish", you create an empty application with specified root package + subpackages pages, components, services and also it will generate AppModule and web.xml. We can select add necessary Tapestry 5 relative jars into our project or not in this wizard.

  * 2. Tapestry's built in Live Class Reloading support

It should be possible to change Java code, template files, properties etc, and see the results without to restart the web server. In the past there were some problems with WTP and Live Class Reloading(click here to check details about Class Reloading feature and its importance) , our project will fix this problem.

  * 3. Tapestry page/component creation wizard

It will supply a wizard helping us to create the .tml file and .java file together for a Tapestry page/component.

This one asks the user after the page name and generates java class and template,whereby the user should be able to provide a default settings where template goes to. Note that for page template there are 2 possibilities: in web app context and classpath, for components only classpath is possible.

  * 4. Add Tapestry component drap-and-drop feature support for .tml file editor

If we open a Tapestry page/component with WTP Web Page Editor, its palette will contains three main section, HTML,Tapestry core and Tapestry custom component etc. Then, we can drap-and-drop the components in the palette to the canvas. It should also be possible to drag and component from any Tapestry-based library, not only Tapestry core components. Every libray should have its own section in the palette.

WTP web editor supplies property view for us already, we can improve the property to support Tapestry component. In Tapestry Core section, it includes Tapestry standard components, CheckBox, DateField,Form,Label, Radio and Select components and all the other standard components.

NOTE Eclipse WTP supplies relative interface for us to change palette components, view the design details figure here.

  * 5. Add convenient way for the Web Page Editor to change-over between a Tapestry page's .tml file and .java file

This should be possible both: menu and short keys

  * 6. Add Tapestry built in and custom components autocomplete function for WTP Web Page Editor's source view



If we add actionlink component, such as:
```
<t:actionlink t:id="logout">Log out</t:actionlink> 
```
,So when you type
```
<t: 
```
it comes up with a list you can choose from and then completes it with the required parameters listed.



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


  * 7.Tapestry component's parameters property view

We select a component in canvas design view or source view, the Eclipse property view will list this component's parameters and we can edit the parameters here. And it will also display parameter description here.

  * 8.Autocomplete of properties from the .java page when editing the .tml file

In the Source Page of the Web Page Editor, add the Tapestry tag, (for example ${prop:index}). With the cursor inside the brackets, hit Ctrl+spacebar, should see a pop-up with a list of all the available properties defined in the corresponding java class. Check details here.

> # Optional features #
  * 9. Validation function in Tapestry .tml file source view

Beyond the basic JSP validation already provided with the JSP editor, this editor supplies semantic validation of the Tapestry standard tag libraries for both EL and non-EL attribute values.

  * 10. Hyperlink function in Tapestry .tml file source view

Hyperlink to the Java editor from  property and  method referenced in the Expression Language(EL) of a tag-attribute, click (CTRL + property or method ), it will jump to the corresponding Java Class file.