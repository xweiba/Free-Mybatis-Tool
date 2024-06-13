package com.tianlei.mybatis.generate.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class MySQLForUpdatePlugin
        extends PluginAdapter {
    public boolean validate(List<String> warnings) {
        return true;
    }

    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        PrimitiveTypeWrapper booleanWrapper = FullyQualifiedJavaType.getBooleanPrimitiveInstance().getPrimitiveTypeWrapper();
        Field forUpdate = new Field("forUpdate", (FullyQualifiedJavaType) booleanWrapper);
        forUpdate.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(forUpdate);
        Method setForUpdate = new Method("setForUpdate");
        setForUpdate.setVisibility(JavaVisibility.PUBLIC);
        setForUpdate.addParameter(new Parameter((FullyQualifiedJavaType) booleanWrapper, "forUpdate"));
        setForUpdate.addBodyLine("this.forUpdate = forUpdate;");
        topLevelClass.addMethod(setForUpdate);
        Method getForUpdate = new Method("getForUpdate");
        getForUpdate.setVisibility(JavaVisibility.PUBLIC);
        getForUpdate.setReturnType((FullyQualifiedJavaType) booleanWrapper);
        getForUpdate.addBodyLine("return forUpdate;");
        topLevelClass.addMethod(getForUpdate);
        return true;
    }

    private void appendForUpdate(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement forUpdateElement = new XmlElement("if");
        forUpdateElement.addAttribute(new Attribute("test", "forUpdate != null and forUpdate == true"));
        forUpdateElement.addElement((VisitableElement) new TextElement("for update"));
        element.addElement((VisitableElement) forUpdateElement);
    }

    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        appendForUpdate(element, introspectedTable);
        return true;
    }

    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        appendForUpdate(element, introspectedTable);
        return true;
    }
}
