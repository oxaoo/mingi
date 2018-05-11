package com.github.oxaoo.mingi.service.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 10.05.2018
 */
@Getter
@Setter
public class ParserProperties implements Serializable {
    private static final long serialVersionUID = 2301226568272423219L;

    private String commonSource;
    private String classifierModelPath;
    private String treeTaggerPath;
    private String parserConfigurationPath;
}
