/**
 * Created on May 10, 2005
 */
package org.alfresco.repo.node.operations.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.content.ContentReader;
import org.alfresco.repo.content.ContentService;
import org.alfresco.repo.content.ContentWriter;
import org.alfresco.repo.dictionary.NamespaceService;
import org.alfresco.repo.dictionary.PropertyTypeDefinition;
import org.alfresco.repo.dictionary.impl.DictionaryBootstrap;
import org.alfresco.repo.dictionary.impl.DictionaryDAO;
import org.alfresco.repo.dictionary.impl.M2Aspect;
import org.alfresco.repo.dictionary.impl.M2Association;
import org.alfresco.repo.dictionary.impl.M2ChildAssociation;
import org.alfresco.repo.dictionary.impl.M2Model;
import org.alfresco.repo.dictionary.impl.M2Property;
import org.alfresco.repo.dictionary.impl.M2Type;
import org.alfresco.repo.node.NodeService;
import org.alfresco.repo.node.operations.NodeOperationsService;
import org.alfresco.repo.ref.ChildAssocRef;
import org.alfresco.repo.ref.NodeAssocRef;
import org.alfresco.repo.ref.NodeRef;
import org.alfresco.repo.ref.QName;
import org.alfresco.repo.ref.StoreRef;
import org.alfresco.util.BaseSpringTest;
import org.alfresco.util.debug.NodeStoreInspector;

/**
 * Node operations service unit tests
 * 
 * @author Roy Wetherall
 */
public class NodeOperationsServiceImplTest extends BaseSpringTest 
{
	/**
	 * Services used by the tests
	 */
	private NodeService nodeService;
	private NodeOperationsService nodeOperationsService;
	private DictionaryDAO dictionaryDAO;
	private ContentService contentService;
	
	/**
	 * Data used by the tests
	 */
	private StoreRef storeRef;
	private NodeRef sourceNodeRef;	
	private NodeRef rootNodeRef;	
	private NodeRef targetNodeRef;
	private NodeRef nonPrimaryChildNodeRef;
	private NodeRef childNodeRef;
	private NodeRef destinationNodeRef;
	
	/**
	 * Types and properties used by the tests
	 */
	private static final String TEST_TYPE_NAMESPACE = "testTypeNamespaceURI";
	private static final QName TEST_TYPE_QNAME = QName.createQName(TEST_TYPE_NAMESPACE, "testType");
	private static final QName PROP1_QNAME_MANDATORY = QName.createQName(TEST_TYPE_NAMESPACE, "prop1Mandatory");
	private static final QName PROP2_QNAME_OPTIONAL = QName.createQName(TEST_TYPE_NAMESPACE, "prop2Optional");
	
	private static final QName TEST_ASPECT_QNAME = QName.createQName(TEST_TYPE_NAMESPACE, "testAspect");
	private static final QName PROP3_QNAME_MANDATORY = QName.createQName(TEST_TYPE_NAMESPACE, "prop3Mandatory");
	private static final QName PROP4_QNAME_OPTIONAL = QName.createQName(TEST_TYPE_NAMESPACE, "prop4Optional");
	
    private static final QName TEST_MANDATORY_ASPECT_QNAME = QName.createQName(TEST_TYPE_NAMESPACE, "testMandatoryAspect");
    private static final QName PROP5_QNAME_MANDATORY = QName.createQName(TEST_TYPE_NAMESPACE, "prop5Mandatory");
    
	private static final String TEST_VALUE_1 = "testValue1";
	private static final String TEST_VALUE_2 = "testValue2";
    private static final String TEST_VALUE_3 = "testValue3";
	
	private static final QName TEST_CHILD_ASSOC_QNAME = QName.createQName(TEST_TYPE_NAMESPACE, "testChildAssocName");
	private static final QName TEST_ASSOC_QNAME = QName.createQName(TEST_TYPE_NAMESPACE, "testAssocName");
	private static final QName TEST_CHILD_ASSOC_QNAME2 = QName.createQName(TEST_TYPE_NAMESPACE, "testChildAssocName2");
	
	/**
	 * Test content
	 */
	private static final String SOME_CONTENT = "This is some content ...";		
	
	/**
	 * Sets the meta model DAO
	 * 
	 * @param dictionaryDAO  the meta model DAO
	 */
	public void setDictionaryDAO(DictionaryDAO dictionaryDAO)
    {
        this.dictionaryDAO = dictionaryDAO;
    }
	
	/**
	 * On setup in transaction implementation
	 */
	@Override
	protected void onSetUpInTransaction() 
		throws Exception 
	{
		// Set the services
		this.nodeService = (NodeService)this.applicationContext.getBean("dbNodeService");
		this.nodeOperationsService = (NodeOperationsService)this.applicationContext.getBean("nodeOperationsService");
		this.contentService = (ContentService)this.applicationContext.getBean("contentService");
		
		// Create the test model
		createTestModel();
		
		// Create the store and get the root node reference
		this.storeRef = this.nodeService.createStore(StoreRef.PROTOCOL_WORKSPACE, "Test_" + System.currentTimeMillis());
		this.rootNodeRef = this.nodeService.getRootNode(storeRef);
		
		// Create the node used for copying
		ChildAssocRef childAssocRef = this.nodeService.createNode(
				rootNodeRef,
				null,
				QName.createQName("{test}test"),
				TEST_TYPE_QNAME,
				createTypePropertyBag());
		this.sourceNodeRef = childAssocRef.getChildRef();
		
		// Create another bag of properties
		Map<QName, Serializable> aspectProperties = new HashMap<QName, Serializable>();
		aspectProperties.put(PROP3_QNAME_MANDATORY, TEST_VALUE_1);
		aspectProperties.put(PROP4_QNAME_OPTIONAL, TEST_VALUE_2);
		
		// Apply the test aspect
		this.nodeService.addAspect(
				this.sourceNodeRef, 
				TEST_ASPECT_QNAME, 
				aspectProperties);
		
		// Add a child
		ChildAssocRef temp3 =this.nodeService.createNode(
				this.sourceNodeRef, 
				null, 
				TEST_CHILD_ASSOC_QNAME, 
				TEST_TYPE_QNAME, 
				createTypePropertyBag());
		this.childNodeRef = temp3.getChildRef();
		
		// Add a child that is primary
		ChildAssocRef temp2 = this.nodeService.createNode(
				rootNodeRef,
				null,
				QName.createQName("{test}testNonPrimaryChild"),
				TEST_TYPE_QNAME,
				createTypePropertyBag());
		this.nonPrimaryChildNodeRef = temp2.getChildRef();
		this.nodeService.addChild(this.sourceNodeRef, this.nonPrimaryChildNodeRef, TEST_CHILD_ASSOC_QNAME2);
		
		// Add a target assoc
		ChildAssocRef temp = this.nodeService.createNode(
				rootNodeRef,
				null,
				QName.createQName("{test}testAssoc"),
				TEST_TYPE_QNAME,
				createTypePropertyBag());
		this.targetNodeRef = temp.getChildRef();
		this.nodeService.createAssociation(this.sourceNodeRef, this.targetNodeRef, TEST_ASSOC_QNAME);
		
		// Create a node we can use as the destination in a copy
		Map<QName, Serializable> destinationProps = new HashMap<QName, Serializable>();
		destinationProps.put(PROP1_QNAME_MANDATORY, TEST_VALUE_1);			
        destinationProps.put(PROP5_QNAME_MANDATORY, TEST_VALUE_3); 
        destinationProps.put(DictionaryBootstrap.PROP_QNAME_MIME_TYPE, "text/plain");
        destinationProps.put(DictionaryBootstrap.PROP_QNAME_ENCODING, "UTF-8");
		ChildAssocRef temp5 = this.nodeService.createNode(
				this.rootNodeRef,
				null,
				QName.createQName("{test}testDestinationNode"),
				TEST_TYPE_QNAME,
				destinationProps);
		this.destinationNodeRef = temp5.getChildRef();
	}
	
	/**
	 * Helper method that creates a bag of properties for the test type
	 * 
	 * @return  bag of properties
	 */
	private Map<QName, Serializable> createTypePropertyBag()
	{
		Map<QName, Serializable> result = new HashMap<QName, Serializable>();
		result.put(PROP1_QNAME_MANDATORY, TEST_VALUE_1);
		result.put(PROP2_QNAME_OPTIONAL, TEST_VALUE_2);
        result.put(PROP5_QNAME_MANDATORY, TEST_VALUE_3);
        result.put(DictionaryBootstrap.PROP_QNAME_MIME_TYPE, "text/plain");
        result.put(DictionaryBootstrap.PROP_QNAME_ENCODING, "UTF-8");
		return result;
	}
	
	/**
	 * Creates the test model used by the tests
	 */
	private void createTestModel()
	{
        M2Model model = M2Model.createModel("test:nodeoperations");
        model.createNamespace(TEST_TYPE_NAMESPACE, "test");
        model.createImport(NamespaceService.ALFRESCO_DICTIONARY_URI, NamespaceService.ALFRESCO_DICTIONARY_PREFIX);
        model.createImport(NamespaceService.ALFRESCO_URI, NamespaceService.ALFRESCO_PREFIX);

        M2Type testType = model.createType("test:" + TEST_TYPE_QNAME.getLocalName());
        testType.setParentName("alf:" + DictionaryBootstrap.TYPE_QNAME_CONTENT.getLocalName());
        
        M2Property prop1 = testType.createProperty("test:" + PROP1_QNAME_MANDATORY.getLocalName());
        prop1.setMandatory(true);
        prop1.setType("d:" + PropertyTypeDefinition.TEXT.getLocalName());
        prop1.setMultiValued(false);
        
		M2Property prop2 = testType.createProperty("test:" + PROP2_QNAME_OPTIONAL.getLocalName());
		prop2.setMandatory(false);
        prop2.setType("d:" + PropertyTypeDefinition.TEXT.getLocalName());
		prop2.setMandatory(false);
		
		M2ChildAssociation childAssoc = testType.createChildAssociation("test:" + TEST_CHILD_ASSOC_QNAME.getLocalName());
        childAssoc.setTargetClassName("alf:base");
		childAssoc.setTargetMandatory(false);
		
		M2ChildAssociation childAssoc2 = testType.createChildAssociation("test:" + TEST_CHILD_ASSOC_QNAME2.getLocalName());
        childAssoc2.setTargetClassName("alf:base");
		childAssoc2.setTargetMandatory(false);
		
		M2Association assoc = testType.createAssociation("test:" + TEST_ASSOC_QNAME.getLocalName());
        assoc.setTargetClassName("alf:base");
		assoc.setTargetMandatory(false);
		
		M2Aspect testAspect = model.createAspect("test:" + TEST_ASPECT_QNAME.getLocalName());
		
		M2Property prop3 = testAspect.createProperty("test:" + PROP3_QNAME_MANDATORY.getLocalName());
		prop3.setMandatory(true);
        prop3.setType("d:" + PropertyTypeDefinition.TEXT.getLocalName());
		prop3.setMultiValued(false);
		
		M2Property prop4 = testAspect.createProperty("test:" + PROP4_QNAME_OPTIONAL.getLocalName());
		prop4.setMandatory(false);
        prop4.setType("d:" + PropertyTypeDefinition.TEXT.getLocalName());
		prop4.setMultiValued(false);

        M2Aspect testMandatoryAspect = model.createAspect("test:" + TEST_MANDATORY_ASPECT_QNAME.getLocalName());
        M2Property prop5 = testMandatoryAspect.createProperty("test:" + PROP5_QNAME_MANDATORY.getLocalName());
        prop5.setType("d:" + PropertyTypeDefinition.TEXT.getLocalName());
        prop5.setMandatory(true);

        testType.addMandatoryAspect("test:" + TEST_MANDATORY_ASPECT_QNAME.getLocalName());
        
        dictionaryDAO.putModel(model);
	}
	
	/**
	 * Test copy new node within store	 
	 */
	public void testCopyToNewNode()
	{
		// Copy to new node without copying children
		NodeRef copy = this.nodeOperationsService.copy(
				this.sourceNodeRef,
				this.rootNodeRef,
				null,
				QName.createQName("{test}copyAssoc"));		
		checkCopiedNode(this.sourceNodeRef, copy, true, true, false);
		
		// Copy to new node, copying children
		NodeRef copy2 = this.nodeOperationsService.copy(
				this.sourceNodeRef,
				this.rootNodeRef,
				null,
				QName.createQName("{test}copyAssoc"),
				true);		
		checkCopiedNode(this.sourceNodeRef, copy2, true, true, true);
		
		// Check that a copy of a copy works correctly
		NodeRef copyOfCopy = this.nodeOperationsService.copy(
				copy,
				this.rootNodeRef,
				null,
				QName.createQName("{test}copyOfCopy"));
		checkCopiedNode(copy, copyOfCopy, true, true, false);
		
        // TODO check copying from a versioned copy
		// TODO check copying from a lockable copy
		
		// Check copying from a node with content
		//Map<QName, Serializable>contentProperties = new HashMap<QName, Serializable>();
		//contentProperties.put(DictionaryBootstrap.PROP_QNAME_MIME_TYPE, "text/plain");
		//contentProperties.put(DictionaryBootstrap.PROP_QNAME_ENCODING, "UTF-8");
		//this.nodeService.addAspect(this.sourceNodeRef, DictionaryBootstrap.ASPECT_QNAME_CONTENT, contentProperties);		
		ContentWriter contentWriter = this.contentService.getUpdatingWriter(this.sourceNodeRef);
		contentWriter.putContent(SOME_CONTENT);		
		NodeRef copyWithContent = this.nodeOperationsService.copy(
				this.sourceNodeRef,
				this.rootNodeRef,
				null,
				QName.createQName("{test}copyWithContent"));
		checkCopiedNode(this.sourceNodeRef, copyWithContent, true, true, false);
		//assertTrue(this.nodeService.hasAspect(copyWithContent, DictionaryBootstrap.ASPECT_QNAME_CONTENT));
		ContentReader contentReader = this.contentService.getReader(copyWithContent);
		assertNotNull(contentReader);
		assertEquals(SOME_CONTENT, contentReader.getContentString());
		
		// TODO check copying to a different store
		
		System.out.println(
				NodeStoreInspector.dumpNodeStore(this.nodeService, this.storeRef));
	}	
	
	public void testCopyToExistingNode()
	{
		// Copy nodes within the same store
		this.nodeOperationsService.copy(this.sourceNodeRef, this.destinationNodeRef);
		checkCopiedNode(this.sourceNodeRef, this.destinationNodeRef, false, true, false);
		
		// TODO check copying from a copy
		// TODO check copying from a versioned copy
		// TODO check copying from a lockable copy
		// TODO check copying from a node with content
		
		// TODO check copying nodes between stores
		
		System.out.println(
				NodeStoreInspector.dumpNodeStore(this.nodeService, this.storeRef));
	}
	
	/**
	 * Check that the copied node contains the state we are expecting
	 * 
	 * @param sourceNodeRef       the source node reference
	 * @param destinationNodeRef  the destination node reference
	 */
	private void checkCopiedNode(NodeRef sourceNodeRef, NodeRef destinationNodeRef, boolean newCopy, boolean sameStore, boolean copyChildren)
	{
		if (newCopy == true)
		{
			if (sameStore == true)
			{
				// Check that the copy aspect has been applied to the copy
				boolean hasCopyAspect = this.nodeService.hasAspect(destinationNodeRef, DictionaryBootstrap.ASPECT_QNAME_COPIEDFROM);
				assertTrue(hasCopyAspect);
				NodeRef copyNodeRef = (NodeRef)this.nodeService.getProperty(destinationNodeRef, DictionaryBootstrap.PROP_QNAME_COPY_REFERENCE);
				assertNotNull(copyNodeRef);
				assertEquals(sourceNodeRef, copyNodeRef);
			}
			else
			{
				// Check that destiantion has the same id as the source
				assertEquals(sourceNodeRef.getId(), destinationNodeRef.getId());
		}
		}
		
		// TODO
		// Check that all the appropriate aspects have been applied to the desitation node
//		Set<ClassRef> sourceAspects = this.nodeService.getAspects(sourceNodeRef);
//		Set<ClassRef> destinationAspects = this.nodeService.getAspects(destinationNodeRef);
//		if (sameStore == true && newCopy == true)
//		{
//			assertEquals(sourceAspects.size()+1, destinationAspects.size());
//		}
//		else
//		{
//			assertEquals(sourceAspects.size(), destinationAspects.size());
//		}
		boolean hasTestAspect = this.nodeService.hasAspect(destinationNodeRef, TEST_ASPECT_QNAME);
		assertTrue(hasTestAspect);
		
		// Check that all the correct properties have been copied
		Map<QName, Serializable> destinationProperties = this.nodeService.getProperties(destinationNodeRef);
		assertNotNull(destinationProperties);
		// TODO
//		if (sameStore == true && newCopy == true)
//		{
//			assertEquals(5, destinationProperties.size());
//		}
//		else
//		{
//			assertEquals(4, destinationProperties.size());
//		}
		String value1 = (String)destinationProperties.get(PROP1_QNAME_MANDATORY);
		assertNotNull(value1);
		assertEquals(TEST_VALUE_1, value1);
		String value2 = (String)destinationProperties.get(PROP2_QNAME_OPTIONAL);
		assertNotNull(value2);
		assertEquals(TEST_VALUE_2, value2);
		String value3 = (String)destinationProperties.get(PROP3_QNAME_MANDATORY);
		assertNotNull(value3);
		assertEquals(TEST_VALUE_1, value3);
		String value4 = (String)destinationProperties.get(PROP4_QNAME_OPTIONAL);
		assertNotNull(value4);
		assertEquals(TEST_VALUE_2, value4);
		
		// Check all the target associations have been copied
		List<NodeAssocRef> destinationTargets = this.nodeService.getTargetAssocs(destinationNodeRef, TEST_ASSOC_QNAME);
		assertNotNull(destinationTargets);
		assertEquals(1, destinationTargets.size());
		NodeAssocRef nodeAssocRef = destinationTargets.get(0);
		assertNotNull(nodeAssocRef);
		assertEquals(this.targetNodeRef, nodeAssocRef.getTargetRef());
		
		// Check all the child associations have been copied
		List<ChildAssocRef> childAssocRefs = this.nodeService.getChildAssocs(destinationNodeRef);
		assertNotNull(childAssocRefs);
		assertEquals(2, childAssocRefs.size());
		for (ChildAssocRef ref : childAssocRefs) 
		{
			if (ref.getQName().equals(TEST_CHILD_ASSOC_QNAME2) == true)
			{
				// Since this child is non-primary in the source it will always be non-primary in the destination
				assertFalse(ref.isPrimary());
				assertEquals(this.nonPrimaryChildNodeRef, ref.getChildRef());
			}
			else
			{
				if (copyChildren == false)
				{
					assertFalse(ref.isPrimary());
					//assertEquals(this.childNodeRef, ref.getChildRef());
				}
				else
				{
					//assertTrue(ref.isPrimary());
					assertTrue(this.childNodeRef.equals(ref.getChildRef()) == false);
					
					// TODO need to check that the copied child has all the correct details ..
				}
			}	
		}
	}
}
