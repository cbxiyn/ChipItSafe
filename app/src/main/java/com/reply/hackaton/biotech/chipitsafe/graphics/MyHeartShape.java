package com.reply.hackaton.biotech.chipitsafe.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;

public class MyHeartShape extends View{

    protected static int WIDTH = 200;//500;
    protected static int HEIGHT = WIDTH * 3 / 5;//300;

    public Path path;
    public Paint paint;

    protected int top;
    protected int left;
    private boolean beating = false;
    public int heartColor = Color.RED;//beating ? Color.RED : Color.WHITE;


    public MyHeartShape(Context context) {
        super(context);

        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        top=10;
        left=10;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fill the canvas with background color
        //canvas.drawColor(Color.WHITE);
        paint.setShader(null);

        // Defining of  the heart path starts
        path.moveTo(left+WIDTH/2, (top+HEIGHT/4)+10); // Starting point
        // Create a cubic Bezier cubic left path
        path.cubicTo(left+WIDTH/5,top,
                (left+WIDTH/4)-10,top+4*HEIGHT/5,
                left+WIDTH/2, top+HEIGHT);
        // This is right Bezier cubic path
        path.cubicTo(left+3*WIDTH/4,top+4*HEIGHT/5,
                (left+4*WIDTH/5)+10,top,
                left+WIDTH/2, (top+HEIGHT/4)+10);

        paint.setColor(heartColor); // Set with heart color
        //paint.setShader(shader);
        paint.setStyle(Style.FILL); // Fill with heart color
        canvas.drawPath(path, paint); // Actual drawing happens here

        // Draw Blue Boundary
        paint.setShader(null);
        paint.setColor(Color.WHITE); // Change the boundary color
        paint.setStrokeWidth(4);
        paint.setStyle(Style.STROKE);
        canvas.drawPath(path, paint);
        beating = !beating;

    }
    /*
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fill the canvas with background color
        canvas.drawColor(Color.WHITE);
        paint.setShader(null);

        // Defining of  the heart path starts
        path.moveTo(left+WIDTH/2, top+HEIGHT/4); // Starting point
        // Create a cubic Bezier cubic left path
        path.cubicTo(left+WIDTH/5,top,
                left+WIDTH/4,top+4*HEIGHT/5,
                left+WIDTH/2, top+HEIGHT);
        // This is right Bezier cubic path
        path.cubicTo(left+3*WIDTH/4,top+4*HEIGHT/5,
                left+4*WIDTH/5,top,
                left+WIDTH/2, top+HEIGHT/4);

        paint.setColor(Color.RED); // Set with heart color
        //paint.setShader(shader);
        paint.setStyle(Style.FILL); // Fill with heart color
        canvas.drawPath(path, paint); // Actual drawing happens here

        // Draw Blue Boundary
        paint.setShader(null);
        paint.setColor(Color.WHITE); // Change the boundary color
        paint.setStrokeWidth(4);
        paint.setStyle(Style.STROKE);
        canvas.drawPath(path, paint);

    }
    */
}
