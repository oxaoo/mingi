package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_node;
import libsvm.svm_problem;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
@AllArgsConstructor
public class SvmProblemWrapper implements SvmProblem {

    private final List<List<SvmNode>> matrix;
    private final List<Double> vector;
    private final int dimension;


    public SvmProblemWrapper(final int nodesNumber) {
        this.matrix = new ArrayList<>(nodesNumber);
        this.vector = new ArrayList<>(nodesNumber);
        this.dimension = nodesNumber;
    }

    @Override
    public svm_problem getProblem() {
        final svm_problem problem = new svm_problem();
        problem.x = new svm_node[this.matrix.size()][];
        IntStream.range(0, this.matrix.size()).forEach(i -> problem.x[i]
                = this.matrix.get(i).stream().map(SvmNode::getNode).toArray(svm_node[]::new));
        problem.y = this.vector.stream().mapToDouble(Double::doubleValue).toArray();
        problem.l = this.dimension;
        return problem;
    }

    @Override
    public void initMatrixRow(final int capacity) {
        this.matrix.add(new ArrayList<>(capacity));
    }

    @Override
    public void addMatrixValue(final SvmNode node) {
        this.matrix.get(this.matrix.size() - 1).add(node);
    }

    @Override
    public void addVectorValue(double value) {
        this.vector.add(value);
    }
}
