package com.tianlei.mybatis.generate.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;

import java.util.List;

public class RepositoryPlugin
        extends PluginAdapter {
    private FullyQualifiedJavaType annotationRepository = new FullyQualifiedJavaType("org.springframework.stereotype.Repository");
    private String annotation = "@Repository";


    public boolean validate(List<String> list) {
        return true;
    }

    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(this.annotationRepository);
        interfaze.addAnnotation(this.annotation);
        return true;
    }
}

