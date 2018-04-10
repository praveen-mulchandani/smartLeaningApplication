package com.sd.smartlearningapplication.classifier;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.IOException;

/**
 * Created by shilpi.das on 18-01-2018.
 */

public class LevelPredictorClassifier {
    private static final String TAG = "LevelPredictorClassifier";
    private TensorFlowInferenceInterface mInferenceInterface;
    private float[] outputs;

    public float[] create(
            AssetManager assetManager,
            String modelFilename,
            String inputName,
            String outputName,
            float input[])
            throws IOException {
        mInferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);

        // The shape of the output is [N, NUM_CLASSES], where N is the batch size.
        int numClasses = 1;

        // Ideally, inputSize could have been retrieved from the shape of the input operation.  Alas,
        // the placeholder node for input in the graphdef typically used does not specify a shape, so it
        // must be passed in as a parameter.

        String[] outputNames = new String[]{"output"};
        outputs = new float[numClasses];
        mInferenceInterface.feed(inputName, input, input.length);
        mInferenceInterface.run(outputNames);
        mInferenceInterface.fetch(outputName, outputs);

        return outputs;
    }

}
