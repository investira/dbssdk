package br.com.dbsoft.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ricardo.villar
 * Anotação que indica que classe é um Model de uma tabela real no banco de dados
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DBSTableModel {
	 public String tablename();
}
