package com.karavan.ates;

import android.graphics.*;
import android.graphics.drawable.Drawable;

/** Decorative embers stay behind the controls, with no bitmap or network dependency. */
public final class EmberBackdrop extends Drawable {
 private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
 public void draw(Canvas canvas){
  Rect b=getBounds();float w=b.width(),h=b.height();
  paint.setShader(new LinearGradient(0,0,w,h,new int[]{0xff38261f,0xff211b18,0xff151715},null,Shader.TileMode.CLAMP));canvas.drawRect(b,paint);
  paint.setShader(new RadialGradient(w*.95f,h*.12f,Math.max(w,h)*.65f,new int[]{0x477f401e,0x007f401e},null,Shader.TileMode.CLAMP));canvas.drawRect(b,paint);paint.setShader(null);
  paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(1);paint.setColor(0x126fb99c);
  for(int i=0;i<4;i++)canvas.drawCircle(w*1.08f,h*.14f,w*(.24f+i*.13f),paint);
  paint.setStyle(Paint.Style.FILL);paint.setColor(0x35e5a263);
  for(int i=0;i<22;i++){float x=((i*137)%997)/997f*w,y=((i*211+53)%991)/991f*h;canvas.drawCircle(x,y,1+(i%3),paint);}
 }
 public void setAlpha(int alpha){paint.setAlpha(alpha);invalidateSelf();}
 public void setColorFilter(ColorFilter filter){paint.setColorFilter(filter);invalidateSelf();}
 public int getOpacity(){return PixelFormat.OPAQUE;}
}
