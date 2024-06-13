package com.tianlei.mybatis.generate.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Iterator;
import java.util.List;

/**
 * Jpa 注解
 *
 * 作者 weiba
 * 日期 2024/6/13 上午9:41
 */
public class JpaAnnotationPlugin extends PluginAdapter {

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

        boolean isId = introspectedColumn.isIdentity();

        if (!isId) {
            Iterator<IntrospectedColumn> var5 = introspectedTable.getPrimaryKeyColumns().iterator();

            while (var5.hasNext()) {
                IntrospectedColumn column = var5.next();
                if (introspectedColumn == column) {
                    isId = true;
                    break;
                }
            }
        }


        // 处理主键标识
        if (isId) {
            topLevelClass.addImportedType("javax.persistence.Id");
            field.addAnnotation("@Id");
        }

        // 处理自生成主键注解
        if (introspectedColumn.isAutoIncrement()) {
            topLevelClass.addImportedType("javax.persistence.GeneratedValue");
            if (introspectedTable.getTableConfiguration().getGeneratedKey() != null
                    && "JDBC".equals(introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement())) {
                field.addAnnotation("@GeneratedValue(generator = \"JDBC\")");
            } else {
                topLevelClass.addImportedType("javax.persistence.GenerationType");
                field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
            }
        }

        // 处理Sequence列的注解
        if (introspectedColumn.isSequenceColumn()) {
            topLevelClass.addImportedType("javax.persistence.SequenceGenerator");
            field.addAnnotation("@SequenceGenerator(name=\"seq_gen\",sequenceName=\"" +introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement()+ "\")");

            topLevelClass.getImportedTypes().remove("javax.persistence.GenerationType");
            topLevelClass.addImportedType("javax.persistence.GenerationType");
            field.addAnnotation("@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"seq_gen\")");
        }

        // 添加@NotEmpty注解
        if (!introspectedColumn.isNullable() && !isId) {
            topLevelClass.addImportedType("org.hibernate.validator.constraints.NotEmpty");
            field.addAnnotation("@NotEmpty");
        }

        // 添加@Column注解
        topLevelClass.addImportedType("javax.persistence.Column");

        StringBuilder sb = new StringBuilder();

        sb.append("@Column(name = \"");
        sb.append(introspectedColumn.getActualColumnName());
        sb.append("\"");

        if (!introspectedColumn.isNullable()) {
            sb.append(", nullable = false");
        }

        // 如果是自增列，插入和更新时不带该属性
        if (introspectedColumn.isAutoIncrement()) {
            sb.append(", insertable = false, updatable = false");
        }

        if (introspectedColumn.getLength() > 0) {
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
