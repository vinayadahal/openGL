package com.mediaplayer.opengl.shapes;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.mediaplayer.opengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Rectangle {

    private FloatBuffer vertexBuffer;    // buffer holding the vertices

    private float vertices[] = {
            -0.9f, -1.3f, 0.0f,        // V1 - bottom left
            -0.9f, 1.3f, 0.0f,        // V2 - top left
            0.9f, -1.3f, 0.0f,        // V3 - bottom right
            0.9f, 1.3f, 0.0f            // V4 - top right
    };

    private FloatBuffer textureBuffer;    // buffer holding the texture coordinates
    private float texture[] = {
            // Mapping coordinates for the vertices
            0.0f, 1.0f,        // top left		(V2)
            0.0f, 0.0f,        // bottom left	(V1)
            1.0f, 1.0f,        // top right	(V4)
            1.0f, 0.0f        // bottom right	(V3)
    };

    private int[] textures = new int[1];

    public void loadGLTexture(GL10 gl, Context context) {
        // loading texture
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img);
        System.out.println("bitmap height:::: " + bitmap.getHeight());
        // generate one texture pointer
        gl.glGenTextures(1, textures, 0);
        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // create nearest filtered texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Clean up
        bitmap.recycle();
    }

    public Rectangle() {
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        vertexByteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = vertexByteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    public void draw(GL10 gl) { //don't mess up the order for showing the picture correctly
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

}
