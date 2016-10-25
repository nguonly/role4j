package net.role4j.evolution;

import net.role4j.Compartment;
import net.role4j.Registry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by nguonly on 10/21/16.
 */
public class UAdaptationXMLParser {
    public static void parse(String xml){
        try {
//            File inputFile = new File(xmlPath);
            InputSource is = new InputSource(new StringReader(xml));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            Registry registry = Registry.getRegistry();

            ////////////////////////
            // compartment tag
            ////////////////////////
            NodeList nodeList = doc.getElementsByTagName("compartment");
            for(int i=0; i<nodeList.getLength(); i++){
                //component tag
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element)node;
                    String sCompId = element.getAttribute("id");
                    int compartmentId = sCompId.isEmpty()?0:Integer.parseInt(sCompId);

                    //Create compartment if it doesn't have one
                    Compartment compartment;
                    if(compartmentId<=0){
                        //compartment = Compartment.initialize(Compartment.class);
                        compartment = registry.newCompartment(Compartment.class);
                        compartment.activate();
                        compartmentId = compartment.hashCode();
                    }

                    processBind(element, compartmentId);

                    processRebindOperation(element, compartmentId);

                    processUnbind(element, compartmentId);

                }
            }
        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    private static void processBind(Element element, int compartmentId) throws Throwable{
        Registry registry = Registry.getRegistry();

        NodeList bindNodes = element.getElementsByTagName("bind");
        for (int iBind = 0; iBind < bindNodes.getLength(); iBind++) {
            Node bindNode = bindNodes.item(iBind);
            Element bindElement = (Element) bindNode;
//            int coreId = Integer.parseInt(bindElement.getAttribute("coreId"));

            String strCoreId = bindElement.getAttribute("coreId");
            String strRoleId = bindElement.getAttribute("roleId");
            int bindCoreId = strCoreId.equals("")?0:Integer.parseInt(strCoreId);
            int bindRoleId = strRoleId.equals("")?0:Integer.parseInt(strRoleId);

            String roleClass = bindElement.getAttribute("roleType");
//                        boolean reload = StringValueConverter.convert(bindElement.getAttribute("reload"), boolean.class);
//            System.out.format("%10s %10s %s\n", compartmentId, coreId, roleClass);

            if(!strCoreId.isEmpty()) {
                //Bind role to core object
                registry.bindRoleToCore(compartmentId, bindCoreId, roleClass);
            }
            if(!strRoleId.isEmpty()){
                registry.bindRoleToRole(compartmentId, bindRoleId, roleClass);
            }
        }
    }

    private static void processRebindOperation(Element element, int compartmentId) throws Throwable{
        Registry registry = Registry.getRegistry();
        NodeList bindNodes = element.getElementsByTagName("rebind");
        for(int iBind=0; iBind<bindNodes.getLength(); iBind++){
            Node bindNode = bindNodes.item(iBind);
            Element bindElement = (Element) bindNode;

            String strCoreId = bindElement.getAttribute("coreId");
            String strRoleId = bindElement.getAttribute("roleId");
            int bindCoreId = strCoreId.equals("")?0:Integer.parseInt(strCoreId);
            int bindRoleId = strRoleId.equals("")?0:Integer.parseInt(strRoleId);

            String roleClass = bindElement.getAttribute("roleType");

            if(!strCoreId.isEmpty()) {
                //Bind role to core object
                registry.rebindRoleToCore(compartmentId, bindCoreId, roleClass);
            }
            if(!strRoleId.isEmpty()){
                registry.rebindRoleToRole(compartmentId, bindRoleId, roleClass);
            }

            ///////////////////
            //invoke tag
            ///////////////////
            NodeList invokeNodes = bindElement.getElementsByTagName("invoke");
            //System.out.println("::: invoke :::: " + invokeNodes.getLength());
            for(int iInvoke=0; iInvoke<invokeNodes.getLength(); iInvoke++){
                Node invokeNode = invokeNodes.item(iInvoke);
                Element invokeNodeElement = (Element)invokeNode;
                String method = invokeNodeElement.getAttribute("method");
                System.out.println(method);

                //param nodes
                NodeList paramNodes = invokeNodeElement.getElementsByTagName("param");
                Object[] paramValues = new Object[paramNodes.getLength()];
                Class[] paramClasses = new Class[paramNodes.getLength()];
                for(int iParam=0; iParam<paramNodes.getLength(); iParam++){
                    Node paramNode = paramNodes.item(iParam);
                    Element paramNodeElement = (Element)paramNode;
//                                paramClasses[iParam] = ClassHelper.forName(paramNodeElement.getAttribute("type"));
//                                paramValues[iParam] = StringValueConverter.convert(paramNodeElement.getAttribute("value"), paramClasses[iParam]);
                }

                //Return type
//                            Class returnType = ClassHelper.forName(invokeNodeElement.getAttribute("returnType"));
                //Prepare method definition to invoke
//                            Object core = registry.getCoreObjectMap().get(coreId);
                //core.invoke(method, paramClasses, paramValues);
//                            registry.invokeRole(null, core, method, returnType, paramClasses, paramValues);
            }

            //Register to watch service for bytecode change
//                        if(reload){
//                            String dir = System.getProperty("user.dir");
//                            String roleFileName = roleClass.substring(roleClass.lastIndexOf('.')+1) + ".class";
//                            String rolePath = roleClass.substring(0, roleClass.lastIndexOf('.'));
//                            Path p = Paths.get(dir + "/target/test-classes/" + rolePath.replaceAll("\\.", File.separator));
//                            FileWatcher fileWatcher = FileWatcher.getInstance();
//                            fileWatcher.register(p);
//                            fileWatcher.monitor(roleFileName);
//                        }
        }
    }

    private static void processUnbind(Element element, int compartmentId) throws Throwable{
        Registry registry = Registry.getRegistry();
        NodeList unbindNodes = element.getElementsByTagName("unbind");
        //System.out.println("::: unbind :::");
        for(int iUnbind=0; iUnbind<unbindNodes.getLength(); iUnbind++){
            Node unbindNode = unbindNodes.item(iUnbind);
            Element unbindElement = (Element)unbindNode;
            String strCoreId = unbindElement.getAttribute("coreId");
            String strRoleId = unbindElement.getAttribute("roleId");
            int unbindCoreId = Integer.parseInt(strCoreId.equals("")?"0":strCoreId);
            int unbindRoleId = Integer.parseInt(strRoleId.equals("")?"0":strRoleId);
            String unbindRole = unbindElement.getAttribute("roleType");

            //Prepare unbind operation
            //unbind role from core
            if(unbindCoreId>0 && !unbindRole.isEmpty()) {
                System.out.println("::: unbind coreId = " + unbindCoreId + " :::");
                registry.unbind(compartmentId, unbindCoreId, true, unbindRole);
            }

            //unbind role from role
            if(unbindRoleId>0 && !unbindRole.isEmpty()) {
                System.out.println("::: unbind coreId = " + unbindCoreId + " :::");
                registry.unbind(compartmentId, unbindRoleId, false, unbindRole);
            }
        }
    }
}
