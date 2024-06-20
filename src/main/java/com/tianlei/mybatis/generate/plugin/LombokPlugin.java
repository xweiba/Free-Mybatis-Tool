package com.tianlei.mybatis.generate.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.*;

public class LombokPlugin extends PluginAdapter {
    private final Collection<Annotations> annotations = new LinkedHashSet(Annotations.values().length);

    public LombokPlugin() {
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addDataAnnotation(topLevelClass);
        return true;
    }

    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
        return false;
    }

    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
        return false;
    }

    private void addDataAnnotation(TopLevelClass topLevelClass) {
        Iterator var2 = this.annotations.iterator();

        while(var2.hasNext()) {
            Annotations annotation = (Annotations)var2.next();
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.name);
        }

    }

    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.annotations.add(Annotations.DATA);
        Iterator var2 = properties.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry)var2.next();
            boolean isEnable = Boolean.parseBoolean(entry.getValue().toString());
            if (isEnable) {
                String paramName = entry.getKey().toString().trim();
                Annotations annotation = Annotations.getValueOf(paramName);
                if (annotation != null) {
                    this.annotations.add(annotation);
                    this.annotations.addAll(Annotations.getDependencies(annotation));
                }
            }
        }

    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");
        return true;
    }

    private static enum Annotations {
        DATA("data", "@Data", "lombok.Data"),
        BUILDER("builder", "@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
        ACCESSORS("accessors", "@Accessors(chain = true)", "lombok.experimental.Accessors"),
        TO_STRING("toString", "@ToString", "lombok.ToString");

        private final String paramName;
        private final String name;
        private final FullyQualifiedJavaType javaType;

        private Annotations(String paramName, String name, String className) {
            this.paramName = paramName;
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
        }

        private static Annotations getValueOf(String paramName) {
            Annotations[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Annotations annotation = var1[var3];
                if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                    return annotation;
                }
            }

            return null;
        }

        private static Collection<Annotations> getDependencies(Annotations annotation) {
            return (Collection)(annotation == ALL_ARGS_CONSTRUCTOR ? Collections.singleton(NO_ARGS_CONSTRUCTOR) : Collections.emptyList());
        }
    }
}
