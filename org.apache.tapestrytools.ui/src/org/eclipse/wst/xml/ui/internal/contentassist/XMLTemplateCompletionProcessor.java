/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     gavingui2011@gmail.com - Add TapestryTools features
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.ui.internal.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.contentmodel.tapestry.TapestryElementCollection;
import org.eclipse.wst.xml.core.internal.contentmodel.tapestry.travelpackage.CoreComponentsUtil;
import org.eclipse.wst.xml.core.internal.contentmodel.tapestry.travelpackage.TapestryClassLoader;
import org.eclipse.wst.xml.core.internal.contentmodel.tapestry.travelpackage.TapestryCoreComponents;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.eclipse.wst.xml.ui.internal.contentassist.tapestry.TapestryComponentCompletionProposalComputer;
import org.eclipse.wst.xml.ui.internal.contentassist.tapestry.TapestryRootComponentsProposalComputer;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * <p>Completion computer for XML templates</p>
 */
class XMLTemplateCompletionProcessor extends TemplateCompletionProcessor {
	//Represent Tapestry component when try to pop up attributes list in components
	private Node currentTapestryComponent = null;
	private TapestryElementCollection collection = new TapestryElementCollection();
	private HashMap<String, TapestryCoreComponents[]> templateCacheMap = new HashMap<String, TapestryCoreComponents[]>();
	private TapestryClassLoader tapestryClassLoader = new TapestryClassLoader();
	private TapestryRootComponentsProposalComputer tapestryRootComponentsProposalComputer = new TapestryRootComponentsProposalComputer();
	private static final class ProposalComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return ((TemplateProposal) o2).getRelevance() - ((TemplateProposal) o1).getRelevance();
		}
	}

	private static final Comparator fgProposalComparator = new ProposalComparator();
	private String fContextTypeId = null;

	/**
	 * This method is used to generate content assit for:
	 * 1. blank space to list Tapestry components
	 * 2. input '<' to list Tapestry components
	 * 3. int '<t:' to list Tapestry components
	 * 4. list attributes for Tapestry component
	 * 5. list attributes values
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {

		ITextSelection selection = (ITextSelection) viewer.getSelectionProvider().getSelection();

		// adjust offset to end of normalized selection
		if (selection.getOffset() == offset) {
			offset = selection.getOffset() + selection.getLength();
		}

		String prefix = extractPrefix(viewer, offset);
		Region region = new Region(offset - prefix.length(), prefix.length());
		TemplateContext context = createContext(viewer, region, offset);
		if (context == null) {
			return new ICompletionProposal[0];
		}

		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(viewer, offset);

		currentTapestryComponent = (Node) treeNode;	
		char preChar=0,preChar2=0;
		//In situation user input <, we should store the char before cursor into preChar and even preChar2
		if(currentTapestryComponent.getNodeValue() != null){
			for(int i=offset-treeNode.getStartOffset()-1; i>=0; i--){
				char temp = currentTapestryComponent.getNodeValue().charAt(i);
				if(temp != 9 && temp != 10 && temp != 32){
					if(preChar == 0)
						preChar = temp;
					else{
						preChar2 = temp;
						break;
					}
				}
			}
		}
		
		while ((currentTapestryComponent != null) && (currentTapestryComponent.getNodeType() == Node.TEXT_NODE) && (currentTapestryComponent.getParentNode() != null)) {
			currentTapestryComponent = currentTapestryComponent.getParentNode();
		}

		// name of the selection variables {line, word}_selection
		context.setVariable("selection", selection.getText()); //$NON-NLS-1$
		
		System.out.println(">>>>> Get template list by context id:" + context.getContextType().getId() + "  selection:" + selection.getText());
		
		Template[] templates = getTemplates((IDOMNode) treeNode, offset, context.getContextType().getId(), preChar, preChar2);

		List matches = new ArrayList();
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			try {
				context.getContextType().validate(template.getPattern());
			}
			catch (TemplateException e) {
				continue;
			}
			if (template.matches(prefix, context.getContextType().getId())) {
				int place = getRelevance(template, prefix);
				matches.add(createProposal(template, context, (IRegion) region, place));
			}
		}

		Collections.sort(matches, fgProposalComparator);

		return (ICompletionProposal[]) matches.toArray(new ICompletionProposal[matches.size()]);
	}

	/**
	 * Creates a concrete template context for the given region in the
	 * document. This involves finding out which context type is valid at the
	 * given location, and then creating a context of this type. The default
	 * implementation returns a <code>SmartReplaceTemplateContext</code> for
	 * the context type at the given location. This takes the offset at which
	 * content assist was invoked into consideration.
	 * 
	 * @param viewer
	 *            the viewer for which the context is created
	 * @param region
	 *            the region into <code>document</code> for which the
	 *            context is created
	 * @param offset
	 *            the original offset where content assist was invoked
	 * @return a template context that can handle template insertion at the
	 *         given location, or <code>null</code>
	 */
	private TemplateContext createContext(ITextViewer viewer, IRegion region, int offset) {
		// pretty much same code as super.createContext except create
		// SmartReplaceTemplateContext
		TemplateContextType contextType = getContextType(viewer, region);
		if (contextType != null) {
			IDocument document = viewer.getDocument();
			return new ReplaceNameTemplateContext(contextType, document, region.getOffset(), region.getLength(), offset);
		}
		return null;
	}

	protected ICompletionProposal createProposal(Template template, TemplateContext context, IRegion region, int relevance) {
		return new CustomTemplateProposal(template, context, region, getImage(template), relevance);
	}

	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		TemplateContextType type = null;

		ContextTypeRegistry registry = getTemplateContextRegistry();
		if (registry != null) {
			type = registry.getContextType(fContextTypeId);
		}

		return type;
	}

	protected Image getImage(Template template) {
		if(template.getContextTypeId().equals(TapestryElementCollection.componentsContextTypeId))
			return XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_TAPESTRY_DEFAULT);
		else if(template.getContextTypeId().equals(TapestryElementCollection.attributesContextTypeId))
			return XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_TAPESTRY_ATTRIBUTE);
		else if(template.getContextTypeId().equals(TapestryElementCollection.entitiesContextTypeId))
			return XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_TAPESTRY_ENTITY);
		else if(template.getContextTypeId().equals(TapestryElementCollection.attributesValueContextTypeId))
			return XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_TAPESTRY_DEFAULT);
		else 
			return XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_TAG_MACRO);
	}

	private ContextTypeRegistry getTemplateContextRegistry() {
		return XMLUIPlugin.getDefault().getTemplateContextRegistry();
	}

	/**
	 * This method does not used, just implement abstract method
	 */
	protected Template[] getTemplates(String contextTypeId) {
		
		return null;
	}
	
	private IProject getCurrentProject(){
		IEditorPart editorPart = Workbench.getInstance()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
		IFile file = input.getFile();
		return file.getProject();
	}
	
	private PackageFragment getTapestryCoreLibrary() {
		IPackageFragmentRoot root = tapestryClassLoader.getTapestryCoreJar(getCurrentProject());
		if(root == null)
			return null;
		IPackageFragment pack = root.getPackageFragment("org.apache.tapestry5.corelib.components");
		if(pack != null && pack instanceof PackageFragment)
			return (PackageFragment) pack;
		return null;
	}
	
	/**
	 * Get templates entrance method
	 * 
	 * @param node
	 * @param offset
	 * @param contextTypeId
	 * @param preChar
	 * @param preChar2
	 * @return
	 */
	protected Template[] getTemplates(IDOMNode node, int offset, String contextTypeId, char preChar, char preChar2) {
		IProject project = getCurrentProject();
		String mapKey = project.getName();
		TapestryCoreComponents[] coreList = this.templateCacheMap.get(mapKey);
		if(coreList == null || coreList.length <= 0){
			//Get tapestry components from classpath
			List<TapestryCoreComponents> list = new ArrayList<TapestryCoreComponents>();
			PackageFragment tapestryCorePackage = getTapestryCoreLibrary();
			try {
				if(tapestryCorePackage !=null)
					for(Object packo : tapestryCorePackage.getChildrenOfType(IJavaElement.CLASS_FILE)){
						ClassFile packi = (ClassFile) packo;
						if(packi.getElementName().indexOf('$') < 0){
							TapestryCoreComponents component = tapestryClassLoader.loadComponentAttributesFromClassFile(tapestryClassLoader.getTapestryCoreJar(project), "t", packi);
							if(component != null)
								list.add(component);
						}
					}
			} catch (JavaModelException e) {
				e.printStackTrace();
			} catch (ClassFormatException e) {
				e.printStackTrace();
			}
			if(list != null && list.size() > 0){
				coreList = list.toArray(new TapestryCoreComponents[0]);
				this.templateCacheMap.put(mapKey, coreList);
			}
		}
		
		if(coreList == null)
			return new Template[0];
		
		if(contextTypeId.equals(TapestryElementCollection.componentsContextTypeId) ){			
			boolean customComponent = false;
			int type = 1;
			if(preChar == '<')
				type = 2;
			else if(currentTapestryComponent.getPrefix() != null){
				customComponent = tapestryRootComponentsProposalComputer.getComponentsPrefixList(this.getCurrentProject()).contains(currentTapestryComponent.getPrefix());
				if(customComponent)
					type = 3;
			}
			List<Template> components = new ArrayList<Template>();
			if(currentTapestryComponent.getPrefix() != null && !customComponent)
				return components.toArray(new Template[0]);
			
			if(type!=3 || currentTapestryComponent.getNodeName().equals("t:")){
				List<Template> buildInList = CoreComponentsUtil.buildTemplateListFromComponents(coreList, contextTypeId, type);
				if(buildInList != null && buildInList.size() > 0)
					components.addAll(buildInList);
				List<Template> rootComponents = tapestryRootComponentsProposalComputer.getRootComponentsTemplates(this.getCurrentProject(), contextTypeId, type);
				if(rootComponents != null && rootComponents.size() > 0)
					components.addAll(rootComponents);
			}
			
			if(currentTapestryComponent.getPrefix() == null || (currentTapestryComponent.getPrefix() + ":").equals(currentTapestryComponent.getNodeName())){
				List<Template> customComponents = tapestryRootComponentsProposalComputer.getCustomComponentsTemplates(this.getCurrentProject(), contextTypeId, type, currentTapestryComponent.getPrefix());
				if(customComponents != null && customComponents.size() > 0)
					components.addAll(customComponents);
			}
			
			return components == null ? null : components.toArray(new Template[0]);
		}else if(contextTypeId.equals(TapestryElementCollection.attributesContextTypeId)){
			String tapestryComponentName = getTapestryComponentName(node);
			//In condition <t:ActionLink
			if(tapestryComponentName == null)
				tapestryComponentName = currentTapestryComponent.getNodeName().toLowerCase();
			//In condition <t:html.Message
			if(tapestryComponentName.indexOf('.') > -1 && currentTapestryComponent.getPrefix()!= null && currentTapestryComponent.getPrefix().equals("t"))
				tapestryComponentName = tapestryComponentName.substring(2).replace('.', ':');
				
			List<Template> tapestryTemplates = CoreComponentsUtil.getAttributeList(coreList, contextTypeId, tapestryComponentName);
			if(tapestryTemplates == null || tapestryTemplates.size() ==0)
				tapestryTemplates = tapestryRootComponentsProposalComputer.getRootComponentsAttributes(project, contextTypeId, tapestryComponentName);
			if(tapestryTemplates == null || tapestryTemplates.size() ==0)
				tapestryTemplates = tapestryRootComponentsProposalComputer.getCustomComponentsAttributes(project, contextTypeId, tapestryComponentName, tapestryClassLoader);
			
			return tapestryTemplates == null ? null : tapestryTemplates.toArray(new Template[0]);
		}else if(contextTypeId.equals(TapestryElementCollection.attributesValueContextTypeId)){
			List<Template> tapestryTemplates = null;
			if(isComponentTypeContentAssist(node, offset)){
				tapestryTemplates = CoreComponentsUtil.getTapestryComponentNameList(coreList, contextTypeId);
				List<Template> rootComponents = tapestryRootComponentsProposalComputer.getRootComponentsNameTemplates(this.getCurrentProject(), contextTypeId);
				if(rootComponents != null && rootComponents.size() > 0)
					tapestryTemplates.addAll(rootComponents);
				List<Template> customComponents = tapestryRootComponentsProposalComputer.getCustomComponentsNameTemplates(this.getCurrentProject(), contextTypeId);
				if(customComponents != null && customComponents.size() > 0)
					tapestryTemplates.addAll(customComponents);
			}else if(isComponentContentassist(node, offset)){
				tapestryTemplates = TapestryComponentCompletionProposalComputer.getInstance().computeCompletionProposals("", node, offset);
			}else{
				tapestryTemplates = collection.getAttributeValueList(contextTypeId, currentTapestryComponent);
			}
			return tapestryTemplates == null ? null : tapestryTemplates.toArray(new Template[0]);
		}
		else{
			Template templates[] = null;
			TemplateStore store = getTemplateStore();
			if (store != null) {
				templates = store.getTemplates(contextTypeId);
			}

			return templates;
		}
	}
	
	/**
	 * Get component name such as "ActionLink" in this condition <a t:type="ActionLink"></a>
	 * 
	 * @param node
	 * @return
	 */
	private String getTapestryComponentName(IDOMNode node){
		NamedNodeMap attributes = node.getAttributes();
		if(attributes != null && attributes.getLength() > 0){
			for(int i=0; i<attributes.getLength(); i++){
				if(attributes.item(i).getNodeName().equals("t:type")){
					String value = attributes.item(i).getNodeValue();
					if(value.indexOf('/') > -1){
						return value.replace('/', ':').toLowerCase();
					}else
						return "t:" + value.toLowerCase();
				}
			}
		}
		return null;
	}
	
	//Decide whether in condition <span t:id="
	private boolean isComponentContentassist(IDOMNode node, int offset){
		int sp=0, ep=0;
		for (int i = offset - node.getStartOffset() - 1; i >= 0; i--) {
			char temp = node.getSource().charAt(i);
			if(ep ==0 && temp == '=')
				ep = i;
			else if(sp ==0 && temp == ' ')
				sp = i;
			if(sp != 0 && ep != 0)
				break;
		}
		if(sp == 0 || ep == 0)
			return false;
		else{
			if(node.getSource().substring(sp, ep).trim().equals("t:id"))
				return true;
			else
				return false;
		}
	}
	
	//Decide whether in condition <a t:type=""></a>
	private boolean isComponentTypeContentAssist(IDOMNode node, int offset){
		int sp=0, ep=0;
		for (int i = offset - node.getStartOffset() - 1; i >= 0; i--) {
			char temp = node.getSource().charAt(i);
			if(ep ==0 && temp == '=')
				ep = i;
			else if(sp ==0 && temp == ' ')
				sp = i;
			if(sp != 0 && ep != 0)
				break;
		}
		if(sp == 0 || ep == 0)
			return false;
		else{
			if(node.getSource().substring(sp, ep).trim().equals("t:type"))
				return true;
			else
				return false;
		}
	}

	private TemplateStore getTemplateStore() {
		return XMLUIPlugin.getDefault().getTemplateStore();
	}

	void setContextType(String contextTypeId) {
		fContextTypeId = contextTypeId;
	}


}
