package com.hanif.exproleshader

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Shader1 : AppCompatActivity() {

    private var glView: GLSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MyGLSurfaceView(this)
        setContentView(glView)
    }
}

class MyGLSurfaceView(context: Context) : GLSurfaceView(context){

    private val renderer: MyGLRenderer
    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer()
        setRenderer(renderer)

    }
}


class MyGLRenderer : GLSurfaceView.Renderer{

    private lateinit var square: Square
    private lateinit var triangle: Triangle

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        triangle = Triangle()
        GLES20.glClearColor(0.0f, .0f, 0.0f, 1.0f)
        square = Square()
    }
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix,0,-ratio,ratio,-1f,1f,3f,7f)
    }
    override fun onDrawFrame(gl: GL10?) {
//        triangle.draw()

        Matrix.setLookAtM(viewMatrix,0,0f,0f,3f,0f,0f,0f,0f,1.0f,0f)
        Matrix.multiplyMM(vPMatrix,0,projectionMatrix,0,viewMatrix,0)
        square.draw(vPMatrix)
    }
}

