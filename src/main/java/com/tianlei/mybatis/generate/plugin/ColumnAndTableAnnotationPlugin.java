package com.tianlei.mybatis.generate.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
* PO 属性添加@Column注解
*
* @author weiba
* @date 2024/6/13 上午9:41
*/
public class ColumnAndTableAnnotationPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true; // 验证逻辑，可视需要修改
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addTableAnnotation(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addTableAnnotation(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addTableAnnotation(topLevelClass, introspectedTable);
        return true;
    }

    private void addTableAnnotation(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 在类上添加import语句
        topLevelClass.addImportedType("javax.persistence.Table");

        // 获取表名
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();

        // 构建@Table注解
        StringBuilder annotationBuilder = new StringBuilder();
        annotationBuilder.append("@Table(name = \"").append(tableName).append("\")");

        topLevelClass.addAnnotation(annotationBuilder.toString());
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {

        topLevelClass.addImportedType("javax.persistence.Column");

        StringBuilder sb = new StringBuilder();
        sb.append("@Column(name = \"");
        sb.append(introspectedColumn.getActualColumnName());
        sb.append("\"");

        // 属性判断并添加
        if (!introspectedColumn.isNullable()) {
            sb.append(", nullable = false");
        }

        if (introspectedColumn.isIdentity()) {
            sb.append(", insertable = false");
        }

        if (introspectedColumn.isAutoIncrement()) {
            sb.append(", updatable = false");
        }

        if (introspectedColumn.getLength() != 0) {
            sb.append(", length = ").append(introspectedColumn.getLength());
        }

        if (introspectedColumn.getScale() > 0) {
            sb.append(", scale = ").append(introspectedColumn.getScale());
        }

        sb.append(")");
        field.addAnnotation(sb.toString());

        return true; // 保持其他处理逻辑继续进行
    }
}
