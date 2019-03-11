/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.jtools.javawebdatafront;


import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.lang.reflect.*;
import java.util.*;

public class JWebFilerUtils {

	public JWebFilerUtils() {
	}

	public static Class[] getParameterTypes(Object filer, String methodName) {
		try {
			Class c = filer.getClass();
			Method[] theMethods = c.getMethods();
			for (int i = 0; i < theMethods.length; i++) {
				String methodString = theMethods[i].getName();
				if (methodName.equalsIgnoreCase(methodString)) {
					return theMethods[i].getParameterTypes();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// Mahmood Code begin 11 Nov 2003
	public static Class[] getParameterTypes(Object filer, String methodName, String parameterType) {
		try {
			Class c = filer.getClass();
			Method[] theMethods = c.getMethods();
			for (int i = 0; i < theMethods.length; i++) {
				String methodString = theMethods[i].getName();
				if (theMethods[i].getParameterTypes().length > 0 && isPrimitive(theMethods[i].getParameterTypes()[0].getName()) && !isPrimitive(parameterType))
					continue;
				if (methodName.equalsIgnoreCase(methodString)) { // &&
					// parameterType.equalsIgnoreCase(theMethods[i].getParameterTypes()[0].getName()))
					// {
					return theMethods[i].getParameterTypes();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static final String appDir = "d:\\projects\\javacharts\\com\\javawebcharts\\";

	public static void getTagsGetSetMethodsFromXML(String parentTag, ArrayList tags, ArrayList getters, ArrayList setters) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			Document doc = factory.newDocumentBuilder().parse(new java.io.File(appDir + "Objects.xml"));
			Element docElement = doc.getDocumentElement();
			if (docElement.hasChildNodes()) {
				NodeList rulebase = docElement.getChildNodes();
				// System.out.println("=============================================================");
				// System.out.println("parentTag : " + parentTag);
				for (int i = 0; i < rulebase.getLength(); i++) {
					Node nameNode = rulebase.item(i);
					// System.out.println("name : " + nameNode.getNodeName());
					// System.out.println("type : " + nameNode.getNodeType());
					// System.out.println("Value : " + nameNode.getNodeValue());
					if (nameNode.getNodeName().equalsIgnoreCase(parentTag)) {
						if (nameNode.hasChildNodes()) {
							NodeList propertyList = nameNode.getChildNodes();
							for (int j = 0; j < propertyList.getLength(); j++) {
								Node propertyNode = propertyList.item(j);
								// System.out.println("prt name : " +
								// propertyNode.getNodeName());
								// System.out.println("prt type : " +
								// propertyNode.getNodeType());
								// System.out.println("prt Value : " +
								// propertyNode.getNodeValue());
								if (propertyNode.getNodeName().equalsIgnoreCase("PROPERTY")) {
									if (propertyNode.hasChildNodes()) {
										NodeList childList = propertyNode.getChildNodes();
										for (int k = 0; k < childList.getLength(); k++) {
											Node childNode = childList.item(k);
											// System.out.println("child name :
											// " + childNode.getNodeName());
											// System.out.println("child type :
											// " + childNode.getNodeType());
											// System.out.println("child Value :
											// " + childNode.getNodeValue());
											if (childNode.getNodeName().equalsIgnoreCase("TAG")) {
												if (childNode.hasChildNodes()) {
													NodeList tagList = childNode.getChildNodes();
													for (int l = 0; l < tagList.getLength(); l++) {
														Node tagNode = tagList.item(l);
														// System.out.println("tag
														// name : " +
														// tagNode.getNodeName());
														// System.out.println("tag
														// type : " +
														// tagNode.getNodeType());
														// System.out.println("tag
														// Value : " +
														// tagNode.getNodeValue());
														tags.add(tagNode.getNodeValue());
													}
												}
											} else if (childNode.getNodeName().equalsIgnoreCase("GET")) {
												if (childNode.hasChildNodes()) {
													NodeList gettersList = childNode.getChildNodes();
													for (int l = 0; l < gettersList.getLength(); l++) {
														Node getterNode = gettersList.item(l);
														// System.out.println("get
														// name : " +
														// getterNode.getNodeName());
														// System.out.println("get
														// type : " +
														// getterNode.getNodeType());
														// System.out.println("get
														// Value : " +
														// getterNode.getNodeValue());
														getters.add(getterNode.getNodeValue());
													}
												}
											} else if (childNode.getNodeName().equalsIgnoreCase("SET")) {
												if (childNode.hasChildNodes()) {
													NodeList settersList = childNode.getChildNodes();
													for (int l = 0; l < settersList.getLength(); l++) {
														Node setterNode = settersList.item(l);
														// System.out.println("set
														// name : " +
														// setterNode.getNodeName());
														// System.out.println("set
														// type : " +
														// setterNode.getNodeType());
														// System.out.println("set
														// Value : " +
														// setterNode.getNodeValue());
														setters.add(setterNode.getNodeValue());
													}
												}
											}
										}
									}
								} else if (propertyNode.getNodeName().equalsIgnoreCase("SUPER")) {
									if (propertyNode.hasChildNodes()) {
										NodeList superList = propertyNode.getChildNodes();
										for (int k = 0; k < superList.getLength(); k++) {
											Node supperNode = superList.item(k);
											// System.out.println("supper name :
											// " + supperNode.getNodeName());
											// System.out.println("supper type :
											// " + supperNode.getNodeType());
											// System.out.println("supper Value
											// : " + supperNode.getNodeValue());
											getTagsGetSetMethodsFromXML(supperNode.getNodeValue(), tags, getters, setters);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setFilingProperties(Object filer, Element docElement, String parentTag) {
		try {
			// System.out.println("=============================================================");
			// System.out.println("Begin parentTag = " + parentTag);
			// System.out.println("filer = " + filer);
			ArrayList tags = new ArrayList();
			ArrayList saveTags = new ArrayList();
			ArrayList readTags = new ArrayList();
			getTagsGetSetMethodsFromXML(parentTag, tags, saveTags, readTags);
			if (docElement.hasChildNodes()) {
				NodeList rulebase = docElement.getChildNodes();
				for (int i = 0; i < rulebase.getLength(); i++) {
					Node nameNode = rulebase.item(i);
					if (nameNode.hasChildNodes()) {
						// System.out.println("nameNode.getAttributes().getNamedItem(Type)
						// = " + nameNode.getAttributes().getNamedItem("Type"));
						// System.out.print(" Node[child=" +
						// nameNode.hasChildNodes()+" name=" +
						// nameNode.getNodeName() +" value=" +
						// nameNode.getNodeValue() +" type=" +
						// nameNode.getNodeType() + "]");
						if (nameNode.getAttributes().getNamedItem("Type") != null) {
							String dType = nameNode.getAttributes().getNamedItem("Type").getNodeValue();
							// System.out.println("dbType= " + dType);
							NodeList children = nameNode.getChildNodes();
							for (int l = 0; l < children.getLength(); l++) {
								Node valeNode = children.item(l);
								if (valeNode != null) {
									// System.out.print(" child = " +
									// valeNode.hasChildNodes()+" name = " +
									// valeNode.getNodeName()+" value = " +
									// valeNode.getNodeValue()+" type = " +
									// valeNode.getNodeType() + "]");
									// setValue(valeNode.getNodeValue(), dType,
									// j);

									// System.out.println("nameNode.getNodeName()
									// = " + nameNode.getNodeName());
									int k = 0;
									for (k = 0; k < tags.size(); k++)
										if (((String) tags.get(k)).equalsIgnoreCase(nameNode.getNodeName()))
											break;

									// System.out.println("readTags.get(k) = " +
									// readTags.get(k));
									/*
									 * String methodNames[] = new
									 * String[filer.getClass().getMethods().length];
									 * for(int n = 0; n <
									 * filer.getClass().getMethods().length;
									 * n++) { methodNames[n] =
									 * filer.getClass().getMethods()[n].getName();
									 * if(methodNames[n].equalsIgnoreCase("setAxisPosition"))
									 * System.out.println("setAxisPosition
									 * method found at = " + n); }
									 */
									Method setM = filer.getClass().getMethod((String) readTags.get(k), getParameterTypes(filer, (String) readTags.get(k)));
									// System.out.println("setM = " + setM);
									Object[] parms = new Object[1];
									// System.out.println("valeNode.getNodeValue()
									// = " + valeNode.getNodeValue());
									parms[0] = convertIfPrimitive(valeNode.getNodeValue(), dType); // Float.valueOf(val);
									// System.out.println("parms[0] = " +
									// parms[0]);
									setM.invoke(filer, parms);
								}
							}
						} else {
							// getMethod for reading.
							// System.out.println("when object
							// nameNode.getNodeName() = " +
							// nameNode.getNodeName());
							int k = 0;
							for (k = 0; k < tags.size(); k++)
								if (((String) tags.get(k)).equalsIgnoreCase(nameNode.getNodeName()))
									break;

							// System.out.println("saveTags.get(k) = " +
							// saveTags.get(k));
							Method getM = filer.getClass().getMethod((String) saveTags.get(k), (Class<?>) null);
							Object obj = getM.invoke(filer, (Class<?>) null);
							if (obj == null) {
								Class c = Class.forName(getM.getReturnType().getName());
								obj = c.newInstance();
							}
							if (obj instanceof ArrayList) { // if the object is
								// an ArrayList
								ArrayList list = (ArrayList) obj;
								// System.out.println("((String) tags.get(k)) =
								// " + ((String) tags.get(k)));
								String tag = (String) tags.get(k);
								tag = tag.substring(0, tag.length() - 4);
								// System.out.println("tag = " + tag);
								// int size =
								// Integer.parseInt(nameNode.getAttributes().getNamedItem("Size").getNodeValue());
								// System.out.println("size = " + size);
								String className = nameNode.getAttributes().getNamedItem("ElementsType").getNodeValue();
								// System.out.println("className = " +
								// className);
								list.removeAll(list);
								Class c = Class.forName(className.trim());
								// System.out.println("c = " + c);
								if (nameNode.hasChildNodes()) {
									NodeList arrayElements = nameNode.getChildNodes();
									for (int l = 0; l < arrayElements.getLength(); l++) {
										Node elmt = arrayElements.item(l);
										// System.out.println("elmt.getNodeName()
										// = " + elmt.getNodeName());
										if (elmt.getNodeName().equalsIgnoreCase(tag)) {
											Object o = c.newInstance();
											// System.out.println("o = " + o);
											setFilingProperties(o, (Element) elmt, tag);
											list.add(o);
										}
									}
									// System.out.println("readTags.get(k) = " +
									// readTags.get(k));
									Method setM = filer.getClass().getMethod((String) readTags.get(k), getParameterTypes(filer, (String) readTags.get(k)));
									// System.out.println("setM = " + setM);
									Object[] parms = new Object[1];
									parms[0] = list;
									setM.invoke(filer, parms);
								}
								/*
								 * for(int j = 0; j < size; j++) { Object o =
								 * c.newInstance(); System.out.println("o = " +
								 * o); setFilingProperties(o, (Element)
								 * nameNode, tag); list.add(o); }
								 */
							} else { // if the object is not an ArrayList
								setFilingProperties(obj, (Element) nameNode, ((String) tags.get(k)));
								// System.out.println("readTags.get(k) = " +
								// readTags.get(k));
								// System.out.println("readTags.get(k+1) = " +
								// readTags.get(k+1));
								/*
								 * String methodNames[] = new
								 * String[filer.getClass().getMethods().length];
								 * for(int n = 0; n <
								 * filer.getClass().getMethods().length; n++) {
								 * methodNames[n] =
								 * filer.getClass().getMethods()[n].getName();
								 * if(methodNames[n].equalsIgnoreCase("setBottomWall"))
								 * System.out.println("setBottomWall method
								 * found at = " + n); }
								 */
								// System.out.println("obj.getClass().getName()
								// = " + obj.getClass().getName());
								Method setM = filer.getClass().getMethod((String) readTags.get(k), getParameterTypes(filer, (String) readTags.get(k), obj.getClass().getName()));
								// System.out.println("setM = " + setM);
								Object[] parms = new Object[1];
								parms[0] = obj;
								setM.invoke(filer, parms);
							}
						}
					}
				}
			}
			// System.out.println("End parentTag = " + parentTag);
			// System.out.println("************************************************************");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String getFilingProperties(Object filer, String parentTag) {
		try {
			ArrayList tags = new ArrayList();
			ArrayList writeTags = new ArrayList();
			ArrayList readTags = new ArrayList();
			getTagsGetSetMethodsFromXML(parentTag, tags, writeTags, readTags);
			// System.out.println("=============================================================");
			// System.out.println("parentTag = " + parentTag);

			String nl = System.getProperties().getProperty("line.separator");
			String properties = "<" + parentTag + ">";
			for (int j = 0; j < tags.size(); j++) {
				// System.out.println("(String) writeTags.get(j) = " + ((String)
				// writeTags.get(j)));
				Method getM = filer.getClass().getMethod((String) writeTags.get(j), (Class<?>)null);
				// System.out.println("getM.getReturnType().getName() = " +
				// getM.getReturnType().getName());
				if (!isPrimitive(getM.getReturnType().getName())) {
					Object returnObject = getM.invoke(filer, (Class<?>) null);
					// System.out.println("returnObject = " + returnObject);
					if (returnObject != null) {
						if (returnObject instanceof ArrayList) {
							String tag = (String) tags.get(j);
							ArrayList list = (ArrayList) returnObject;
							if (list.size() > 0) {
								// System.out.println("list.get(0).getClass().getName()
								// = " + list.get(0).getClass().getName());
								properties += "<" + tag + " ElementsType='" + list.get(0).getClass().getName() + "'>";
								// tag = tag.substring(0, tag.length() - 4);
								for (int i = 0; i < list.size(); i++)
									properties += nl + getFilingProperties(list.get(i), tag.substring(0, tag.length() - 4));

								properties += "</" + tag + ">";
							}
						} else
							properties += nl + getFilingProperties(returnObject, (String) tags.get(j));
					}
				} else {
					properties += nl + "<" + ((String) tags.get(j)) + " Type='" + getM.getReturnType().getName() + "'>" + getM.invoke(filer, (Class<?>) null) + "</" + ((String) tags.get(j)) + ">";
				}
			}
			properties += nl + "</" + parentTag + ">";
			// System.out.println("End parentTag = " + parentTag);
			// System.out.println("*********************************************************");
			return properties;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	// Mahmood Code end 11 Nov 2003
	/*
	 * public static void setFilingProperties (JWebFiler filer, Element
	 * docElement) { try { String[] tags = filer.getTagListForXML(); String[]
	 * saveTags = filer.getMethodListForSavingToXML(); String[] readTags =
	 * filer.getMethodListForReadingFromXML(); if (docElement.hasChildNodes()) {
	 * NodeList rulebase = docElement.getChildNodes(); for (int i = 0; i <
	 * rulebase.getLength(); i++) { Node nameNode = rulebase.item(i); if
	 * (nameNode.hasChildNodes()) { //System.out.println(); //System.out.print("
	 * Node[child=" + nameNode.hasChildNodes()+" name=" + nameNode.getNodeName() +"
	 * value=" + nameNode.getNodeValue() +" type=" + nameNode.getNodeType() +
	 * "]"); if (nameNode.getAttributes().getNamedItem("Type") != null) { String
	 * dType = nameNode.getAttributes().getNamedItem("Type").getNodeValue();
	 * NodeList children = nameNode.getChildNodes(); //System.out.print(" Values
	 * dbType= " + dType +"["); for (int l = 0; l < children.getLength(); l++) {
	 * Node valeNode = children.item(l); if (valeNode != null) {
	 * //System.out.print(" child = " + valeNode.hasChildNodes()+" name = " +
	 * valeNode.getNodeName()+" value = " + valeNode.getNodeValue()+" type = " +
	 * valeNode.getNodeType() + "]"); //setValue(valeNode.getNodeValue(), dType,
	 * j); int k = JWebUtils.find(tags, nameNode.getNodeName()); Method setM =
	 * filer.getClass().getMethod(readTags[k], getParameterTypes(filer,
	 * readTags[k])); Object[] parms = new Object[1]; parms[0] =
	 * convertIfPrimitive(valeNode.getNodeValue(), dType); //Float.valueOf(val);
	 * setM.invoke(filer, parms); } } } else { // getMethod for reading. int k =
	 * JWebUtils.find(tags, nameNode.getNodeName()); Method getM =
	 * filer.getClass().getMethod(saveTags[k], null); Object obj =
	 * getM.invoke(filer, null); if (obj instanceof JWebFiler) { JWebFiler
	 * childFiler = (JWebFiler)obj; setFilingProperties(childFiler,
	 * (Element)nameNode); } } } } } } catch (Exception ex) {
	 * ex.printStackTrace(); } }
	 */

	/*
	 * public static String getFilingProperties (JWebFiler filer, String
	 * parentTag) { try { ArrayList tags = new ArrayList(); ArrayList writeTags =
	 * new ArrayList(); ArrayList readTags = new ArrayList();
	 * getObjectFromXML(parentTag, tags, writeTags, readTags);
	 * System.out.println("=============================================================");
	 * 
	 * //String[] tags = filer.getTagListForXML(); //String[] writeTags =
	 * filer.getMethodListForSavingToXML(); //String[] readTags =
	 * filer.getMethodListForReadingFromXML(); Class c = filer.getClass();
	 * Method[] theMethods = c.getMethods(); String nl =
	 * System.getProperties().getProperty("line.separator"); String properties = "<" +
	 * parentTag + ">"; for (int j = 0; j < tags.size(); j++) {
	 * System.out.println("(String) writeTags.get(j) = " + ((String)
	 * writeTags.get(j))); Method getM = filer.getClass().getMethod((String)
	 * writeTags.get(j), null); if
	 * (!isPrimitive(getM.getReturnType().getName())) { Object returnObject =
	 * getM.invoke(filer, null); if (returnObject instanceof JWebFiler) {
	 * JWebFiler childFiler = (JWebFiler)returnObject; properties += nl +
	 * getFilingProperties(childFiler, (String) tags.get(j)); } } else {
	 * properties += nl + "<" + ((String) tags.get(j)) + " Type='" +
	 * getM.getReturnType().getName() + "'>" + getM.invoke(filer, null) + "</" +
	 * ((String) tags.get(j)) + ">"; } } properties += nl + "</" + parentTag +
	 * ">"; return properties;
	 */
	/*
	 * String[] tags = filer.getTagListForXML(); String[] writeTags =
	 * filer.getMethodListForSavingToXML(); String[] readTags =
	 * filer.getMethodListForReadingFromXML(); Class c = filer.getClass();
	 * Method[] theMethods = c.getMethods(); String nl =
	 * System.getProperties().getProperty("line.separator"); String properties = "<" +
	 * parentTag + ">"; for (int j = 0; j < tags.length; j++) { Method getM =
	 * filer.getClass().getMethod(writeTags[j], null);
	 * //System.err.println("tag:"+writeTags[j]+"
	 * Return:"+getM.getReturnType().getName()+" for
	 * "+filer.getClass().getName()+" method:"+getM.toString()); if
	 * (!isPrimitive(getM.getReturnType().getName())) { Object returnObject =
	 * getM.invoke(filer, null); if (returnObject instanceof JWebFiler) {
	 * JWebFiler childFiler = (JWebFiler)returnObject; properties += nl +
	 * getFilingProperties(childFiler, tags[j]); } } else { properties += nl + "<" +
	 * tags[j] + " Type='" + getM.getReturnType().getName() + "'>" +
	 * getM.invoke(filer, null) + "</" + tags[j] + ">"; } } properties += nl + "</" +
	 * parentTag + ">"; return properties;
	 */
	/*
	 * } catch (Exception ex) { ex.printStackTrace(); } return ""; }
	 */
	private static Object convertIfPrimitive(String value, String dType) {
		String dt[] = { "byte", "char", "int", "short", "long", "float", "double", "boolean", "java.lang.String" };
		int i = 0;
		for (i = 0; i < dt.length; i++) {
			if (dType.compareTo(dt[i]) == 0) {
				break;
			}
		}
		switch (i) {
		case 0:
			return Byte.valueOf(value);
		case 1:
			return new Character(value.charAt(0));
		case 2:
			return Integer.valueOf(value);
		case 3:
			return Short.valueOf(value);
		case 4:
			return Long.valueOf(value);
		case 5:
			return Float.valueOf(value);
		case 6:
			return Float.valueOf(value);
		case 7:
			return Boolean.valueOf(value);
		}
		return value;
	}

	private static boolean isPrimitive(String dType) {
		String dt[] = { "byte", "char", "int", "short", "long", "float", "double", "boolean", "java.lang.String" };
		for (int i = 0; i < dt.length; i++) {
			if (dType.compareTo(dt[i]) == 0) {
				return true;
			}
		}
		return false;
	}
}
