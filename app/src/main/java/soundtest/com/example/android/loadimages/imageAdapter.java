package soundtest.com.example.android.loadimages;

/**
 * Created by This Pc on 10/8/2017.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static soundtest.com.example.android.loadimages.MainActivity.mClassifiers;
import static soundtest.com.example.android.loadimages.MainActivity.resultfinal;
import static soundtest.com.example.android.loadimages.R.id.result;

/**
 * Created by This Pc on 9/26/2017.
 */

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.SongHolder> {
    private ArrayList<imageInfo> _images;
    Context context;

    public imageAdapter(Context context ,ArrayList<imageInfo> _images) {
        this.context=context;
        this._images=_images;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(context).inflate(R.layout.rawimage,parent,false); //view has the reference to linear layout
        //Log.i(TAG, "onCreateViewHolder: called for"+viewType);
        return new SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        final imageInfo s=_images.get(position);
        holder.imageName.setText(s.getImageUrl());
        holder.imageName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bitmap bmpcolor= BitmapFactory.decodeFile(MainActivity.dir.getPath() + "/" +s.getImageUrl());
                Bitmap bmp=toGrayscale(bmpcolor);
                //Log.i(TAG, "onClick: clicked"+bmp.toString());
                bmp.setHeight(28);
                bmp.setWidth(28);
                int pixels[] = new int[bmp.getWidth()*bmp.getHeight()];
                float[] pixdouble =new float[pixels.length];
                bmp.getPixels(pixels , 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
                for(int i=0;i<pixels.length;i++)
                {

                        pixdouble[i]=(float)pixels[i];

                }
                String text = "";
                //for each classifier in our array
                for (Classifier classifier : mClassifiers) {
                    //perform classification on the image
                    final Classification res = classifier.recognize(pixdouble);
                    //if it can't classify, output a question mark
                    if (res.getLabel() == null) {
                        text += classifier.name() + ": ?\n";
                    } else {
                        //else output its name
                        text += String.format("%s: %s, %f\n", classifier.name(), res.getLabel(),
                                res.getConf());
                    }
                }
                resultfinal.setText(text);
            }

        });
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    @Override
    public int getItemCount() {
        return _images.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{
        TextView imageName;
        Bitmap imageBitmap;
        public SongHolder(View itemView) { //item view represents the cardview
            super(itemView);
            imageName=itemView.findViewById(R.id.imageName);
        }
    }
}



