package com.example.visorx;


//Provides access to an application's raw asset files;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
//Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
import com.example.visorx.Classification;

import java.io.BufferedReader;
//for erros
import java.io.FileInputStream;
import java.io.IOException;
//An InputStreamReader is a bridge from byte streams to character streams:
// //It reads bytes and decodes them into characters using a specified charset.
// //The charset that it uses may be specified by name or may be given explicitly, or the platform's default charset may be accepted.
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
//made by google, used as the window between android and tensorflow native C++
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import org.tensorflow.lite.Interpreter;

/**
 * Changed from https://github.com/MindorksOpenSource/AndroidTensorFlowMNISTExample/blob/master
 * /app/src/main/java/com/mindorks/tensorflowexample/TensorFlowImageClassifier.java
 * Created by marianne-linhares on 20/04/17.
 */

//lets create this classifer
public class TensorFlowClassifier implements Classifier {

    // Only returns if at least this confidence
    //must be a classification percetnage greater than this

    TensorFlowClassifier(){}

    private static final float THRESHOLD = 0.1f;

    private TensorFlowInferenceInterface tfHelper;


    private Interpreter interpreter;
    private int inputSize;
    private boolean feedKeepProb;

    private List<String> labelList;
    private float[] output;
    private String[] outputNames;


    //given a model, its label file, and its metadata
    //fill out a classifier object with all the necessary
    //metadata including output prediction
    public static Classifier create(AssetManager assetManager,
                                    String modelPath,
                                    String labelPath,
                                    int inputSize
    ) throws IOException {

        TensorFlowClassifier classifier = new TensorFlowClassifier();
        classifier.interpreter = new Interpreter(classifier.loadModelFile(assetManager,modelPath), new Interpreter.Options());
        classifier.labelList = classifier.loadLabelList(assetManager, labelPath);
        classifier.inputSize = inputSize;

        return classifier;
    }



    //given a saved drawn model, lets read all the classification labels that are
    //stored and write them to our in memory labels list
    public List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }


    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd("tflite_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }



//    @Override
//    public String name() {
//        return name;
//    }

//    @Override
//    public Classification recognize(final float[] pixels) {
//
//        //using the interface
//        //give it the input name, raw pixels from the drawing,
//        //input size
//        tfHelper.feed(inputName, pixels, 1, inputSize, inputSize, 1);
//
//        //probabilities
//        if (feedKeepProb) {
//            tfHelper.feed("keep_prob", new float[] { 1 });
//        }
//        //get the possible outputs
//        tfHelper.run(outputNames);
//
//        //get the output
//        tfHelper.fetch(outputName, output);
//
//        // Find the best classification
//        //for each output prediction
//        //if its above the threshold for accuracy we predefined
//        //write it out to the view
//        Classification ans = new Classification();
//        for (int i = 0; i < output.length; ++i) {
//            System.out.println(output[i]);
//            System.out.println(labels.get(i));
//            if (output[i] > THRESHOLD && output[i] > ans.getConf()) {
//                ans.update(output[i], labels.get(i));
//            }
//        }
//
//        return ans;
//    }

    @Override
    public Classification recognize(float[][][][] pixels){
        float[][] ans = new float[1][2];
        interpreter.run(pixels,ans);
        float max = -1;
        float index = 0;
        for(int i =0;i<2;i++){
            if(max<ans[0][i]) {
                max = ans[0][i];
                index = i;
            }
        }
        Classification res = new Classification();
        res.update(max,String.valueOf(index));

        return res;
    }
}
