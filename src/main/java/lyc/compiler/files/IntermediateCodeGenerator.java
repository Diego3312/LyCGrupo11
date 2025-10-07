package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;

import lyc.compiler.intermediateCodeG.IntermediateCode;


public class IntermediateCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        IntermediateCode intermediateCode = IntermediateCode.getSingletonInstance();
        fileWriter.write(intermediateCode.toString());
    }
}
