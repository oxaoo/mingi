package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
@Getter
@AllArgsConstructor
public class SvmParametersWrapper implements SvmParameters {
    private final svm_parameter parameters;

    public SvmParametersWrapper() {
        this.parameters = new svm_parameter();
    }

    @Override
    public void setProbability(final int probability) {
        this.parameters.probability = probability;
    }

    @Override
    public void setGamma(final double gamma) {
        this.parameters.gamma = gamma;
    }

    @Override
    public void setNu(final double nu) {
        this.parameters.nu = nu;
    }

    @Override
    public void setC(final double c) {
        this.parameters.C = c;
    }

    @Override
    public void setType(final SvmType type) {
        this.parameters.svm_type = type.getIndex();
    }

    @Override
    public void setKernelType(final SvmKernelType type) {
        this.parameters.kernel_type = type.getIndex();
    }

    @Override
    public void setCacheSize(final double cacheSize) {
        this.parameters.cache_size = cacheSize;
    }

    @Override
    public void setEps(final double eps) {
        this.parameters.eps = eps;
    }
}
