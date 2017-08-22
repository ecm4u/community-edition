package org.alfresco.service.cmr.repository;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Helper class to provide an {@link ApplicationContext} to {@link MLText}. See
 * https://github.com/ecm4u/community-edition/issues/2
 *
 * @author Lutz Horn lutz.horn@ecm4u.de
 *
 */
public class MLTextSwitchProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
