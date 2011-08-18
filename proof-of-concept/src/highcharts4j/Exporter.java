package highcharts4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

public class Exporter {
	public static void main(String[] args) throws TranscoderException, IOException{
		renderTestOptions(1);
		renderTestOptions(2);
	}
	
	public static void renderTestOptions(int number) throws IOException, TranscoderException{
		final FileInputStream stream = new FileInputStream(new File("tests/options"+number+".js"));
		try {
			final FileChannel fc = stream.getChannel();
			final MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			final String svg = renderSVG(Charset.defaultCharset().decode(bb).toString());

			final FileOutputStream output = new FileOutputStream("tests/output/options"+number+".png");

			transcodeSvg(svg, output);
		}
		finally {
			stream.close();
		}
		
	}
	
	public static void transcodeSvg(final String svg, FileOutputStream targetfile) throws TranscoderException{
		final PNGTranscoder transcoder = new PNGTranscoder();
		transcoder.transcode(new TranscoderInput(new StringReader(svg.toString())), new TranscoderOutput(targetfile));
	}

	public static String renderSVG(String options) {
		final Context cx = Context.enter();
		
		try {
			cx.setOptimizationLevel(-1);
			cx.setLanguageVersion(Context.VERSION_1_5);

			final Global scope = new Global(); // !! recreate each time
			scope.init(cx); 
			
			cx.evaluateString(scope, "var chartOptions = "+options, "<inline>", 1, null);
			
			Main.processFile(cx, scope,  "js/exporter.envjs.js");
	
			return scope.get("svg").toString();
		} finally {
			Context.exit();
		}
	}
}
