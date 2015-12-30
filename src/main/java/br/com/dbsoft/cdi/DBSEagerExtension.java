package br.com.dbsoft.cdi;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

import br.com.dbsoft.annotation.DBSEager;

/**
 * Implementa a anotação DBSEager para forçar a inicialização dos @ApplicationScoped logo no startup
 * @author ricardo.villar
 *
 */
public class DBSEagerExtension implements Extension {
    private List<Bean<?>> eagerBeansList = new ArrayList<Bean<?>>();

    public <T> void collect(@Observes ProcessBean<T> pEvent) {
        if (pEvent.getAnnotated().isAnnotationPresent(DBSEager.class)
            && pEvent.getAnnotated().isAnnotationPresent(ApplicationScoped.class)) {
            eagerBeansList.add(pEvent.getBean());
        }
    }

    @SuppressWarnings("unused")
	public void load(@Observes AfterDeploymentValidation pEvent, BeanManager pBeanManager) {
        for (Bean<?> bean : eagerBeansList) {
            // note: toString() is important to instantiate the bean
            pBeanManager.getReference(bean, bean.getBeanClass(), pBeanManager.createCreationalContext(bean)).toString();
        }
    }
}