package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
public abstract class SvmManager {

    public static SvmModel loadModel(final BufferedReader bf) throws IOException {
        final svm_model loadedModel = svm.svm_load_model(bf);
        return new SvmModelWrapper(loadedModel);
    }

    public static SvmModel loadModel(final String modelPath) throws IOException {
        final InputStreamReader streamReader = new InputStreamReader(new FileInputStream(modelPath));
        return loadModel(new BufferedReader(streamReader));
    }

    public static void saveModel(final String modelPath, final SvmModel modelWrapper) throws IOException {
        svm.svm_save_model(modelPath, modelWrapper.getModel());
    }

    public static SvmModel train(SvmProblem problemWrapper, SvmParameters parametersWrapper) {
        final svm_model model = svm.svm_train(problemWrapper.getProblem(), parametersWrapper.getParameters());
        return new SvmModelWrapper(model);
    }

    public static int getClassesNumber(final SvmModel modelWrapper) {
        return svm.svm_get_nr_class(modelWrapper.getModel());
    }

    public static int[] getLabels(final SvmModel modelWrapper, final int classesNumber) {
        final int[] labels = new int[classesNumber];
        svm.svm_get_labels(modelWrapper.getModel(), labels);
        return labels;
    }

    public static double predictProbability(final SvmModel modelWrapper, final List<SvmNode> wrappedNodes, final int classesNumber) {
        final double[] probEstimates = new double[classesNumber];
        final svm_node[] arrayNode = wrappedNodes.stream().map(SvmNode::getNode).toArray(svm_node[]::new);
        return svm.svm_predict_probability(modelWrapper.getModel(), arrayNode, probEstimates);
    }
}
