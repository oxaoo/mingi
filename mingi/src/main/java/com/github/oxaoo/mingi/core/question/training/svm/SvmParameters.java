package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_parameter;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
public interface SvmParameters {

    svm_parameter getParameters();

    void setProbability(int probability);
    void setGamma(double gamma);
    void setNu(double nu);
    void setC(double c);
    void setType(SvmType type);
    void setKernelType(SvmKernelType type);
    void setCacheSize(double cacheSize);
    void setEps(double eps);

}
