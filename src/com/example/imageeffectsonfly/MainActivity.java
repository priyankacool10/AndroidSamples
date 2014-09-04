package com.example.imageeffectsonfly;

import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {
	  
    private ImageView img;
    private Bitmap bmp,bmp2;
    private Bitmap operation;
    
    public static final double PI = 3.14159d;
    public static final double FULL_CIRCLE_DEGREE = 360d;
    public static final double HALF_CIRCLE_DEGREE = 180d;
    public static final double RANGE = 256d;
    public static final int COLOR_MIN = 0x00;
    public static final int COLOR_MAX = 0xFF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      


        img = (ImageView)findViewById(R.id.imageView1);
        BitmapDrawable  abmp = (BitmapDrawable)img.getDrawable();
        bmp =bmp2= abmp.getBitmap();

    }
    
     /* Bitmap src, int depth, double red, double green, double blue */
     public void createSepiaToningEffect(View view) {
    	    // image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    int depth=5;
    	    int red=2;
    	    int green=5;
    	    int blue=1;
    	    // create output bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, bmp.getConfig());
    	    // constant grayscale
    	    final double GS_RED = 0.3;
    	    final double GS_GREEN = 0.59;
    	    final double GS_BLUE = 0.11;
    	    // color information
    	    int A, R, G, B;
    	    int pixel;
    	 
    	    // scan through all pixels
    	    for(int x = 0; x < width; ++x) {
    	        for(int y = 0; y < height; ++y) {
    	            // get pixel color
    	            pixel = bmp.getPixel(x, y);
    	            // get color on each channel
    	            A = Color.alpha(pixel);
    	            R = Color.red(pixel);
    	            G = Color.green(pixel);
    	            B = Color.blue(pixel);
    	            // apply grayscale sample
    	            B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
    	 
    	            // apply intensity level for sepid-toning on each channel
    	            R += (depth * red);
    	            if(R > 255) { R = 255; }
    	 
    	            G += (depth * green);
    	            if(G > 255) { G = 255; }
    	 
    	            B += (depth * blue);
    	            if(B > 255) { B = 255; }
    	 
    	            // set new pixel color to output image
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	 
    	    // return final image
    	    img.setImageBitmap(bmOut);
    	}
     public void  doInvert(View view) {
    	    // create new bitmap with the same settings as source bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
    	    // color info
    	    int A, R, G, B;
    	    int pixelColor;
    	    // image size
    	    int height = bmp.getHeight();
    	    int width = bmp.getWidth();
    	 
    	    // scan through every pixel
    	    for (int y = 0; y < height; y++)
    	    {
    	        for (int x = 0; x < width; x++)
    	        {
    	            // get one pixel
    	            pixelColor = bmp.getPixel(x, y);
    	            // saving alpha channel
    	            A = Color.alpha(pixelColor);
    	            // inverting byte for each R/G/B channel
    	            R = 255 - Color.red(pixelColor);
    	            G = 255 - Color.green(pixelColor);
    	            B = 255 - Color.blue(pixelColor);
    	            // set newly-inverted pixel to output image
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	 
    	    // return final bitmap
    	    img.setImageBitmap(bmOut);
    	}
     public void applyMeanRemoval(View v) {
    	    double[][] MeanRemovalConfig = new double[][] {
    	        { -1 , -1, -1 },
    	        { -1 ,  9, -1 },
    	        { -1 , -1, -1 }
    	    };
    	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	    convMatrix.applyConfig(MeanRemovalConfig);
    	    convMatrix.Factor = 1;
    	    convMatrix.Offset = 0;
    	    Bitmap bmOut= ConvolutionMatrix.computeConvolution3x3(bmp, convMatrix);
    	    img.setImageBitmap(bmOut);
    	}
     public void doGamma(View v) {
    	 double red=1.8; //0.6 for darkness
    	 double green=1.8;
    	 double blue=1.8;
    	 
    	 // create output image
    	    Bitmap bmOut = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
    	    // get image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    // color information
    	    int A, R, G, B;
    	    int pixel;
    	    // constant value curve
    	    final int    MAX_SIZE = 256;
    	    final double MAX_VALUE_DBL = 255.0;
    	    final int    MAX_VALUE_INT = 255;
    	    final double REVERSE = 1.0;
    	 
    	    // gamma arrays
    	    int[] gammaR = new int[MAX_SIZE];
    	    int[] gammaG = new int[MAX_SIZE];
    	    int[] gammaB = new int[MAX_SIZE];
    	 
    	    // setting values for every gamma channels
    	    for(int i = 0; i < MAX_SIZE; ++i) {
    	        gammaR[i] = (int)Math.min(MAX_VALUE_INT,
    	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
    	        gammaG[i] = (int)Math.min(MAX_VALUE_INT,
    	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
    	        gammaB[i] = (int)Math.min(MAX_VALUE_INT,
    	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
    	    }
    	 
    	    // apply gamma table
    	    for(int x = 0; x < width; ++x) {
    	        for(int y = 0; y < height; ++y) {
    	            // get pixel color
    	            pixel = bmp.getPixel(x, y);
    	            A = Color.alpha(pixel);
    	            // look up gamma
    	            R = gammaR[Color.red(pixel)];
    	            G = gammaG[Color.green(pixel)];
    	            B = gammaB[Color.blue(pixel)];
    	            // set new color to output bitmap
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	 
    	    // return final image
    	    img.setImageBitmap(bmOut);
    	}
     
     public void applyFleaEffect(View v) {
    	    // get image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    int[] pixels = new int[width * height];
    	    // get pixel array from source
    	    bmp.getPixels(pixels, 0, width, 0, 0, width, height);
    	    // a random object
    	    Random random = new Random();
    	 
    	    int index = 0;
    	    // iteration through pixels
    	    for(int y = 0; y < height; ++y) {
    	        for(int x = 0; x < width; ++x) {
    	            // get current index in 2D-matrix
    	            index = y * width + x;
    	            // get random color
    	            int randColor = Color.rgb(random.nextInt(COLOR_MAX),
    	                    random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
    	            // OR
    	            pixels[index] |= randColor;
    	        }
    	    }
    	    // output bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, bmp.getConfig());
    	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
    	    // return final bitmap
    	    img.setImageBitmap(bmOut);
    	}
     
     public void doGreyscale(View view) {
    	    // constant factors
    	    final double GS_RED = 0.299;
    	    final double GS_GREEN = 0.587;
    	    final double GS_BLUE = 0.114;
    	 
    	    // create output bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(bmp.getWidth(),
    	            bmp.getHeight(),bmp.getConfig());
    	    // pixel information
    	    int A, R, G, B;
    	    int pixel;
    	 
    	    // get image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	 
    	    // scan through every single pixel
    	    for(int x = 0; x < width; ++x) {
    	        for(int y = 0; y < height; ++y) {
    	            // get one pixel color
    	            pixel = bmp.getPixel(x, y);
    	            // retrieve color of all channels
    	            A = Color.alpha(pixel);
    	            R = Color.red(pixel);
    	            G = Color.green(pixel);
    	            B = Color.blue(pixel);
    	            // take conversion up to one single value
    	            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
    	            // set new pixel color to output bitmap
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	 
    	    // return final image
    	    img.setImageBitmap(bmOut);
     }
     
     public void sharpen(View v) {
    	 int weight=10; //input
    	    double[][] SharpConfig = new double[][] {
    	        { 0 , -2    , 0  },
    	        { -2, weight, -2 },
    	        { 0 , -2    , 0  }
    	    };
    	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	    convMatrix.applyConfig(SharpConfig);
    	    convMatrix.Factor = weight - 8;
    	    Bitmap bmOut=ConvolutionMatrix.computeConvolution3x3(bmp, convMatrix);
    	    img.setImageBitmap(bmOut);
    	}
     
     public void embossEffect(View v) {
    	    double[][] EmbossConfig = new double[][] {
    	        { -1 ,  0, -1 },
    	        {  0 ,  4,  0 },
    	        { -1 ,  0, -1 }
    	    };
    	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	    convMatrix.applyConfig(EmbossConfig);
    	    convMatrix.Factor = 1;
    	    convMatrix.Offset = 127;
    	    Bitmap bmOut =ConvolutionMatrix.computeConvolution3x3(bmp, convMatrix);
    	    img.setImageBitmap(bmOut); 
    	}
     
     public void engraveEffect(View v) {
    	 ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	    convMatrix.setAll(0);
    	    convMatrix.Matrix[0][0] = -2;
    	    convMatrix.Matrix[1][1] = 2;
    	    convMatrix.Factor = 1;
    	    convMatrix.Offset = 95;
 	    Bitmap bmOut =ConvolutionMatrix.computeConvolution3x3(bmp, convMatrix);
 	    img.setImageBitmap(bmOut); 
 	}
     public void original(View v){
//    	 BitmapDrawable  abmp = (BitmapDrawable)img.getDrawable();
//         bmp = abmp.getBitmap();
    	 img.setImageBitmap(bmp2);
     }
     public void applySnowEffect(View v) {
    	 	int COLOR_MAX=1000;
    	    // get image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    int[] pixels = new int[width * height];
    	    // get pixel array from source
    	    bmp.getPixels(pixels, 0, width, 0, 0, width, height);
    	    // random object
    	    Random random = new Random();
    	     
    	    int R, G, B, index = 0, thresHold = 50;
    	    // iteration through pixels
    	    for(int y = 0; y < height; ++y) {
    	        for(int x = 0; x < width; ++x) {
    	            // get current index in 2D-matrix
    	            index = y * width + x;              
    	            // get color
    	            R = Color.red(pixels[index]);
    	            G = Color.green(pixels[index]);
    	            B = Color.blue(pixels[index]);
    	            // generate threshold
    	            thresHold = random.nextInt(COLOR_MAX);
    	            if(R > thresHold && G > thresHold && B > thresHold) {
    	                pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
    	            }                           
    	        }
    	    }
    	    // output bitmap                
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
    	    img.setImageBitmap(bmOut); 
    	}
     public void applyGaussianBlur(View v) {
    	    double[][] GaussianBlurConfig = new double[][] {
    	        { 1, 2, 1 },
    	        { 2, 3, 2 },
    	        { 1, 2, 1 }
    	    };
    	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	    convMatrix.applyConfig(GaussianBlurConfig);
    	    convMatrix.Factor = 16;
    	    convMatrix.Offset = 0;
    	    Bitmap bmOut =ConvolutionMatrix.computeConvolution3x3(bmp, convMatrix);
     	    img.setImageBitmap(bmOut); 
    }
     
     public void smooth(View v) {
    	 double value=100; //input
    	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    	    convMatrix.setAll(1);
    	    convMatrix.Matrix[1][1] = value;
    	    convMatrix.Factor = value + 8;
    	    convMatrix.Offset = 1;
    	    Bitmap bmOut =ConvolutionMatrix.computeConvolution3x3(bmp, convMatrix);
     	    img.setImageBitmap(bmOut); 
     	}
     
     public void doBrightness(View v) {
    	    // image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    int value=50; //input from user
    	    // create output bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, bmp.getConfig());
    	    // color information
    	    int A, R, G, B;
    	    int pixel;
    	 
    	    // scan through all pixels
    	    for(int x = 0; x < width; ++x) {
    	        for(int y = 0; y < height; ++y) {
    	            // get pixel color
    	            pixel = bmp.getPixel(x, y);
    	            A = Color.alpha(pixel);
    	            R = Color.red(pixel);
    	            G = Color.green(pixel);
    	            B = Color.blue(pixel);
    	 
    	            // increase/decrease each channel
    	            R += value;
    	            if(R > 255) { R = 255; }
    	            else if(R < 0) { R = 0; }
    	 
    	            G += value;
    	            if(G > 255) { G = 255; }
    	            else if(G < 0) { G = 0; }
    	 
    	            B += value;
    	            if(B > 255) { B = 255; }
    	            else if(B < 0) { B = 0; }
    	 
    	            // apply new pixel color to output bitmap
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	 
    	    // return final image
    	    img.setImageBitmap(bmOut);
    	}
     
     public void Watermark(View v) {
    	    
    	 Point location=new Point(100,100); //input
    	 int color= 4; //input
    	 int alpha=50; //input
    	 int size=12;  //input
    	 boolean underline=true; //input
    	 int w = bmp.getWidth();
    	    int h = bmp.getHeight();
    	    Bitmap result = Bitmap.createBitmap(w, h, bmp.getConfig());
    	 
    	    Canvas canvas = new Canvas(result);
    	    canvas.drawBitmap(bmp, 0, 0, null);
    	 
    	    Paint paint = new Paint();
    	    paint.setColor(color);
    	    paint.setAlpha(alpha);
    	    paint.setTextSize(size);
    	    paint.setAntiAlias(true);
    	    paint.setUnderlineText(underline);
    	    canvas.drawText("Tapsule", location.x, location.y, paint);
    	    img.setImageBitmap(result);
    	    
    	}
     
     public void boost(View v) {
    	 int type=2; //R 1 G 2 B 3
    	 float percent=10;
    	 int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, bmp.getConfig());
    	 
    	    int A, R, G, B;
    	    int pixel;
    	 
    	    for(int x = 0; x < width; ++x) {
    	        for(int y = 0; y < height; ++y) {
    	            pixel = bmp.getPixel(x, y);
    	            A = Color.alpha(pixel);
    	            R = Color.red(pixel);
    	            G = Color.green(pixel);
    	            B = Color.blue(pixel);
    	            if(type == 1) {
    	                R = (int)(R * (1 + percent));
    	                if(R > 255) R = 255;
    	            }
    	            else if(type == 2) {
    	                G = (int)(G * (1 + percent));
    	                if(G > 255) G = 255;
    	            }
    	            else if(type == 3) {
    	                B = (int)(B * (1 + percent));
    	                if(B > 255) B = 255;
    	            }
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	    img.setImageBitmap(bmOut);
     }
     
     public void applyHueFilter(View v) {
    	    // get image size
    	 int level=2;
    	 int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    int[] pixels = new int[width * height];
    	    float[] HSV = new float[3];
    	    // get pixel array from source
    	    bmp.getPixels(pixels, 0, width, 0, 0, width, height);
    	     
    	    int index = 0;
    	    // iteration through pixels
    	    for(int y = 0; y < height; ++y) {
    	        for(int x = 0; x < width; ++x) {
    	            // get current index in 2D-matrix
    	            index = y * width + x;              
    	            // convert to HSV
    	            Color.colorToHSV(pixels[index], HSV);
    	            // increase Saturation level
    	            HSV[0] *= level;
    	            HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
    	            // take color back
    	            pixels[index] |= Color.HSVToColor(HSV);
    	        }
    	    }
    	    // output bitmap                
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
    	    img.setImageBitmap(bmOut);       
    	}
     
     public void applySaturationFilter(View v ) {
    	 int level=2;
    	    // get image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    int[] pixels = new int[width * height];
    	    float[] HSV = new float[3];
    	    // get pixel array from source
    	    bmp.getPixels(pixels, 0, width, 0, 0, width, height);
    	 
    	    int index = 0;
    	    // iteration through pixels
    	    for(int y = 0; y < height; ++y) {
    	        for(int x = 0; x < width; ++x) {
    	            // get current index in 2D-matrix
    	            index = y * width + x;
    	            // convert to HSV
    	            Color.colorToHSV(pixels[index], HSV);
    	            // increase Saturation level
    	            HSV[1] *= level;
    	            HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
    	            // take color back
    	            pixels[index] |= Color.HSVToColor(HSV);
    	        }
    	    }
    	    // output bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
    	    img.setImageBitmap(bmOut);   
    	}
     
     public void tintImage(View v) {
    	 int degree=90;
         int width = bmp.getWidth();
         int height = bmp.getHeight();
  
         int[] pix = new int[width * height];
         bmp.getPixels(pix, 0, width, 0, 0, width, height);
  
         int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
         double angle = (PI * (double)degree) / HALF_CIRCLE_DEGREE;
         
         int S = (int)(RANGE * Math.sin(angle));
         int C = (int)(RANGE * Math.cos(angle));
  
         for (int y = 0; y < height; y++)
             for (int x = 0; x < width; x++) {
                 int index = y * width + x;
                 int r = ( pix[index] >> 16 ) & 0xff;
                 int g = ( pix[index] >> 8 ) & 0xff;
                 int b = pix[index] & 0xff;
                 RY = ( 70 * r - 59 * g - 11 * b ) / 100;
                 GY = (-30 * r + 41 * g - 11 * b ) / 100;
                 BY = (-30 * r - 59 * g + 89 * b ) / 100;
                 Y  = ( 30 * r + 59 * g + 11 * b ) / 100;
                 RYY = ( S * BY + C * RY ) / 256;
                 BYY = ( C * BY - S * RY ) / 256;
                 GYY = (-51 * RYY - 19 * BYY ) / 100;
                 R = Y + RYY;
                 R = ( R < 0 ) ? 0 : (( R > 255 ) ? 255 : R );
                 G = Y + GYY;
                 G = ( G < 0 ) ? 0 : (( G > 255 ) ? 255 : G );
                 B = Y + BYY;
                 B = ( B < 0 ) ? 0 : (( B > 255 ) ? 255 : B );
                 pix[index] = 0xff000000 | (R << 16) | (G << 8 ) | B;
             }
          
         Bitmap outBitmap = Bitmap.createBitmap(width, height, bmp.getConfig());    
         outBitmap.setPixels(pix, 0, width, 0, 0, width, height);
         
         pix = null;
         
         img.setImageBitmap(outBitmap);
     }
     
     public void applyReflection(View v ) {
    	    // gap space between original and reflected
    	    final int reflectionGap = 4;
    	    // get image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();          
    	 
    	    // this will not scale but will flip on the Y axis
    	    Matrix matrix = new Matrix();
    	    matrix.preScale(1, -1);
    	       
    	    // create a Bitmap with the flip matrix applied to it.
    	    // we only want the bottom half of the image
    	    Bitmap reflectionImage = Bitmap.createBitmap(bmp, 0, height/2, width, height/2, matrix, false);          
    	           
    	    // create a new bitmap with same width but taller to fit reflection
    	    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);
    	     
    	    // create a new Canvas with the bitmap that's big enough for
    	    // the image plus gap plus reflection
    	    Canvas canvas = new Canvas(bitmapWithReflection);
    	    // draw in the original image
    	    canvas.drawBitmap(bmp, 0, 0, null);
    	    // draw in the gap
    	    Paint defaultPaint = new Paint();
    	    canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
    	    // draw in the reflection
    	    canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
    	      
    	    // create a shader that is a linear gradient that covers the reflection
    	    Paint paint = new Paint();
    	    LinearGradient shader = new LinearGradient(0, bmp.getHeight(), 0,
    	            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
    	            TileMode.CLAMP);
    	    // set the paint to use this shader (linear gradient)
    	    paint.setShader(shader);
    	    // set the Transfer mode to be porter duff and destination in
    	    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    	    // draw a rectangle using the paint with our linear gradient
    	    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
    	             
    	    img.setImageBitmap(bitmapWithReflection);    	
    	    }
     
     public void createContrast(View v) {
    	 	double value=60;
    	 	// image size
    	    int width = bmp.getWidth();
    	    int height = bmp.getHeight();
    	    // create output bitmap
    	    Bitmap bmOut = Bitmap.createBitmap(width, height, bmp.getConfig());
    	    // color information
    	    int A, R, G, B;
    	    int pixel;
    	    // get contrast value
    	    double contrast = Math.pow((100 + value) / 100, 2);
    	 
    	    // scan through all pixels
    	    for(int x = 0; x < width; ++x) {
    	        for(int y = 0; y < height; ++y) {
    	            // get pixel color
    	            pixel = bmp.getPixel(x, y);
    	            A = Color.alpha(pixel);
    	            // apply filter contrast for every channel R, G, B
    	            R = Color.red(pixel);
    	            R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
    	            if(R < 0) { R = 0; }
    	            else if(R > 255) { R = 255; }
    	 
    	            G = Color.red(pixel);
    	            G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
    	            if(G < 0) { G = 0; }
    	            else if(G > 255) { G = 255; }
    	 
    	            B = Color.red(pixel);
    	            B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
    	            if(B < 0) { B = 0; }
    	            else if(B > 255) { B = 255; }
    	 
    	            // set new pixel color to output bitmap
    	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
    	        }
    	    }
    	 
    	    // return final image
    	    img.setImageBitmap(bmOut);
    	}
     
     public void combineImages(View v) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
    	   
    	 Bitmap s= BitmapFactory.decodeResource(this.getResources(),
                 R.drawable.ic_launcher);;
    	 Bitmap c=bmp;
    	 Bitmap cs = null; 
    	 
    	    int width, height = 0; 
    	     
    	    if(c.getWidth() > s.getWidth()) { 
    	      width = c.getWidth(); 
    	      height = c.getHeight() + s.getHeight(); 
    	    } else { 
    	      width = s.getWidth(); 
    	      height = c.getHeight() + s.getHeight(); 
    	    } 
    	 
    	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
    	 
    	    Canvas comboImage = new Canvas(cs); 
    	 
    	    comboImage.drawBitmap(c, 0f, 0f, null); 
    	    comboImage.drawBitmap(s, 0f, c.getHeight(), null); 
    	 
    	    // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location 
    	    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png"; 
    	 
    	    OutputStream os = null; 
    	    try { 
    	      os = new FileOutputStream(loc + tmpImg); 
    	      cs.compress(CompressFormat.PNG, 100, os); 
    	    } catch(IOException e) { 
    	      Log.e("combineImages", "problem combining images", e); 
    	    }*/ 
    	 
    	    img.setImageBitmap(cs); 
    	  } 

}
