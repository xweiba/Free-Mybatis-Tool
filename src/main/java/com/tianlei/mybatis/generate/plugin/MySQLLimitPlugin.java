package com.tianlei.mybatis.generate.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class MySQLLimitPlugin
  extends PluginAdapter
{
  public boolean validate(List<String> list) {
    return true;
  }

  public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
    PrimitiveTypeWrapper longWrapper = (new FullyQualifiedJavaType("long")).getPrimitiveTypeWrapper();
    Field limit = new Field("limit", (FullyQualifiedJavaType)integerWrapper);
    limit.setVisibility(JavaVisibility.PRIVATE);
    topLevelClass.addField(limit);
    Method setLimit = new Method("setLimit");
    setLimit.setVisibility(JavaVisibility.PUBLIC);
    setLimit.addParameter(new Parameter((FullyQualifiedJavaType)integerWrapper, "limit"));
    setLimit.addBodyLine("this.limit = limit;");
    topLevelClass.addMethod(setLimit);
    Method getLimit = new Method("getLimit");
    getLimit.setVisibility(JavaVisibility.PUBLIC);
    getLimit.setReturnType((FullyQualifiedJavaType)integerWrapper);
    getLimit.addBodyLine("return limit;");
    topLevelClass.addMethod(getLimit);
    Field offset = new Field("offset", (FullyQualifiedJavaType)longWrapper);
    offset.setVisibility(JavaVisibility.PRIVATE);
    topLevelClass.addField(offset);
    Method setOffset = new Method("setOffset");
    setOffset.setVisibility(JavaVisibility.PUBLIC);
    setOffset.addParameter(new Parameter((FullyQualifiedJavaType)longWrapper, "offset"));
    setOffset.addBodyLine("this.offset = offset;");
    topLevelClass.addMethod(setOffset);
    Method getOffset = new Method("getOffset");
    getOffset.setVisibility(JavaVisibility.PUBLIC);
    getOffset.setReturnType((FullyQualifiedJavaType)longWrapper);
    getOffset.addBodyLine("return offset;");
    topLevelClass.addMethod(getOffset);
    return true;
  }

  public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
    XmlElement ifLimitNotNullElement = new XmlElement("if");
    ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null"));
    XmlElement ifOffsetNotNullElement = new XmlElement("if");
    ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
    ifOffsetNotNullElement.addElement((VisitableElement)new TextElement("limit ${offset}, ${limit}"));
    ifLimitNotNullElement.addElement((VisitableElement)ifOffsetNotNullElement);
    XmlElement ifOffsetNullElement = new XmlElement("if");
    ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
    ifOffsetNullElement.addElement((VisitableElement)new TextElement("limit ${limit}"));
    ifLimitNotNullElement.addElement((VisitableElement)ifOffsetNullElement);
    element.addElement((VisitableElement)ifLimitNotNullElement);
    return true;
  }

  public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
    XmlElement ifLimitNotNullElement = new XmlElement("if");
    ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null"));
    XmlElement ifOffsetNotNullElement = new XmlElement("if");
    ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
    ifOffsetNotNullElement.addElement((VisitableElement)new TextElement("limit ${offset}, ${limit}"));
    ifLimitNotNullElement.addElement((VisitableElement)ifOffsetNotNullElement);
    XmlElement ifOffsetNullElement = new XmlElement("if");
    ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
    ifOffsetNullElement.addElement((VisitableElement)new TextElement("limit ${limit}"));
    ifLimitNotNullElement.addElement((VisitableElement)ifOffsetNullElement);
    element.addElement((VisitableElement)ifLimitNotNullElement);
    return true;
  }
}
