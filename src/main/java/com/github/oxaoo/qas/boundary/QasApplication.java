package com.github.oxaoo.qas.boundary;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 01.04.2017
 */
@ApplicationPath("/qas")
public class QasApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Collections.singletonList(QasService.class));
    }
}
